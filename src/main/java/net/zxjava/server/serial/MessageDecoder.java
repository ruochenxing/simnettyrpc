package net.zxjava.server.serial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RPC消息的解码器 二进制流反序列化成消息对象
 * 
 * NettyRPC是基于TCP协议的，TCP在传输数据的过程中会出现所谓的“粘包”现象，
 * 所以我们的MessageDecoder要对RPC消息体的长度进行校验，如果不满足RPC消息报文头中指定的消息体长度，我们直接重置一下ByteBuf读索引的位置
 */
public class MessageDecoder extends ByteToMessageDecoder {

	final public static int MESSAGE_LENGTH = MessageCodecUtil.MESSAGE_LENGTH;
	private MessageCodecUtil util = null;

	public MessageDecoder(final MessageCodecUtil util) {
		this.util = util;
	}

	// 处理完后交由下一个解码器处理，也就是MessageRecvHandler
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		// 出现粘包导致消息头长度不对，直接返回 （可读信息段比头部信息都小，不太可能）
		if (in.readableBytes() < MessageDecoder.MESSAGE_LENGTH) {
			return;
		}

		in.markReaderIndex();
		// 读取消息的内容长度
		int messageLength = in.readInt();

		if (messageLength < 0) {
			ctx.close();
		}

		// 读到的消息长度和报文头的已知长度不匹配。那就重置一下ByteBuf读索引的位置
		if (in.readableBytes() < messageLength) {
			in.resetReaderIndex();
			return;
		} else {
			byte[] messageBody = new byte[messageLength];
			in.readBytes(messageBody);// 读取消息体
			try {
				Object obj = util.decode(messageBody);// 解码
				out.add(obj);
			} catch (IOException ex) {
				Logger.getLogger(MessageDecoder.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}