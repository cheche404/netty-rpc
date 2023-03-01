package com.cheche.core;

import com.cheche.handler.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * rpc 服务端 server
 *
 * @author fudy
 * @date 2023/2/14
 */
public class RpcServer {

  /**
   * 服务地址
   */
  private final String serverAddress;

  /**
   * 服务端口
   */
  private final int serverPort;

  public RpcServer(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
  }

  public void start() {
    System.out.println("rpc server starting....");
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(boss, worker)
      .channel(NioServerSocketChannel.class)
      .childHandler(new RpcServerInitializer());
    try {
      ChannelFuture future = bootstrap.bind(this.serverAddress, this.serverPort).sync();
      System.out.println("rpc server started on " + serverAddress + ":" + serverPort);
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }

  }

}
