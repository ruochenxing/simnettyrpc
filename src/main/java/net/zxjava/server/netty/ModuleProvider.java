package net.zxjava.server.netty;

public interface ModuleProvider<T> {
    ModuleInvoker<T> getInvoker();

    void destoryInvoker();
}
