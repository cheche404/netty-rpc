package com.cheche;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author fudy
 * @date 2023/2/15
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RpcClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(RpcClientApplication.class, args);
  }

}
