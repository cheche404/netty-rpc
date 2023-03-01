package com.cheche.domain;

import com.cheche.protocol.RpcProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求处理类
 *
 * @author fudy
 * @date 2023/2/15
 */
public class RequestHolder {

  /**
   * 原子性 request_id 请求 ID
   */
  public static final AtomicLong REQUEST_ID = new AtomicLong();;

  /**
   * 保存请求ID与返回数据的关系
   */
  public static final Map<Long, RpcFuture> REQUEST_MAP = new ConcurrentHashMap<>();

}
