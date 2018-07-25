package net.zxjava.client.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.zxjava.common.serial.RpcSerializeProtocol;
import net.zxjava.server.config.RpcSystemConfig;

/**
 * NettyRPC客户端，要加载NettyRPC服务端的一些上下文（Context）信息
 * 
 * rpc服务器配置加载
 */
public class RpcServerLoader {

	private static volatile RpcServerLoader rpcServerLoader;
	private static final String DELIMITER = RpcSystemConfig.DELIMITER;
	private MessageSendHandler messageSendHandler = null;

	// 方法返回到Java虚拟机的可用的处理器数量
	private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
	// netty nio
	private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);

	private static ListeningExecutorService threadPoolExecutor = MoreExecutors
			.listeningDecorator(Executors.newCachedThreadPool());

	// 等待Netty服务端链路建立通知信号
	private Lock lock = new ReentrantLock();
	private Condition connectStatus = lock.newCondition();
	private Condition handlerStatus = lock.newCondition();

	private RpcServerLoader() {
	}

	// 并发双重锁定
	public static RpcServerLoader getInstance() {
		if (rpcServerLoader == null) {
			synchronized (RpcServerLoader.class) {
				if (rpcServerLoader == null) {
					rpcServerLoader = new RpcServerLoader();
				}
			}
		}
		return rpcServerLoader;
	}

	public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {
		String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
		if (ipAddr.length == RpcSystemConfig.IPADDR_OPRT_ARRAY_LENGTH) {
			String host = ipAddr[0];
			int port = Integer.parseInt(ipAddr[1]);
			final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);

			ListenableFuture<Boolean> listenableFuture = threadPoolExecutor
					.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, serializeProtocol));
			// 监听线程池异步的执行结果成功与否再决定是否唤醒全部的客户端RPC线程
			Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
					try {
						lock.lock();
						if (messageSendHandler == null) {
							handlerStatus.await();
						}
						// Futures异步回调，唤醒所有rpc等待线程
						if (result.equals(Boolean.TRUE) && messageSendHandler != null) {
							connectStatus.signalAll();
						}
					} catch (InterruptedException ex) {
						Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, ex);
					} finally {
						lock.unlock();
					}
				}

				@Override
				public void onFailure(Throwable t) {
					t.printStackTrace();
				}
			}, threadPoolExecutor);
		}
	}

	public void setMessageSendHandler(MessageSendHandler messageInHandler) {
		try {
			lock.lock();
			this.messageSendHandler = messageInHandler;
			// 唤醒所有等待客户端RPC线程
			handlerStatus.signal();
		} finally {
			lock.unlock();
		}
	}

	public MessageSendHandler getMessageSendHandler() throws InterruptedException {
		try {
			lock.lock();
			// Netty服务端链路没有建立完毕之前，先挂起等待
			if (messageSendHandler == null) {
				connectStatus.await();
			}
			return messageSendHandler;
		} finally {
			lock.unlock();
		}
	}

	public void unLoad() {
		threadPoolExecutor.shutdown();
		eventLoopGroup.shutdownGracefully();
	}
}
