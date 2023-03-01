package com.cheche.codec;

/**
 * 心跳
 *
 * @author fudy
 * @date 2023/2/15
 */
public final class Beat {

  /**
   * 心跳间隔
   */
  public static final int BEAT_INTERVAL = 30;

  /**
   * 心跳超时
   */
  public static final int BEAT_TIMEOUT = 3 * BEAT_INTERVAL;

  public static final String BEAT_ID = "BEAT_PING_PONG";

  public static RpcRequest BEAT_PING;

  static {
    BEAT_PING = new RpcRequest();
    BEAT_PING.setRequestId(BEAT_ID);
  }

}
