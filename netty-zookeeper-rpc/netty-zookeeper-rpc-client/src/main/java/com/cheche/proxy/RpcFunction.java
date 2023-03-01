package com.cheche.proxy;

/**
 * @author fudy
 * @date 2023/2/16
 */
@FunctionalInterface
public interface RpcFunction<T, P> extends SerializableFunction<T> {

  /**
   * apply
   *
   * @param t t
   * @param p p
   * @return obj
   */
  Object apply(T t, P p);

}
