package com.cheche.service;

import com.cheche.annotations.RpcService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fudy
 * @date 2023/2/16
 */
@Service
@RpcService(PersonService.class)
public class PersonServiceImpl implements PersonService {

  @Override
  public List<Person> callPerson(String name, Integer num) {
    List<Person> persons = new ArrayList<>(num);
    for (int i = 0; i < num; ++i) {
      persons.add(new Person(Integer.toString(i), name));
    }
    return persons;
  }
}
