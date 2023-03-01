package com.cheche.proxy;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * SerializableFunction
 *
 * @author fudy
 * @date 2023/2/16
 */
public interface SerializableFunction<T> extends Serializable {

  /**
   * getName
   *
   * @return String
   * @throws Exception Exception
   */
  default String getName() throws Exception {
    Method write = this.getClass().getDeclaredMethod("writeReplace");
    write.setAccessible(true);
    SerializedLambda serializedLambda = (SerializedLambda) write.invoke(this);
    return serializedLambda.getImplMethodName();
  }

}
