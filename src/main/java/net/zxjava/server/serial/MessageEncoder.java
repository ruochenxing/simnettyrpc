package net.zxjava.server.serial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC消息的编码器MessageEncoder
 * 
 * RPC消息对象编码成二进制流的格式
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

	private MessageCodecUtil util = null;

	public MessageEncoder(final MessageCodecUtil util) {
		this.util = util;
	}

	@Override
	protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
		util.encode(out, msg);
	}
}
