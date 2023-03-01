package com.cheche.service;

import java.util.Map;

/**
 * @author fudy
 * @date 2023/2/15
 */
public interface UserService {

  /**
   * 根据 ID 获取 用户信息
   *
   * @param id
   * @return
   */
  Map<String, String> getUserInfoById(Integer id);

}
