package com.cheche.core;

import com.cheche.codec.*;
import com.cheche.serializer.Serializer;
import com.cheche.serializer.kryo.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * rpc server initializer
 *
 * @author fudy
 * @date 2023/2/16
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

  private Map<String, Object> handlerMap;
  private ThreadPoolExecutor threadPoolExecutor;

  public RpcServerInitializer(Map<String, Object> handerMap, ThreadPoolExecutor threadPoolExecutor) {
    this.handlerMap = handerMap;
    this.threadPoolExecutor = threadPoolExecutor;
  }

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    //        Serializer serializer = ProtostuffSerializer.class.newInstance();
//        Serializer serializer = HessianSerializer.class.newInstance();
    Serializer serializer = KryoSerializer.class.newInstance();
    ChannelPipeline cp = channel.pipeline();
    cp.addLast(new IdleStateHandler(0, 0, Beat.BEAT_TIMEOUT, TimeUnit.SECONDS));
    cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
    cp.addLast(new RpcDecoder(RpcRequest.class, serializer));
    cp.addLast(new RpcEncoder(RpcResponse.class, serializer));
    cp.addLast(new RpcServerHandler(handlerMap, threadPoolExecutor));
  }
}
