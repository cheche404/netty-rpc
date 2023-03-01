package com.cheche.serializer.java;

import com.cheche.serializer.Serializer;

import java.io.*;
import java.util.Objects;

/**
 * java 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class JavaSerializer extends Serializer {

  @Override
  public <T> byte[] serialize(T obj) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
      return bos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        oos.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        bos.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

    }
  }

  @Override
  public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (Objects.nonNull(ois)) {
          ois.close();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
