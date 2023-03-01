package com.cheche.constants;

/**
 * zk constant
 *
 * @author fudy
 * @date 2023/2/15
 */
public interface Constant {

  /**
   * zk session timeout (ms)
   */
  int ZK_SESSION_TIMEOUT = 5000;

  /**
   * zk connection timeout (ms)
   */
  int ZK_CONNECTION_TIMEOUT = 5000;

  /**
   * zk registry path
   */
  String ZK_REGISTRY_PATH = "/registry";

  String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

  /**
   * zk namespace
   */
  String ZK_NAMESPACE = "netty-rpc";

}
