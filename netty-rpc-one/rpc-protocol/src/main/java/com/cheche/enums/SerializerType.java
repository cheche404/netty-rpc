package com.cheche.enums;

/**
 * 序列化类型
 *
 * @author fudy
 * @date 2023/2/15
 */
public enum SerializerType {

  /**
   * json 序列化
   */
  JSON_SERIAL((byte) 1),

  /**
   * java 序列化
   */
  JAVA_SERIAL((byte) 2),

  /**
   * protostuff 序列化
   */
  PROTOSTUFF_SERIAL((byte) 3),

  /**
   * hessian 序列化  dubbo 默认采用 hessian2 序列化
   */
  HESSIAN_SERIAL((byte) 4);

  private final byte code;

  SerializerType(byte code) {
    this.code = code;
  }

  public byte code() {
    return this.code;
  }
}
