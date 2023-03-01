package com.cheche.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息头
 *
 * @author fudy
 * @date 2023/2/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header implements Serializable {

  /**
   * 魔数 2个字节
   * Magic number，即幻数，它可以用来标记文件或者协议的格式，很多文件都有幻数标志来表明该文件的格式
   * 例如Windows操作系统可执行程序的开头标记一般为MZ，这是一种源于磁盘操作系统时代的格式
   */
  private short magic;

  /**
   * 序列化类型  1个字节
   */
  private byte serialType;

  /**
   * 消息类型 1个字节
   */
  private byte reqType;

  /**
   * 请求ID 8个字节
   */
  private long requestId;

  /**
   * 消息体长度 4个字节
   */
  private int length;

}
