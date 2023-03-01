package com.cheche.codec;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * rpc response
 *
 * @author fudy
 * @date 2023/2/15
 */
@Setter
@Getter
public class RpcResponse implements Serializable {

  /**
   * request id
   */
  private String requestId;

  /**
   * error
   */
  private String error;

  /**
   * result
   */
  private Object result;

  public boolean isError() {
    return error != null;
  }

}
