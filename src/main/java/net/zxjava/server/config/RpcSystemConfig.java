package net.zxjava.server.config;

public class RpcSystemConfig {
	public static final int SYSTEM_PROPERTY_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
	public static final String DELIMITER = ":";
	public static final int IPADDR_OPRT_ARRAY_LENGTH = 2;
	public static final int SERIALIZE_POOL_MAX_TOTAL = 500;
	public static final int SERIALIZE_POOL_MIN_IDLE = 10;
	public static final int SERIALIZE_POOL_MAX_WAIT_MILLIS = 5000;
	public static final int SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 600000;
	public static final String FILTER_RESPONSE_MSG = "Illegal request,RPC server refused to respond!";

	public static final int SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS = Integer.getInteger("nettyrpc.default.thread.nums",
			16);
	public static final int SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS = Integer.getInteger("nettyrpc.default.queue.nums",
			-1);
}
