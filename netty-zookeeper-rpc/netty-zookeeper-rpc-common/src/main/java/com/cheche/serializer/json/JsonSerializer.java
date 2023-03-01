package com.cheche.serializer.json;

import com.alibaba.fastjson.JSON;
import com.cheche.serializer.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * json 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class JsonSerializer extends Serializer {
  @Override
  public <T> byte[] serialize(T obj) {
    return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
    return JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), clazz);
  }
}
