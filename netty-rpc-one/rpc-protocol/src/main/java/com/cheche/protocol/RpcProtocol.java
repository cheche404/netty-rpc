package com.cheche.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息体
 *
 * @author fudy
 * @date 2023/2/15
 */
@Data
public class RpcProtocol<T> implements Serializable {

  /**
   * 消息头
   */
  private Header header;

  /**
   * 消息内容
   */
  private T content;

}
