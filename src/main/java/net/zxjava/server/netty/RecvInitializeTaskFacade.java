package net.zxjava.server.netty;

import java.util.Map;
import java.util.concurrent.Callable;

import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;

public class RecvInitializeTaskFacade {
	private MessageRequest request;
	private MessageResponse response;
	private Map<String, Object> handlerMap;

	public RecvInitializeTaskFacade(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap) {
		this.request = request;
		this.response = response;
		this.handlerMap = handlerMap;
	}

	public Callable<Boolean> getTask() {
		return new MessageRecvInitializeTaskAdapter(request, response, handlerMap);
	}
}
