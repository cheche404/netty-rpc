package com.cheche.serializer.protostuff;

import com.cheche.serializer.Serializer;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class ProtostuffSerializer extends Serializer {

  private Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

  private Objenesis objenesis = new ObjenesisStd(true);

  @SuppressWarnings("unchecked")
  private <T> Schema<T> getSchema(Class<T> cls) {
    return (Schema<T>) cachedSchema.computeIfAbsent(cls, RuntimeSchema::createFrom);
  }

  @Override
  public <T> byte[] serialize(T obj) {
    Class<T> cls = (Class<T>) obj.getClass();
    LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    try {
      Schema<T> schema = getSchema(cls);
      return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
    } catch (Exception e) {
        throw new IllegalStateException(e);
    } finally {
        buffer.clear();
    }
  }

  @Override
  public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
    try {
      T obj = objenesis.newInstance(clazz);
      Schema<T> schema = getSchema(clazz);
      ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
      return obj;
    } catch (Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
    }
  }
}
