package com.cheche.coder;

import com.cheche.constants.RpcConstant;
import com.cheche.domain.RpcRequest;
import com.cheche.domain.RpcResponse;
import com.cheche.enums.ReqType;
import com.cheche.protocol.Header;
import com.cheche.protocol.RpcProtocol;
import com.cheche.serializer.ISerializer;
import com.cheche.serializer.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Objects;

/**
 * 解码器
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RpcDecoder extends ByteToMessageDecoder {

  /**
   * 首先如果接受的数据长度小于头部的总长度则直接返回
   * 这里使用markReaderIndex标记索引，后期可以还原数据
   * 我们首先读取两个字节的魔数，判读是否相等 然后按照消息协议中定义好的顺序逐步读取序列化类型、请求类型以及消息体的内容
   * 最后根据请求类型以及序列化类型对数据进行反序列化后返回
   *
   * @param ctx channelHandlerContext
   * @param in byteBuf
   * @param out 对象 list
   * @throws Exception 异常
   */
  @Override
  protected void decode(ChannelHandlerContext ctx,
                        ByteBuf in,
                        List<Object> out) throws Exception {
    // 如果接受的数据长度小于头部的总长度则直接返回
    if (in.readableBytes() < RpcConstant.HEAD_TOTAL_LEN) {
      return;
    }
    // 标记开始读时候的索引
    in.markReaderIndex();
    // 读取魔数
    short magic = in.readShort();
    if (magic != RpcConstant.MAGIC) {
      throw new IllegalArgumentException("Illegal request parameter 'magic'," + magic);
    }
    // 读取一个字节的序列化类型
    byte serializerType = in.readByte();
    // 读取一个字节的消息类型
    byte reqType = in.readByte();
    // 读取 requestId
    long requestId = in.readLong();
    // 读取数据报文长度
    int dataLength = in.readInt();
    if (in.readableBytes() < dataLength) {
      // 还原索引 并返回
      in.resetReaderIndex();
      return;
    }
    // 读取消息体
    byte[] content = new byte[dataLength];
    in.readBytes(content);

    Header header = new Header(magic, serializerType, reqType, requestId, dataLength);
    ISerializer serializer = SerializerManager.getSerializer(serializerType);
    ReqType rt = ReqType.findByCode(reqType);
    switch (Objects.requireNonNull(rt)) {
      case REQUEST:
        // 反序列化
        RpcRequest request = serializer.deserializer(content, RpcRequest.class);
        // 返回消息体
        RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
        reqProtocol.setHeader(header);
        reqProtocol.setContent(request);
        // 传递给 handler
        out.add(reqProtocol);
        break;
      case RESPONSE:
        // 反序列化
        RpcResponse response = serializer.deserializer(content, RpcResponse.class);
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
        resProtocol.setHeader(header);
        resProtocol.setContent(response);
        out.add(resProtocol);
        break;
      case HEARTBEAT:
      case CONNECT:
        // TODO
        break;
      default:
        break;
    }

  }

}
