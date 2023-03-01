package com.cheche;

import com.cheche.core.RpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * rpc server application
 *
 * @author fudy
 * @date 2023/2/15
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RpcServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(RpcServerApplication.class, args);
    // start rpc server
    new RpcServer("127.0.0.1", 9999).start();
  }

}
