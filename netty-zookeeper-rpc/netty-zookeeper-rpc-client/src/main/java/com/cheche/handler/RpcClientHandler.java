package com.cheche.handler;

import com.cheche.codec.Beat;
import com.cheche.codec.RpcRequest;
import com.cheche.codec.RpcResponse;
import com.cheche.connect.ConnectionManager;
import com.cheche.protocol.RpcProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc client handler
 *
 * @author fudy
 * @date 2023/2/16
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

  private ConcurrentHashMap<String, RpcFuture> pendingRPC = new ConcurrentHashMap<>();
  private volatile Channel channel;
  private SocketAddress remotePeer;
  private RpcProtocol rpcProtocol;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    this.remotePeer = this.channel.remoteAddress();
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    super.channelRegistered(ctx);
    this.channel = ctx.channel();
  }



  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
                              RpcResponse response) throws Exception {
    String requestId = response.getRequestId();
    RpcFuture rpcFuture = pendingRPC.get(requestId);
    if (Objects.nonNull(rpcFuture)) {
        pendingRPC.remove(requestId);
        rpcFuture.done(response);
    } else {
      log.warn("Can not get pending response for request id: " + requestId);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    log.error("Client caught exception: " + cause.getMessage());
    ctx.close();
  }

  public void close() {
    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  public RpcFuture sendRequest(RpcRequest request) {
    RpcFuture rpcFuture = new RpcFuture(request);
    pendingRPC.put(request.getRequestId(), rpcFuture);
    try {
      ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
      if (!channelFuture.isSuccess()) {
        log.error("Send request [{}] error", request.getRequestId());
      }
    } catch (InterruptedException e) {
      log.error("Send request exception: [{}]", e.getMessage());
    }
    return rpcFuture;
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      sendRequest(Beat.BEAT_PING);
      log.debug("Client send beat-ping to " + remotePeer);
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    ConnectionManager.getInstance().removeHandler(rpcProtocol);
  }

  public void setRpcProtocol(RpcProtocol rpcProtocol) {
    this.rpcProtocol = rpcProtocol;
  }

}
