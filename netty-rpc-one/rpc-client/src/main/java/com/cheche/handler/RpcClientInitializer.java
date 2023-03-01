package com.cheche.handler;

import com.cheche.coder.RpcDecoder;
import com.cheche.coder.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * rpc client 初始化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel.pipeline()
      .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0))
      .addLast(new RpcDecoder())
      .addLast(new RpcEncoder())
      .addLast(new RpcClientHandler());



  }
}
