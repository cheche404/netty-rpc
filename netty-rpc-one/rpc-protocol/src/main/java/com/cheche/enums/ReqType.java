package com.cheche.enums;

/**
 * 请求类型 枚举
 *
 * @author fudy
 * @date 2023/2/15
 */
public enum ReqType {

  /**
   * request 请求
   */
  REQUEST((byte) 1),

  /**
   * response 请求相应
   */
  RESPONSE((byte) 2),

  /**
   * 心跳
   */
  HEARTBEAT((byte) 3),

  /**
   * 仅连接
   */
  CONNECT((byte) 4)
  ;


  private final byte code;

  ReqType(byte code) {
    this.code = code;
  }

  public byte code() {
    return this.code;
  }

  public static ReqType findByCode(byte code) {
    for (ReqType value : ReqType.values()) {
      if (value.code == code) {
        return value;
      }
    }
    return null;
  }

}
