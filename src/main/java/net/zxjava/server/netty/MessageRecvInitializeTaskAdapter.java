package net.zxjava.server.netty;

import java.util.Map;

import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;

public class MessageRecvInitializeTaskAdapter extends AbstractMessageRecvInitializeTask {

	public MessageRecvInitializeTaskAdapter(MessageRequest request, MessageResponse response,
			Map<String, Object> handlerMap) {
		super(request, response, handlerMap);
	}

	@Override
	protected void injectInvoke() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void injectFailInvoke(Throwable error) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void injectFilterInvoke() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void acquire() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void release() {
		// TODO Auto-generated method stub

	}

}
