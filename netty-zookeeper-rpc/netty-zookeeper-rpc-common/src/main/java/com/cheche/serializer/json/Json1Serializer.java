package com.cheche.serializer.json;

import com.cheche.serializer.Serializer;
import com.cheche.util.JsonUtil;

/**
 * @author fudy
 * @date 2023/3/1
 */
public class Json1Serializer extends Serializer {

  @Override
  public <T> byte[] serialize(T obj) {
    return JsonUtil.serialize(obj);
  }

  @Override
  public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
    return JsonUtil.deserialize(bytes, clazz);
  }
}
