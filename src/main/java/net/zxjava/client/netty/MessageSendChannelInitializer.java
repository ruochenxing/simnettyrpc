package net.zxjava.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.zxjava.common.serial.RpcSerializeProtocol;
import net.zxjava.server.serial.protostuff.ProtostuffCodecUtil;
import net.zxjava.server.serial.protostuff.ProtostuffDecoder;
import net.zxjava.server.serial.protostuff.ProtostuffEncoder;

public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel> {

	private RpcSerializeProtocol protocol;

	MessageSendChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
		this.protocol = protocol;
		return this;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		switch (protocol) {
		case PROTOSTUFFSERIALIZE: {
			ProtostuffCodecUtil util = new ProtostuffCodecUtil();
			util.setRpcDirect(false);
			pipeline.addLast(new ProtostuffEncoder(util));// out
			pipeline.addLast(new ProtostuffDecoder(util));// in
			pipeline.addLast(new MessageSendHandler());// in
			break;
		}
		default:
			break;
		}
	}

}
