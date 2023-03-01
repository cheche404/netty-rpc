package com.cheche.handler;

/**
 * AsyncRPCCallback
 *
 * @author fudy
 * @date 2023/2/16
 */
public interface AsyncRPCCallback {

  /**
   * success
   *
   * @param result result
   */
  void success(Object result);

  /**
   * fail
   *
   * @param e e
   */
  void fail(Exception e);

}
