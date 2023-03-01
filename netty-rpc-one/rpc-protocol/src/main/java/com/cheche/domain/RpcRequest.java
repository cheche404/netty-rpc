package com.cheche.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC request 类
 *
 * @author fudy
 * @date 2023/2/15
 */
@Data
public class RpcRequest implements Serializable {

  /**
   * class 名称
   */
  private String className;

  /**
   * 方法名
   */
  private String methodName;

  /**
   * 请求参数
   */
  private Object[] params;

  /**
   * 参数类型
   */
  private Class<?>[] paramsTypes;
}
