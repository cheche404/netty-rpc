package com.cheche;

import com.cheche.annotations.RpcService;
import com.cheche.core.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * rpc server
 *
 * @author fudy
 * @date 2023/2/16
 */
public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {

  public RpcServer(String serverAddress, String registryAddress) {
    super(serverAddress, registryAddress);
  }

  @Override
  public void destroy() throws Exception {
    super.stop();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    super.start();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
    if (MapUtils.isNotEmpty(serviceMap)) {
      for(Object serviceBean: serviceMap.values()) {
        RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
        String interfaceName = rpcService.value().getName();
        String version = rpcService.version();
        super.addService(interfaceName, version, serviceBean);
      }
    }
  }
}
