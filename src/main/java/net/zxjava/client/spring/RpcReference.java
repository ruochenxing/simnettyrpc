package net.zxjava.client.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import net.zxjava.client.netty.MessageSendExecutor;
import net.zxjava.common.serial.RpcSerializeProtocol;

/**
 * nettyrpc:reference自定义标签实体类
 * 
 * 封装自己定制的实例化逻辑(例如你想用工厂模式来实例化，或者Class.getInstance())，然后让spring统一管理(
 * Spring提供的工厂Bean, 方便)
 */
public class RpcReference implements FactoryBean<Object>, InitializingBean {

	private String interfaceName;
	private String ipAddr;
	private String protocol;

	/**
	 * 所有的属性被初始化后调用。
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		MessageSendExecutor.getInstance().setRpcServerLoader(ipAddr, RpcSerializeProtocol.valueOf(protocol));
	}

	/**
	 * ctx.getBean()时调用
	 */
	@Override
	public Object getObject() throws Exception {
		return MessageSendExecutor.getInstance().execute(getObjectType());
	}

	@Override
	public Class<?> getObjectType() {
		try {
			return this.getClass().getClassLoader().loadClass(interfaceName);
		} catch (ClassNotFoundException e) {
			System.err.println("spring analyze fail!");
		}
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
