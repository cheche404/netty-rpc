package com.cheche.proxy;

import com.cheche.handler.RpcFuture;

/**
 * @author fudy
 * @date 2023/2/16
 */
public interface RpcService<T, P, FN extends SerializableFunction<T>> {

  /**
   * call
   *
   * @param funcName funcName
   * @param args args
   * @return RpcFuture
   * @throws Exception Exception
   */
  RpcFuture call(String funcName, Object... args) throws Exception;

  /**
   * call
   *
   * @param fn fn
   * @param args args
   * @return RpcFuture
   * @throws Exception Exception
   */
  RpcFuture call(FN fn, Object... args) throws Exception;

}
