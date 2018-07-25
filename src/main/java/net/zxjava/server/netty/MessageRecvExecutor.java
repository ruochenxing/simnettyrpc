package net.zxjava.server.netty;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.zxjava.common.serial.RpcSerializeProtocol;
import net.zxjava.server.config.RpcSystemConfig;
import net.zxjava.server.util.NamedThreadFactory;

/**
 * Rpc服务器执行模块 为了让Bean获取它所在的Spring容器，可以让该Bean实现ApplicationContextAware接口。
 * 
 */
public class MessageRecvExecutor implements ApplicationContextAware {
	private String serverAddress;
	private int echoApiPort;
	// 默认的序列化采用Java原生本地序列化机制，并且优化了线程池异步调用的层次结构
	private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.PROTOSTUFFSERIALIZE;

	private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
	private static final String DELIMITER = RpcSystemConfig.DELIMITER;

	ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyRPC ThreadFactory");
	EventLoopGroup boss = new NioEventLoopGroup();
	EventLoopGroup worker = new NioEventLoopGroup(PARALLEL, threadRpcFactory, SelectorProvider.provider());

	private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

	private MessageRecvExecutor() {
		handlerMap.clear();
	}

	private static class MessageRecvExecutorHolder {
		static final MessageRecvExecutor INSTANCE = new MessageRecvExecutor();
	}

	public static MessageRecvExecutor getInstance() {
		return MessageRecvExecutorHolder.INSTANCE;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub

	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getEchoApiPort() {
		return echoApiPort;
	}

	public void setEchoApiPort(int echoApiPort) {
		this.echoApiPort = echoApiPort;
	}

	public RpcSerializeProtocol getSerializeProtocol() {
		return serializeProtocol;
	}

	public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
		this.serializeProtocol = serializeProtocol;
	}

	/**
	 * 启动rpc服务器端 启动echo api
	 */
	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
				.childHandler(
						new MessageRecvChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol))
				.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
		String[] ipAddress = serverAddress.split(MessageRecvExecutor.DELIMITER);
		if (ipAddress.length == RpcSystemConfig.IPADDR_OPRT_ARRAY_LENGTH) {
			final String host = ipAddress[0];
			final int port = Integer.parseInt(ipAddress[1]);
			ChannelFuture future = null;
			// 启动rpc服务器端
			try {
				future = bootstrap.bind(host, port).sync();
				// 当bind操作完成后触发operationComplete方法
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(final ChannelFuture channelFuture) throws Exception {
						if (channelFuture.isSuccess()) {
							System.out.println("RPC Server start success!");
							channelFuture.channel().closeFuture();
						}
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.printf("RPC Server start fail! ipAddress value error,ipAddress=" + ipAddress);
		}
	}
}
