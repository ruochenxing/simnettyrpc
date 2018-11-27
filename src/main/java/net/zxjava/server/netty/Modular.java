package net.zxjava.server.netty;

import net.zxjava.server.message.MessageRequest;

public interface Modular {
    <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request);
}
