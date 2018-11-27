package net.zxjava.server.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import net.zxjava.server.filter.ServiceFilterBinder;
import net.zxjava.server.netty.MessageRecvExecutor;

/**
 * nettyrpc:service自定义标签实体类
 * 
 * ApplicationListener ?? 当容器初始化完成之后，需要处理一些操作，比如一些数据的加载、初始化缓存、特定任务的注册等等。
 * 这个时候我们就可以使用Spring提供的ApplicationListener来进行操作。
 * 
 * 事件监听器
 */
public class RpcService implements ApplicationContextAware, ApplicationListener<ApplicationEvent> {
	private String interfaceName;
	private String ref;
	private String filter;
	private ApplicationContext applicationContext;

	/***
	 * 对消息进行接受处理
	 * 
	 * 系统每调用一次applicationContext.publishEvent 该方法就会被调用
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		ServiceFilterBinder binder = new ServiceFilterBinder();
		binder.setObject(applicationContext.getBean(ref));
		System.out.println("interfaceName=" + interfaceName);
		System.out.println("ref=" + ref);
		// handlerMap.put
		MessageRecvExecutor.getInstance().getHandlerMap().put(interfaceName, binder);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		// 事件发布
		// AbstractApplicationContext.finishRefresh也会调用一次publishEvent
		// applicationContext.publishEvent(new ServerStartEvent(new Object()));
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}
