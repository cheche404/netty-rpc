package com.cheche.core;

import com.cheche.codec.Beat;
import com.cheche.codec.RpcRequest;
import com.cheche.codec.RpcResponse;
import com.cheche.util.ServiceUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * rpc handler
 * rpc request processor
 *
 * @author fudy
 * @date 2023/2/16
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

  private final Map<String, Object> handlerMap;
  private final ThreadPoolExecutor threadPoolExecutor;

  public RpcServerHandler(Map<String, Object> handlerMap, final ThreadPoolExecutor threadPoolExecutor) {
    this.handlerMap = handlerMap;
    this.threadPoolExecutor = threadPoolExecutor;
  }

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx,
                              final RpcRequest request) throws Exception {
    if (Beat.BEAT_ID.equalsIgnoreCase(request.getRequestId())) {
      log.info("server read heartbeat ping...");
      return;
    }
    threadPoolExecutor.execute(() -> {
      log.info("receive request:[{}]", request.getRequestId());
      RpcResponse response = new RpcResponse();
      response.setRequestId(request.getRequestId());
      // 调用服务
      try {
        Object obj = handle(request);
        response.setResult(obj);
      } catch (Throwable e) {
        response.setError(e.toString());
        log.error("RPC Server handle request error", e);
      }

      ctx.writeAndFlush(response).addListeners(
          channelFuture -> {
            log.info("Send response for request " + request.getRequestId());
          });
    });


  }

  public Object handle(RpcRequest request) throws Throwable {
    String className = request.getClassName();
    String version = request.getVersion();
    String serviceKey = ServiceUtil.makeServiceKey(className, version);
    Object serviceBean = handlerMap.get(serviceKey);
    if (Objects.isNull(serviceBean)) {
      log.warn("Can not get service name, className:[{}], version:[{}]", className, version);
      return null;
    }
    Class<?> serviceClass = serviceBean.getClass();
    String methodName = request.getMethodName();
    Class<?>[] parameterTypes = request.getParameterTypes();
    Object[] parameters = request.getParameters();

    // jdk 动态代理
//    Method method = serviceClass.getMethod(methodName, parameterTypes);
//    method.setAccessible(true);
//    return method.invoke(serviceBean, parameters);


    // Cglib reflect
    FastClass serviceFastClass = FastClass.create(serviceClass);
//    FastMethod fastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//    return fastMethod.invoke(serviceClass, parameters);
    // cglib higher-performance 效率最高
    int index = serviceFastClass.getIndex(methodName, parameterTypes);
    return serviceFastClass.invoke(index, serviceBean, parameters);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.warn("Server caught exception: " + cause.getMessage());
    ctx.close();
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (ctx instanceof IdleStateEvent) {
      ctx.close();
      log.warn("Channel idle in last [{}] seconds, close it", Beat.BEAT_TIMEOUT);
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }
}
