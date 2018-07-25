package net.zxjava.server.message;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class MessageRequest implements Serializable {

	private static final long serialVersionUID = 2267573499140418394L;
	private String messageId;
	private String className;
	private String methodName;
	private Class<?>[] typeParameters;
	private Object[] parametersVal;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(Class<?>[] typeParameters) {
		this.typeParameters = typeParameters;
	}

	public Object[] getParametersVal() {
		return parametersVal;
	}

	public void setParametersVal(Object[] parametersVal) {
		this.parametersVal = parametersVal;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this, new String[] { "typeParameters", "parametersVal" });
	}
}
