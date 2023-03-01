package com.cheche.serializer;

/**
 * serializer abstract
 *
 * @author fudy
 * @date 2023/2/15
 */
public abstract class Serializer {

  /**
   * serialize
   *
   * @param obj  obj
   * @return byte[]
   * @param <T> T
   */
  public abstract <T> byte[] serialize(T obj);

  /**
   * deserialize
   *
   * @param bytes bytes
   * @param clazz clazz
   * @return Object
   * @param <T> T
   */
  public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);


}
