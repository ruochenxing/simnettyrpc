package net.zxjava.server.serial;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

/**
 * RPC消息编解码接口
 */
public interface MessageCodecUtil {
	
	// RPC消息报文头长度4个字节
	final static int MESSAGE_LENGTH = 4;

	void encode(final ByteBuf out, final Object message) throws IOException;

	Object decode(byte[] body) throws IOException;
}
