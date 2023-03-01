package com.cheche.serializer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化管理器
 *
 * @author fudy
 * @date 2023/2/15
 */
public class SerializerManager {

  /**
   * 存储所有序列化实例
   */
  private static final Map<Byte, ISerializer> SERIALIZER_MAP = new ConcurrentHashMap<>();


  static {
    ISerializer serializer = new JavaSerializer();
    SERIALIZER_MAP.put(serializer.getType(), serializer);
    serializer = new JSONSerializer();
    SERIALIZER_MAP.put(serializer.getType(), serializer);
    serializer = new ProtoStuffSerializer();
    SERIALIZER_MAP.put(serializer.getType(), serializer);
    serializer = new HessianSerializer();
    SERIALIZER_MAP.put(serializer.getType(), serializer);
  }

  public static ISerializer getSerializer(byte key) {
    ISerializer iserializer = SERIALIZER_MAP.get(key);
    if (Objects.isNull(iserializer)) {
      // 默认用 java 序列化
      iserializer = new JavaSerializer();
    }
    return iserializer;
  }


}
