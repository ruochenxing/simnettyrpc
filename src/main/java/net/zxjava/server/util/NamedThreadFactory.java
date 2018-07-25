package net.zxjava.server.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

/**
 * 线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {

	private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
	private final AtomicInteger mThreadNum = new AtomicInteger(1);
	private final String prefix;
	private final boolean daemoThread;
	private final ThreadGroup threadGroup;

	public NamedThreadFactory() {
		this("rpcserver-threadpool-" + THREAD_NUMBER.getAndIncrement(), false);
	}

	public NamedThreadFactory(String prefix) {
		this(prefix, false);
	}

	public NamedThreadFactory(String prefix, boolean daemo) {
		this.prefix = StringUtils.isNotEmpty(prefix) ? prefix + "-thread-" : "";
		daemoThread = daemo;
		// 不添加启动参数直接运行，则相当于没有启动管理器，SecurityManager打印出来为null
		SecurityManager s = System.getSecurityManager();// TODO ???
		// https://blog.csdn.net/hjh200507609/article/details/50330773
		threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		String name = prefix + mThreadNum.getAndIncrement();
		Thread ret = new Thread(threadGroup, runnable, name, 0);
		ret.setDaemon(daemoThread);
		return ret;
	}

}
