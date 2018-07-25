package net.zxjava.server.netty;

import java.util.Map;

import io.netty.channel.ChannelPipeline;
import net.zxjava.common.serial.RpcSerializeProtocol;
import net.zxjava.server.serial.protostuff.ProtostuffCodecUtil;
import net.zxjava.server.serial.protostuff.ProtostuffDecoder;
import net.zxjava.server.serial.protostuff.ProtostuffEncoder;

public class RpcRecvSerializeFrame {

	private Map<String, Object> handlerMap = null;

	public RpcRecvSerializeFrame(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
		switch (protocol) {
		case PROTOSTUFFSERIALIZE: {
			ProtostuffCodecUtil util = new ProtostuffCodecUtil();
			util.setRpcDirect(true);
			// 返回数据
			pipeline.addLast(new ProtostuffEncoder(util));// out
			// 接收到的数据进行解码然后处理
			pipeline.addLast(new ProtostuffDecoder(util));// in
			pipeline.addLast(new MessageRecvHandler(handlerMap));// in
			break;
		}
		default:
			break;
		}
	}

}
