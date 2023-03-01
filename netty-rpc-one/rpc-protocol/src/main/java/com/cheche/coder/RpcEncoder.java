package com.cheche.coder;

import com.cheche.protocol.Header;
import com.cheche.protocol.RpcProtocol;
import com.cheche.serializer.ISerializer;
import com.cheche.serializer.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {


  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext,
                        RpcProtocol<Object> in,
                        ByteBuf out) throws Exception {
    Header header = in.getHeader();
    out.writeShort(header.getMagic());
    out.writeByte(header.getSerialType());
    out.writeByte(header.getReqType());
    out.writeLong(header.getRequestId());
    // 序列化
    ISerializer iSerializer = SerializerManager.getSerializer(header.getSerialType());
    byte[] bytes = iSerializer.serializer(in.getContent());
    out.writeInt(bytes.length);
    out.writeBytes(bytes);
  }
}
