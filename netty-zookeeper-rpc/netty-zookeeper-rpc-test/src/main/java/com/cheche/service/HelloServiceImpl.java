package com.cheche.service;

import com.cheche.annotations.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author fudy
 * @date 2023/2/16
 */
@Service
@RpcService(value = HelloService.class, version = "1.0")
public class HelloServiceImpl implements HelloService {

  public HelloServiceImpl() {
  }

  @Override
  public String hello(String name) {
    return "Hello " + name;
  }

  @Override
  public String hello(Person person) {
    return "Hello " + person.getFirstName() + " " + person.getLastName();
  }

  @Override
  public String hello(String name, Integer age) {
    return name + " is " + age;
  }
}
