package com.cheche.core;

import com.cheche.registry.ServiceRegistry;
import com.cheche.util.ServiceUtil;
import com.cheche.util.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * rpc server
 *
 * @author fudy
 * @date 2023/2/15
 */
@Slf4j
public class NettyServer extends Server {

  /**
   * thread
   */
  private Thread thread;

  /**
   * server address
   */
  private String serverAddress;

  /**
   * server port
   */
  private int port;

  /**
   * service registry
   */
  private ServiceRegistry serviceRegistry;

  /**
   * service map
   */
  private Map<String, Object> serviceMap = new HashMap<>();

  public NettyServer(String serverAddress, String registryAddress) {
    this.serverAddress = serverAddress;
    this.serviceRegistry = new ServiceRegistry(registryAddress);
  }


  public void addService(String interfaceName, String version, Object serviceBean) {
    log.info("Adding service, interface: [{}], version: [{}], bean:[{}]", interfaceName, version, serviceBean);
    String serviceKey = ServiceUtil.makeServiceKey(interfaceName, version);
    serviceMap.put(serviceKey, serviceBean);
  }

  @Override
  public void start() throws Exception {
    thread = new Thread(new Runnable() {
      ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.createThreadPool(
        NettyServer.class.getSimpleName(), 16, 32);

      @Override
      public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
          ServerBootstrap bootstrap = new ServerBootstrap();
          bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new RpcServerInitializer(serviceMap, threadPoolExecutor))
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

          String[] array = serverAddress.split(":");
          String host = array[0];
          int port = Integer.parseInt(array[1]);
          ChannelFuture future = bootstrap.bind(host, port).sync();

          if (serviceRegistry != null) {
            serviceRegistry.registerService(host, port, serviceMap);
          }
          log.info("Server started on port {}", port);
          future.channel().closeFuture().sync();
        } catch (Exception e) {
          if (e instanceof InterruptedException) {
            log.info("Rpc server remoting server stop");
          } else {
            log.error("Rpc server remoting server error", e);
          }
        } finally {
          try {
            serviceRegistry.unregisterService();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
          }
        }
      }
    });
    thread.start();
    thread.start();
  }

  @Override
  public void stop() throws Exception {
  // destroy server thread
    if (thread != null && thread.isAlive()) {
      thread.interrupt();
    }
  }
}
