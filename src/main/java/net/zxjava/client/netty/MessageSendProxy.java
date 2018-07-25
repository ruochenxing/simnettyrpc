package net.zxjava.client.netty;

import java.lang.reflect.Method;
import java.util.UUID;

import com.google.common.reflect.AbstractInvocationHandler;

import net.zxjava.server.message.MessageRequest;

/**
 * Rpc客户端消息处理
 */
public class MessageSendProxy<T> extends AbstractInvocationHandler {

	@Override
	public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
		MessageRequest request = new MessageRequest();
		request.setMessageId(UUID.randomUUID().toString());
		request.setClassName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setTypeParameters(method.getParameterTypes());
		request.setParametersVal(args);
		MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
		handler.sendRequest(request);
		return null;
	}
}
