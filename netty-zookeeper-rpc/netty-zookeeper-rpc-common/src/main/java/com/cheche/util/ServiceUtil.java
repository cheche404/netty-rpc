package com.cheche.util;

import org.apache.commons.lang3.StringUtils;

/**
 * service util
 *
 * @author fudy
 * @date 2023/2/15
 */
public class ServiceUtil {

  public static final String SERVICE_CONCAT_TOKEN = "#";

  /**
   * make service key value
   *
   * @param interfaceName interface name
   * @param version version
   * @return service key interfaceName#version
   */
  public static String makeServiceKey(String interfaceName, String version) {
    String serviceKey = interfaceName;
    if (StringUtils.isNotBlank(version) && version.trim().length() > 0) {
      serviceKey += SERVICE_CONCAT_TOKEN.concat(version);
    }
    return serviceKey;
  }

}
