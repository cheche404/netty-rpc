package com.cheche.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.cheche.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian2 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class HessianSerializer extends Serializer {

  @Override
  public <T> byte[] serialize(T obj) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Hessian2Output out = new Hessian2Output(bos);
    try {
      out.writeObject(obj);
      out.flush();
      return bos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        out.close();
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
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    Hessian2Input hi = new Hessian2Input(bis);
    try {
      return hi.readObject();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        hi.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      try {
        bis.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
