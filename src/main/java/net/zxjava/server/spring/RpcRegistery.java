package net.zxjava.server.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import net.zxjava.common.serial.RpcSerializeProtocol;
import net.zxjava.server.netty.MessageRecvExecutor;

/**
 * 自定义标签实体类
 */
public class RpcRegistery implements InitializingBean, DisposableBean {

	private String ipAddress;
	private String protocol;
	private String echoApiPort;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		MessageRecvExecutor ref = MessageRecvExecutor.getInstance();
		ref.setServerAddress(ipAddress);
		ref.setEchoApiPort(Integer.parseInt(echoApiPort));
		ref.setSerializeProtocol(Enum.valueOf(RpcSerializeProtocol.class, protocol));
		ref.start();
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getEchoApiPort() {
		return echoApiPort;
	}

	public void setEchoApiPort(String echoApiPort) {
		this.echoApiPort = echoApiPort;
	}

}
