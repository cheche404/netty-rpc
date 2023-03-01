package com.cheche.serializer;

import com.cheche.annotation.SerializerClass;
import com.cheche.enums.SerializerType;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
@SerializerClass(value = "protostuff")
public class ProtoStuffSerializer implements ISerializer {

  /**
   * 构建schema的过程可能会比较耗时，因此希望使用过的类对应的schema能被缓存起来
   */
  private static final Map<Class<?>, Schema<?>> CACHED_SCHEMA = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  private static <T> Schema<T> getSchema(Class<T> cls) {
    return (Schema<T>) CACHED_SCHEMA.computeIfAbsent(cls, RuntimeSchema::createFrom);
  }

  @Override
  public <T> byte[] serializer(T obj) {
    // 获得对象的类
    Class<T> cls = (Class<T>) obj.getClass();
    // 使用LinkedBuffer分配一块默认大小的buffer空间
    LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    try {
      // 通过对象的类构建对应的schema
      Schema<T> schema = getSchema(cls);
      return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    } finally {
      // 清空 buffer
      buffer.clear();
    }
  }

  @Override
  public <T> T deserializer(byte[] data, Class<T> clazz) {
    try {
      Objenesis objenesis = new ObjenesisStd(true);
      T obj = objenesis.newInstance(clazz);
      Schema<T> schema = getSchema(clazz);
      ProtostuffIOUtil.mergeFrom(data, obj, schema);
      return obj;
    } catch (Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  public byte getType() {
    return SerializerType.PROTOSTUFF_SERIAL.code();
  }
}
