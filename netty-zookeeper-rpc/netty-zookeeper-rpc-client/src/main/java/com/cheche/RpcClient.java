package com.cheche;

import com.cheche.annotations.RpcAutowired;
import com.cheche.connect.ConnectionManager;
import com.cheche.discovery.ServiceDiscovery;
import com.cheche.proxy.ObjectProxy;
import com.cheche.proxy.RpcService;
import com.cheche.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * rpc client
 *
 * @author fudy
 * @date 2023/2/16
 */
@Slf4j
public class RpcClient implements ApplicationContextAware, DisposableBean {

  private ServiceDiscovery serviceDiscovery;

  private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.createThreadPool(RpcClient.class.getSimpleName(), 8, 16);

  public RpcClient(String address) {
    this.serviceDiscovery = new ServiceDiscovery(address);
  }

  @SuppressWarnings("unchecked")
  public static <T, P> T createService(Class<T> interfaceClass, String version) {
    return (T) Proxy.newProxyInstance(
      interfaceClass.getClassLoader(),
      new Class<?>[]{interfaceClass},
      new ObjectProxy<T, P>(interfaceClass, version)
    );
  }

  public static <T, P> RpcService createAsyncService(Class<T> interfaceClass, String version) {
    return new ObjectProxy<T, P>(interfaceClass, version);
  }

  public static void submit(Runnable task) {
    threadPoolExecutor.submit(task);
  }

  public void stop() {
    threadPoolExecutor.shutdown();
    serviceDiscovery.stop();
    ConnectionManager.getInstance().stop();
  }

  @Override
  public void destroy() throws Exception {
    this.stop();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    for (String beanName : beanNames) {
      Object bean = applicationContext.getBean(beanName);
      Field[] fields = bean.getClass().getDeclaredFields();
      try {
        for (Field field : fields) {
          RpcAutowired rpcAutowired = field.getAnnotation(RpcAutowired.class);
          if (rpcAutowired != null) {
            String version = rpcAutowired.version();
            field.setAccessible(true);
            field.set(bean, createService(field.getType(), version));
          }
        }
      } catch (IllegalAccessException e) {
        log.error(e.toString());
      }
    }
  }
}
