package net.zxjava.server.serial.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

import net.zxjava.server.message.MessageRequest;
import net.zxjava.server.message.MessageResponse;
import net.zxjava.server.serial.RpcSerialize;

/**
 * 定义真正的Protostuff序列化、反序列化类
 */
public class ProtostuffSerialize implements RpcSerialize {
	private static SchemaCache cachedSchema = SchemaCache.getInstance();
	private static Objenesis objenesis = new ObjenesisStd(true);

	private boolean rpcDirect = false;

	public boolean getRpcDirect() {
		return rpcDirect;
	}

	public void setRpcDirect(boolean rpcDirect) {
		this.rpcDirect = rpcDirect;
	}

	@Override
	public void serialize(OutputStream output, Object object) throws IOException {
		Class<?> cls = (Class<?>) object.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema schema = getSchema(cls);
			ProtostuffIOUtil.writeTo(output, object, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	@Override
	public Object deserialize(InputStream input) throws IOException {
		try {
			Class cls = getRpcDirect() ? MessageRequest.class : MessageResponse.class;
			Object message = (Object) objenesis.newInstance(cls);
			Schema<Object> schema = getSchema(cls);
			ProtostuffIOUtil.mergeFrom(input, message, schema);
			return message;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private static <T> Schema<T> getSchema(Class<T> cls) {
		return (Schema<T>) cachedSchema.get(cls);
	}

}
