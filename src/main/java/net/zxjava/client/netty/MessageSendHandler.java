package net.zxjava.client.netty;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;

/**
 * Rpc客户端处理模块
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {
	private ConcurrentHashMap<String, MessageCallBack> mapCallBack = new ConcurrentHashMap<String, MessageCallBack>();

	private volatile Channel channel;
	private SocketAddress remoteAddr;

	public Channel getChannel() {
		return channel;
	}

	public SocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageResponse response = (MessageResponse) msg;
		System.out.println("client recv response" + response);
		String messageId = response.getMessageId();
		MessageCallBack callBack = mapCallBack.get(messageId);
		if (callBack != null) {
			mapCallBack.remove(messageId);
			callBack.over(response);
		}
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

	public MessageCallBack sendRequest(MessageRequest request) {
		MessageCallBack callBack = new MessageCallBack();
		mapCallBack.put(request.getMessageId(), callBack);
		channel.writeAndFlush(request);
		return callBack;
	}
}
