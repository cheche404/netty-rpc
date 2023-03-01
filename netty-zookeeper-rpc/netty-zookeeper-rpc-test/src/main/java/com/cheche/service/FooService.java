package com.cheche.service;

import com.cheche.annotations.RpcAutowired;
import org.springframework.stereotype.Service;

/**
 * @author fudy
 * @date 2023/2/16
 */
@Service
public class FooService implements Foo {

  @RpcAutowired(version = "1.0")
  private HelloService helloService1;

  @RpcAutowired(version = "2.0")
  private HelloService helloService2;

  @Override
  public String say(String s) {
    return helloService1.hello(s);
  }
}
