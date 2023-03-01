package com.cheche.serializer.kryo;

import com.cheche.codec.RpcRequest;
import com.cheche.codec.RpcResponse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * kryo 序列化 工厂 pool
 *
 * @author fudy
 * @date 2023/2/15
 */
public class KryoPoolFactory {

  private static volatile KryoPoolFactory poolFactory = null;

  private KryoPoolFactory() {}

  private KryoFactory factory = () -> {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.register(RpcRequest.class);
    kryo.register(RpcResponse.class);
    Kryo.DefaultInstantiatorStrategy strategy = (Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy();
    strategy.setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
    return kryo;
  };

  private final KryoPool pool = new KryoPool.Builder(factory).build();

  public static KryoPool getKryoPoolInstance() {
      if (poolFactory == null) {
        synchronized (KryoPoolFactory.class) {
            if (poolFactory == null) {
              poolFactory = new KryoPoolFactory();
            }
        }
      }
      return poolFactory.getPool();
  }

  public KryoPool getPool() {
      return pool;
  }

}
