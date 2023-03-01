package com.cheche.handler;

import com.cheche.domain.RequestHolder;
import com.cheche.domain.RpcFuture;
import com.cheche.domain.RpcResponse;
import com.cheche.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * rpc client handler
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                              RpcProtocol<RpcResponse> msg) throws Exception {
    long requestId = msg.getHeader().getRequestId();
    RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
    future.getPromise().setSuccess(msg.getContent());
  }
}
