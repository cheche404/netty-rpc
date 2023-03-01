package com.cheche.proxy;


import java.lang.reflect.Proxy;

/**
 * @author fudy
 * @date 2023/2/15
 */
public class RpcClientProxy {

  public <T> T clientProxy(final Class<T> interfaces, String host, int port) {
    return (T) Proxy.newProxyInstance(interfaces.getClassLoader(),
      new Class<?>[]{interfaces}, new RpcInvokerProxy(host, port));
  }

}
