package com.cheche.service;

import java.util.List;

/**
 * @author fudy
 * @date 2023/2/16
 */
public interface PersonService {
  List<Person> callPerson(String name, Integer num);
}
