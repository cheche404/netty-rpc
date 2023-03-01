package com.cheche.service.impl;

import com.cheche.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fudy
 * @date 2023/2/15
 */
@Service
public class UserServiceImpl implements UserService {
  @Override
  public Map<String, String> getUserInfoById(Integer id) {
    Map<Integer, Map<String, String>> resultMap = new HashMap<>();
    Map<String, String> map = new HashMap<>();
    map.put("id", "1");
    map.put("name", "cheche");
    resultMap.put(1, map);
    return resultMap.get(id);
  }
}
