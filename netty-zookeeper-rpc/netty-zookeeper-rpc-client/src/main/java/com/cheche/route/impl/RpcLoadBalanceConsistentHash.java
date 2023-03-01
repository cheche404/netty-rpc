package com.cheche.route.impl;

import com.cheche.handler.RpcClientHandler;
import com.cheche.protocol.RpcProtocol;
import com.cheche.route.RpcLoadBalance;
import com.google.common.hash.Hashing;

import java.util.List;
import java.util.Map;

/**
 * @author fudy
 * @date 2023/2/16
 */
public class RpcLoadBalanceConsistentHash extends RpcLoadBalance {

  public RpcProtocol doRoute(String serviceKey, List<RpcProtocol> addressList) {
    int index = Hashing.consistentHash(serviceKey.hashCode(), addressList.size());
    return addressList.get(index);
  }

  @Override
  public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
    Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
    List<RpcProtocol> addressList = serviceMap.get(serviceKey);
    if (addressList != null && addressList.size() > 0) {
      return doRoute(serviceKey, addressList);
    } else {
      throw new Exception("Can not find connection for service: " + serviceKey);
    }
  }
}
