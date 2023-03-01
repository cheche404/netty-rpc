package com.cheche.protocol;

import com.cheche.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * RpcServiceInfo
 *
 * @author fudy
 * @date 2023/2/15
 */
@Getter
@Setter
public class RpcServiceInfo implements Serializable {

  private String serviceName;
  private String version;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RpcServiceInfo)) {
      return false;
    }
    RpcServiceInfo that = (RpcServiceInfo) o;
    return Objects.equals(serviceName, that.serviceName) && Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceName, version);
  }

  @Override
  public String toString() {
    return JsonUtil.objectToJson(this);
  }
}
