package com.cheche.domain;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * RPC 返回数据 future
 *
 * @author fudy
 * @date 2023/2/15
 */
@Data
public class RpcFuture<T> {

  private Promise<T> promise;

  public RpcFuture(Promise<T> promise) {
    this.promise = promise;
  }
}
