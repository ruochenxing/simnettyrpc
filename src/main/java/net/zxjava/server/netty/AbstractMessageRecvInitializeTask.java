package net.zxjava.server.netty;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;
import net.zxjava.server.spring.BeanFactoryUtils;

public abstract class AbstractMessageRecvInitializeTask implements Callable<Boolean> {

	protected MessageRequest request = null;
	protected MessageResponse response = null;
	protected Map<String, Object> handlerMap = null;
	protected Modular modular = BeanFactoryUtils.getBean("modular");

	public AbstractMessageRecvInitializeTask(MessageRequest request, MessageResponse response,
			Map<String, Object> handlerMap) {
		this.request = request;
		this.response = response;
		this.handlerMap = handlerMap;
	}

	@Override
	public Boolean call() {
		try {
			acquire();
			response.setMessageId(request.getMessageId());
			injectInvoke();
			Object result = reflect(request);
			boolean isInvokeSucc = result != null;
			if (isInvokeSucc) {
				response.setResult(result);
				response.setError("");
				response.setReturnNotNull(true);
			} else {
				response.setResult(null);
				response.setError("error");
				injectFilterInvoke();
			}
			return Boolean.TRUE;
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.printf("RPC Server invoke error!\n");
			injectFailInvoke(t);
			return Boolean.FALSE;
		} finally {
			release();
		}
	}

	private Object reflect(MessageRequest request) throws Throwable {
		ProxyFactory weaver = new ProxyFactory(new MethodInvoker());
		NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
		advisor.setMappedName("invoke");
		advisor.setAdvice(new MethodProxyAdvisor(handlerMap));
		weaver.addAdvisor(advisor);
		MethodInvoker mi = (MethodInvoker) weaver.getProxy();
		Object obj = invoke(mi, request);
		return obj;
	}

	private Object invoke(MethodInvoker mi, MessageRequest request) throws Throwable {
		if (modular != null) {
			ModuleProvider<Object> provider = modular.invoke(new ModuleInvoker<Object>() {

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public Class getInterface() {
					return mi.getClass().getInterfaces()[0];
				}

				@Override
				public Object invoke(MessageRequest request) throws Throwable {
					return mi.invoke(request);
				}

				@Override
				public void destroy() {

				}
			}, request);
			return provider.getInvoker().invoke(request);
		} else {
			return mi.invoke(request);
		}
	}

	protected abstract void injectInvoke();

	protected abstract void injectFailInvoke(Throwable error);

	protected abstract void injectFilterInvoke();

	protected abstract void acquire();

	protected abstract void release();
}
