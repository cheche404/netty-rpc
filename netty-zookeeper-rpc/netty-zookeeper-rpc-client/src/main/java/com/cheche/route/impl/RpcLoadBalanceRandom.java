package com.cheche.route.impl;

import com.cheche.handler.RpcClientHandler;
import com.cheche.protocol.RpcProtocol;
import com.cheche.route.RpcLoadBalance;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author fudy
 * @date 2023/2/16
 */
public class RpcLoadBalanceRandom extends RpcLoadBalance {
  private Random random = new Random();

  public RpcProtocol doRoute(List<RpcProtocol> addressList) {
    int size = addressList.size();
    // Random
    return addressList.get(random.nextInt(size));
  }

  @Override
  public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
    Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
    List<RpcProtocol> addressList = serviceMap.get(serviceKey);
    if (addressList != null && addressList.size() > 0) {
      return doRoute(addressList);
    } else {
      throw new Exception("Can not find connection for service: " + serviceKey);
    }
  }
}
