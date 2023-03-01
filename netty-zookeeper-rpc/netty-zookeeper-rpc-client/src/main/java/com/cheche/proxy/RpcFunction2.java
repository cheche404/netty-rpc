package com.cheche.proxy;

/**
 * @author fudy
 * @date 2023/2/16
 */
@FunctionalInterface
public interface RpcFunction2<T, P1, P2> extends SerializableFunction<T> {

  /**
   * apply
   *
   * @param t t
   * @param p1 p1
   * @param p2 p2
   * @return Object
   */
  Object apply(T t, P1 p1, P2 p2);

}
