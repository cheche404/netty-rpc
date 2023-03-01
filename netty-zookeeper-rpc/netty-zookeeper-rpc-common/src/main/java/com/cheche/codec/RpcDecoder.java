package com.cheche.codec;

import com.cheche.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * rpc decoder
 *
 * @author fudy
 * @date 2023/2/15
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

  /**
   * 通用类
   */
  private Class<?> genericClass;

  /**
   * 序列化
   */
  private Serializer serializer;

  public RpcDecoder(Class<?> genericClass, Serializer serializer) {
    this.genericClass = genericClass;
    this.serializer = serializer;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx,
                        ByteBuf in,
                        List<Object> out) throws Exception {
    // 可读字节小于4，说明消息长度还没满，直接返回
    if (in.readableBytes() < 4) {
      return;
    }
    // 设置回滚点
    in.markReaderIndex();
    // 读取前 4 个字节（消息头）存储的消息长度，同时会使 readerIndex 向前移动 4 个指针
    int dataLength = in.readInt();
    // 如果可读字节小于消息长度， 说明消息不完整，重置读指针，返回
    if (in.readableBytes() < dataLength) {
      in.resetReaderIndex();
      return;
    }
    byte[] data = new byte[dataLength];
    in.readBytes(data);
    Object obj = null;
    try {
      // 反序列化
      obj = serializer.deserialize(data, genericClass);
      // 添加返回信息
      out.add(obj);
    } catch (Exception e) {
        log.error("decoder error:[{}]", e.toString());
    }

  }
}
