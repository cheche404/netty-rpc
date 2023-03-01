package com.cheche.handler;

import com.cheche.codec.*;
import com.cheche.serializer.Serializer;
import com.cheche.serializer.kryo.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * RpcClientInitializer
 *
 * @author fudy
 * @date 2023/2/16
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    Serializer serializer = KryoSerializer.class.newInstance();
    ChannelPipeline cp = socketChannel.pipeline();
    cp.addLast(new IdleStateHandler(0, 0, Beat.BEAT_INTERVAL, TimeUnit.SECONDS));
    cp.addLast(new RpcEncoder(RpcRequest.class, serializer));
    cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
    cp.addLast(new RpcDecoder(RpcResponse.class, serializer));
    cp.addLast(new RpcClientHandler());
  }

}
