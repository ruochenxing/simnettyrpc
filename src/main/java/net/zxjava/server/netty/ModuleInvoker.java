package net.zxjava.server.netty;

import net.zxjava.server.message.MessageRequest;

public interface ModuleInvoker<T> {
    Class<T> getInterface();

    Object invoke(MessageRequest request) throws Throwable;

    void destroy();
}

