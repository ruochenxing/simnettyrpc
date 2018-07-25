package net.zxjava.common.serial;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 抽象出RPC消息序列化，协议类型对象
 */
public enum RpcSerializeProtocol {
	JDKSERIALIZE("jdknative"), KRYOSERIALIZE("kryo"), HESSIANSERIALIZE("hessian"), PROTOSTUFFSERIALIZE("protostuff");

	private String serializeProtocol;

	private RpcSerializeProtocol(String serializeProtocol) {
		this.serializeProtocol = serializeProtocol;
	}

	@Override
	public String toString() {
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return ReflectionToStringBuilder.toString(this);
	}

	public String getProtocol() {
		return serializeProtocol;
	}
}
