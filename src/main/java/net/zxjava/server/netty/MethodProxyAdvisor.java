package net.zxjava.server.netty;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import net.zxjava.server.filter.ServiceFilterBinder;
import net.zxjava.server.message.MessageRequest;

public class MethodProxyAdvisor implements MethodInterceptor {
	private Map<String, Object> handlerMap;

	public MethodProxyAdvisor(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] params = invocation.getArguments();
		if (params.length <= 0) {
			return null;
		}
		MessageRequest request = (MessageRequest) params[0];
		String className = request.getClassName();
		Object serviceBean = handlerMap.get(className);
		((MethodInvoker) invocation.getThis()).setServiceBean(((ServiceFilterBinder) serviceBean).getObject());
		Object result = invocation.proceed();
		return result;
	}

}
