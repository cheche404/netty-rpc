package com.cheche.service;

/**
 * @author fudy
 * @date 2023/2/16
 */
public interface HelloService {
  String hello(String name);

  String hello(Person person);

  String hello(String name, Integer age);
}
