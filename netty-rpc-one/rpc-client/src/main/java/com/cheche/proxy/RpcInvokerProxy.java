package com.cheche.proxy;

import com.cheche.core.RpcClient;
import com.cheche.constants.RpcConstant;
import com.cheche.domain.RequestHolder;
import com.cheche.domain.RpcFuture;
import com.cheche.domain.RpcRequest;
import com.cheche.domain.RpcResponse;
import com.cheche.enums.ReqType;
import com.cheche.enums.SerializerType;
import com.cheche.protocol.Header;
import com.cheche.protocol.RpcProtocol;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcInvokerProxy implements InvocationHandler {

  private final String host;

  private final int port;

  public RpcInvokerProxy(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public Object invoke(Object obj, Method method, Object[] args) throws Throwable {
    RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
    long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
    Header header = new Header(RpcConstant.MAGIC, SerializerType.HESSIAN_SERIAL.code(),
      ReqType.REQUEST.code(), requestId, 0);
    reqProtocol.setHeader(header);
    RpcRequest request = new RpcRequest();
    request.setClassName(method.getDeclaringClass().getName());
    request.setParams(args);
    request.setMethodName(method.getName());
    request.setParamsTypes(method.getParameterTypes());
    reqProtocol.setContent(request);

    // 短连接
    RpcClient client = new RpcClient(this.host, this.port);
    // 通过设置 DefaultEventLoop 进行轮询获取结果
    RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
    // 保存请求 ID 和返回数据的对应关系
    RequestHolder.REQUEST_MAP.put(requestId, future);
    // 发送请求
    client.sendRequest(reqProtocol);
    // 返回异步调用的数据
    return future.getPromise().get().getData();
  }

}
