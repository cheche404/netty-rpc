package com.cheche.serializer.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.cheche.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian1 序列化
 *
 * @author fudy
 * @date 2023/2/15
 */
public class Hessian1Serializer extends Serializer {

  @Override
  public <T> byte[] serialize(T obj) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    HessianOutput output = new HessianOutput(bos);
    try {
      output.writeObject(obj);
      output.flush();
      return bos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        output.close();
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
    HessianInput input = new HessianInput(bis);
    try {
      return input.readObject();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      input.close();
      try {
        bis.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
