package com.cheche.route.impl;

import com.cheche.handler.RpcClientHandler;
import com.cheche.protocol.RpcProtocol;
import com.cheche.route.RpcLoadBalance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round robin load balance
 *
 * @author fudy
 * @date 2023/2/16
 */
public class RpcLoadBalanceRoundRobin extends RpcLoadBalance {
  private AtomicInteger roundRobin = new AtomicInteger(0);

  public RpcProtocol doRoute(List<RpcProtocol> addressList) {
    int size = addressList.size();
    // Round robin
    int index = (roundRobin.getAndAdd(1) + size) % size;
    return addressList.get(index);
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
