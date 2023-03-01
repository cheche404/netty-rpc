package com.cheche.serializer.kryo;

import com.cheche.serializer.Serializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * kryo 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class KryoSerializer extends Serializer {

  private final KryoPool pool = KryoPoolFactory.getKryoPoolInstance();

  @Override
  public <T> byte[] serialize(T obj) {
    Kryo kryo = pool.borrow();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Output out = new Output(bos);
    try {
      kryo.writeObject(out, obj);
      out.close();
      return bos.toByteArray();
    } catch (Exception e) {
        throw new RuntimeException(e);
    } finally {
      try {
        bos.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      pool.release(kryo);
    }
  }

  @Override
  public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
    Kryo kryo = pool.borrow();
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    Input input = new Input(bis);
    try {
      Object obj = kryo.readObject(input, clazz);
      input.close();
      return obj;
    } catch (Exception e) {
        throw new RuntimeException(e);
    } finally {
      try {
        bis.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      pool.release(kryo);
    }
  }

}
