package com.cheche.handler;

import com.cheche.core.SpringBeanManager;
import com.cheche.domain.RpcRequest;
import com.cheche.domain.RpcResponse;
import com.cheche.enums.ReqType;
import com.cheche.protocol.Header;
import com.cheche.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * rpc server 事件处理器
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
                              RpcProtocol<RpcRequest> msg) throws Exception {
    RpcProtocol<RpcResponse> response = new RpcProtocol<>();
    // 处理返回数据
    Header header = msg.getHeader();
    header.setReqType(ReqType.RESPONSE.code());
    // 反射调用执行对应方法
    Object objResult = invoke(msg.getContent());
    response.setHeader(header);

    RpcResponse rpcResponse = new RpcResponse();
    rpcResponse.setMsg("success");
    rpcResponse.setData(objResult);
    response.setContent(rpcResponse);
    ctx.writeAndFlush(response);
  }

  /**
   * 反射调用相应的方法
   *
   * @param request RPC request 类
   * @return 执行结果
   */
  private Object invoke(RpcRequest request) {
    try {
      // 反射
      Class<?> clazz = Class.forName(request.getClassName());
      // 加载实例
      Object obj = SpringBeanManager.getBean(clazz);
      // 获取方法
      Method method = clazz.getDeclaredMethod(request.getMethodName(), request.getParamsTypes());
      // 调用
      return method.invoke(obj, request.getParams());
    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
