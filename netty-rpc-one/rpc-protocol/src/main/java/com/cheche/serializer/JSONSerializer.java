package com.cheche.serializer;

import com.alibaba.fastjson.JSON;
import com.cheche.annotation.SerializerClass;
import com.cheche.enums.SerializerType;

import java.nio.charset.StandardCharsets;

/**
 * json 序列化实现
 *
 * @author fudy
 * @date 2023/2/15
 */
@SerializerClass(value = "json")
public class JSONSerializer implements ISerializer {

  @Override
  public <T> byte[] serializer(T obj) {
    return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public <T> T deserializer(byte[] data, Class<T> clazz) {
   return JSON.parseObject(new String(data, StandardCharsets.UTF_8), clazz);
  }

  @Override
  public byte getType() {
    return SerializerType.JSON_SERIAL.code();
  }
}
