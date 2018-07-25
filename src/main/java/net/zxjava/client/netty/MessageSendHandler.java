package net.zxjava.client.netty;

import java.net.SocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.zxjava.server.message.MessageRequest;

/**
 * Rpc客户端处理模块
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {
	private volatile Channel channel;
	private SocketAddress remoteAddr;

	public Channel getChannel() {
		return channel;
	}

	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.remoteAddr = this.channel.remoteAddress();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		this.channel = ctx.channel();
	}

	public void sendRequest(MessageRequest request) {
		channel.writeAndFlush(request);
	}
}
