package com.cheche.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author fudy
 * @date 2023/2/16
 */
public class RpcServerBootstrap {
  public static void main(String[] args) {
    new ClassPathXmlApplicationContext("server-spring.xml");
  }
}
