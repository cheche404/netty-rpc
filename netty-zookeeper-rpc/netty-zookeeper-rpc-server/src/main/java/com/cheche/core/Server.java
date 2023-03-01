package com.cheche.core;

/**
 * @author fudy
 * @date 2023/2/15
 */
public abstract class Server {

  /**
   * start server
   *
   * @throws Exception exception
   */
  public abstract void start() throws Exception;

  /**
   * stop server
   *
   * @throws Exception exception
   */
  public abstract void stop() throws Exception;


}
