package net.zxjava.server.netty;

import java.util.Map;
import java.util.concurrent.Callable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;

public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

	private final Map<String, Object> handlerMap;

	public MessageRecvHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageRequest request = (MessageRequest) msg;
		MessageResponse response = new MessageResponse();
		System.out.println("server recv request=" + request);
		RecvInitializeTaskFacade facade = new RecvInitializeTaskFacade(request, response, handlerMap);
		Callable<Boolean> recvTask = facade.getTask();
		recvTask.call();
		System.out.println("server recv response=" + response);
		ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		// 网络有异常要关闭通道
		ctx.close();
	}
}
