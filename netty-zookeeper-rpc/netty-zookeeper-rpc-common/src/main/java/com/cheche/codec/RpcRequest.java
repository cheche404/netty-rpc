package com.cheche.codec;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * rpc request
 *
 * @author fudy
 * @date 2023/2/15
 */
@Getter
@Setter
public class RpcRequest implements Serializable {

  /**
   * request id
   */
  private String requestId;

  /**
   * class name
   */
  private String className;

  /**
   * method name
   */
  private String methodName;

  /**
   * parameterTypes
   */
  private Class<?>[] parameterTypes;

  /**
   * parameters
   */
  private Object[] parameters;

  /**
   * version
   */
  private String version;



}
