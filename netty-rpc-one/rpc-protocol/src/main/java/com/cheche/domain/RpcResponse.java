package com.cheche.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC response
 *
 * @author fudy
 * @date 2023/2/15
 */
@Data
public class RpcResponse implements Serializable {

  /**
   * 返回数据
   */
  private Object data;

  /**
   * 返回消息
   */
  private String msg;
}
