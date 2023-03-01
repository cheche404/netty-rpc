package com.cheche.serializer;

import com.cheche.annotation.SerializerClass;
import com.cheche.enums.SerializerType;

import java.io.*;

/**
 * java 序列化实现
 *
 * @author fudy
 * @date 2023/2/15
 */
@SerializerClass(value = "java")
public class JavaSerializer implements ISerializer {

  @Override
  public <T> byte[] serializer(T obj) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(bos);
      // java 序列化
      oos.writeObject(obj);
      oos.flush();
      return bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new byte[0];
  }

  @Override
  public <T> T deserializer(byte[] data, Class<T> clazz) {
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(new ByteArrayInputStream(data));
      return (T) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public byte getType() {
    return SerializerType.JAVA_SERIAL.code();
  }
}
