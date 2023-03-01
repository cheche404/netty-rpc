package com.cheche;

import com.cheche.proxy.RpcClientProxy;
import com.cheche.service.UserService;

/**
 * @author fudy
 * @date 2023/2/15
 */
public class RpcClientTest {

  public static void main(String[] args) {
    RpcClientProxy rpcClientProxy = new RpcClientProxy();
    UserService userService = rpcClientProxy.clientProxy(UserService.class, "127.0.0.1", 9999);
    System.out.println(userService.getUserInfoById(1));
  }

}
