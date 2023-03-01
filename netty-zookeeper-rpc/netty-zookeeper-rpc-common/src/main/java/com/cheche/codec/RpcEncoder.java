package com.cheche.codec;

import com.cheche.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * rpc encoder
 *
 * @author fudy
 * @date 2023/2/15
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder {

  /**
   * 通用类
   */
  private Class<?> genericClass;

  /**
   * 序列化
   */
  private Serializer serializer;

  public RpcEncoder(Class<?> genericClass, Serializer serializer) {
    this.genericClass = genericClass;
    this.serializer = serializer;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx,
                        Object in,
                        ByteBuf out) throws Exception {
    if (genericClass.isInstance(in)) {
      try {
        byte[] data = serializer.serialize(in);
        out.writeInt(data.length);
        out.writeBytes(data);
      } catch (Exception e) {
          log.error("encode error:[{}]", e.toString());
      }
    }
  }
}
