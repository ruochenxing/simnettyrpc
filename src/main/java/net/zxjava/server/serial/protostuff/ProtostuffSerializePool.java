package net.zxjava.server.serial.protostuff;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import net.zxjava.server.config.RpcSystemConfig;

//https://www.cnblogs.com/shangxiaofei/p/6233001.html
public class ProtostuffSerializePool {

	private GenericObjectPool<ProtostuffSerialize> protostuffPool;
	private static volatile ProtostuffSerializePool INSTANCE = null;

	public static ProtostuffSerializePool getInstance() {
		if (INSTANCE == null) {
			synchronized (ProtostuffSerializePool.class) {
				if (INSTANCE == null) {
					INSTANCE = new ProtostuffSerializePool(RpcSystemConfig.SERIALIZE_POOL_MAX_TOTAL,
							RpcSystemConfig.SERIALIZE_POOL_MIN_IDLE, RpcSystemConfig.SERIALIZE_POOL_MAX_WAIT_MILLIS,
							RpcSystemConfig.SERIALIZE_POOL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
				}
			}
		}
		return INSTANCE;
	}

	@Deprecated
	public ProtostuffSerializePool() {
		protostuffPool = new GenericObjectPool<ProtostuffSerialize>(new ProtostuffSerializeFactory());
	}

	public ProtostuffSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis,
			final long minEvictableIdleTimeMillis) {
		protostuffPool = new GenericObjectPool<ProtostuffSerialize>(new ProtostuffSerializeFactory());
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMinIdle(minIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		protostuffPool.setConfig(config);
	}

	public void restore(final ProtostuffSerialize object) {
		getProtostuffPool().returnObject(object);
	}

	public ProtostuffSerialize borrow() {
		try {
			return getProtostuffPool().borrowObject();
		} catch (final Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public GenericObjectPool<ProtostuffSerialize> getProtostuffPool() {
		return protostuffPool;
	}
}
