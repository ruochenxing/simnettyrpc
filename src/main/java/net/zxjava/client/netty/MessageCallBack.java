package net.zxjava.client.netty;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.zxjava.server.config.RpcSystemConfig;
import net.zxjava.server.message.MessageResponse;

public class MessageCallBack {
	private MessageResponse response;
	private Lock lock = new ReentrantLock();
	private Condition finish = lock.newCondition();

	public MessageCallBack() {
	}

	public Object start() {
		try {
			lock.lock();
			await();
			if (this.response != null) {
				boolean isInvokeSucc = getInvokeResult();
				if (isInvokeSucc) {
					if (this.response.getError().isEmpty()) {
						return this.response.getResult();
					} else {
						throw new RuntimeException(this.response.getError());
					}
				} else {
					throw new RuntimeException(RpcSystemConfig.FILTER_RESPONSE_MSG);
				}
			} else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	public void over(MessageResponse reponse) {
		try {
			lock.lock();
			finish.signal();
			this.response = reponse;
		} finally {
			lock.unlock();
		}
	}

	private void await() {
		boolean timeout = false;
		try {
			// 设定一下超时时间，rpc服务器太久没有相应的话，就默认返回空吧。
			timeout = finish.await(30 * 1000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!timeout) {
			throw new RuntimeException("Timeout request,NettyRPC server request timeout!");
		}
	}

	private boolean getInvokeResult() {
		return (!this.response.getError().equals(RpcSystemConfig.FILTER_RESPONSE_MSG)
				&& (!this.response.getReturnNotNull()
						|| (this.response.getReturnNotNull() && this.response.getResult() != null)));
	}

}
