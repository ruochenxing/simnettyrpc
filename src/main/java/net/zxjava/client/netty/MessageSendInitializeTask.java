package net.zxjava.client.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.zxjava.common.serial.RpcSerializeProtocol;

public class MessageSendInitializeTask implements Callable<Boolean> {

	private EventLoopGroup eventLoopGroup = null;
	private InetSocketAddress serverAddress = null;
	private RpcSerializeProtocol protocol;

	public MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress remoteAddr,
			RpcSerializeProtocol serializeProtocol) {
		this.eventLoopGroup = eventLoopGroup;
		this.serverAddress = remoteAddr;
		this.protocol = serializeProtocol;
	}

	@Override
	public Boolean call() throws Exception {
		Bootstrap b = new Bootstrap();
		b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.remoteAddress(serverAddress);
		b.handler(new MessageSendChannelInitializer().buildRpcSerializeProtocol(protocol));
		ChannelFuture channelFuture = b.connect();
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
					RpcServerLoader.getInstance().setMessageSendHandler(handler);
				}
			}
		});
		return Boolean.TRUE;
	}

}
