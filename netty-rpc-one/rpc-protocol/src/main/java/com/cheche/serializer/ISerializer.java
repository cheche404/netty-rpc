package com.cheche.serializer;

/**
 * @author fudy
 * @date 2023/2/15
 */
public interface ISerializer {

  /**
   * 序列化
   *
   * @param obj 需要序列化的对象
   * @return 序列化后的值
   * @param <T> 泛型
   */
  <T> byte[] serializer(T obj);

  /**
   * 反序列化
   *
   * @param data 需要反序列化的数据
   * @param clazz 序列化后 接收的class对象
   * @return class对象
   * @param <T> 泛型
   */
  <T> T deserializer(byte[] data, Class<T> clazz);

  /**
   * 获取序列化类型
   *
   * @return 序列化类型
   */
  byte getType();

}
