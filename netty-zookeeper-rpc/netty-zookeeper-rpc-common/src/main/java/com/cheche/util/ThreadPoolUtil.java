package com.cheche.util;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * thread pool util
 *
 * @author fudy
 * @date 2023/2/16
 */
public class ThreadPoolUtil {

  /**
   * create thread pool
   *
   * @param name name
   * @param corePoolSize core pool size
   * @param maxPoolSize max pool size
   * @return ThreadPoolExecutor
   */
  public static ThreadPoolExecutor createThreadPool(final String name,
                                                    int corePoolSize,
                                                    int maxPoolSize) {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
      corePoolSize,
      maxPoolSize,
      60L,
      TimeUnit.SECONDS,
      new LinkedBlockingQueue<>(1000),
      r -> new Thread(r, StringUtils.join("netty-rpc-", name, "-", r.hashCode())),
      new ThreadPoolExecutor.AbortPolicy()
    );
    return threadPoolExecutor;
  }

}
