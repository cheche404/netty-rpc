package com.cheche.registry;

import com.cheche.constants.Constant;
import com.cheche.protocol.RpcProtocol;
import com.cheche.protocol.RpcServiceInfo;
import com.cheche.util.ServiceUtil;
import com.cheche.zookeeper.CuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务注册类
 *
 * @author fudy
 * @date 2023/2/15
 */
@Slf4j
public class ServiceRegistry {

  private CuratorClient curatorClient;

  private List<String> pathList = new ArrayList<>();

  public ServiceRegistry(String registryAddress) {
    this.curatorClient = new CuratorClient(registryAddress, 5000);
  }

  /**
   * 注册服务
   *
   * @param host host
   * @param port port
   * @param serviceMap serviceMap
   */
  public void registerService(String host, int port, Map<String, Object> serviceMap) {
    // 注册服务详情
    List<RpcServiceInfo> serviceInfoList = new ArrayList<>();
    for(String key : serviceMap.keySet()) {
      String[] serviceInfo = key.split(ServiceUtil.SERVICE_CONCAT_TOKEN);
      if (serviceInfo.length > 0) {
        RpcServiceInfo rpcServiceInfo = new RpcServiceInfo();
        rpcServiceInfo.setServiceName(serviceInfo[0]);
        if (serviceInfo.length == 2) {
          rpcServiceInfo.setVersion(serviceInfo[1]);
        } else {
          rpcServiceInfo.setVersion(Strings.EMPTY);
        }
        log.info("register new service: [{}]", key);
        serviceInfoList.add(rpcServiceInfo);
      } else {
        log.warn("Can not get service name and version: [{}]", key);
      }
    }

    try {
      RpcProtocol rpcProtocol = new RpcProtocol();
      rpcProtocol.setServiceInfoList(serviceInfoList);
      rpcProtocol.setHost(host);
      rpcProtocol.setPort(port);
      String serviceData = rpcProtocol.toJson();
      byte[] bytes = serviceData.getBytes();
      String path = StringUtils.join(Constant.ZK_DATA_PATH, "-", rpcProtocol.hashCode());
      path = this.curatorClient.createPathData(path, bytes);
      pathList.add(path);
      log.info("Register [{}] new service, host: [{}], port: [{}]", serviceInfoList.size(), host, port);
    } catch (Exception e) {
      log.error("Register service fail, exception: [{}]", e.getMessage());
    }

    curatorClient.addConnectionStateListener((curatorFramework, connectionState) -> {
      if (connectionState == ConnectionState.RECONNECTED) {
        log.info("Connection state: [{}], register service after reconnected", connectionState);
        registerService(host, port, serviceMap);
      }
    });

  }

  public void unregisterService() {
    log.info("Unregister all service");
    for (String path : pathList) {
      try {
        this.curatorClient.deletePathData(path);
      } catch (Exception ex) {
        log.error("Delete service path error: " + ex.getMessage());
      }
    }
    this.curatorClient.close();
  }

}
