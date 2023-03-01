package com.cheche.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.cheche.annotation.SerializerClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
@SerializerClass(value = "hessian")
public class HessianSerializer implements ISerializer {

  @Override
  public <T> byte[] serializer(T obj) {
    Hessian2Output output = null;
    byte[] result = null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      output = new Hessian2Output(bos);
      output.flush();
      result = bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public <T> T deserializer(byte[] data, Class<T> clazz) {
    T result = null;
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    Hessian2Input input = new Hessian2Input();
    try {
      result = (T) input.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public byte getType() {
    return 0;
  }
}
