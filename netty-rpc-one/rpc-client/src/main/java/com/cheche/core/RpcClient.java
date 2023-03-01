package com.cheche.core;

import com.cheche.domain.RpcRequest;
import com.cheche.handler.RpcClientHandler;
import com.cheche.handler.RpcClientInitializer;
import com.cheche.protocol.RpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * rpc client 端 服务
 *
 * @author fudy
 * @date 2023/02/15
 */
public class RpcClient {

  private final Bootstrap bootstrap;

  private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

  /**
   * 连接地址
   */
  private String host;

  /**
   * 端口
   */
  private int port;

  public RpcClient(String host, int port) {
    System.out.println("rpc client starting...");
    bootstrap = new Bootstrap();
    bootstrap.group(eventLoopGroup)
      .channel(NioSocketChannel.class)
      .handler(new RpcClientInitializer());
    this.host = host;
    this.port = port;
  }

  /**
   * 发送数据
   *
   * @param protocol 消息体
   */
  public void sendRequest(RpcProtocol<RpcRequest> protocol) {
    final ChannelFuture future = bootstrap.connect(host, port);
    // 监听连接
    future.addListener(listener -> {
      if (future.isSuccess()) {
        System.out.println("connect rpc server " + host + ":" + port + " success." );
      } else {
        System.out.println("connect rpc server " + host + ":" + port + " error." );
        future.cause().printStackTrace();
        eventLoopGroup.shutdownGracefully();
      }
    });
    future.channel().writeAndFlush(protocol);
  }
}