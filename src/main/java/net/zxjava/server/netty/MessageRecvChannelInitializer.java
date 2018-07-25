package net.zxjava.server.netty;

import java.util.Map;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.zxjava.common.serial.RpcSerializeProtocol;

public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

	private RpcSerializeProtocol protocol;
	private RpcRecvSerializeFrame frame = null;

	MessageRecvChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
		this.protocol = protocol;
		return this;
	}

	MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
		frame = new RpcRecvSerializeFrame(handlerMap);
	}

	// 当新连接accept的时候，这个方法会调用
	// 注册请求的处理handle
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		frame.select(protocol, pipeline);
	}

}
