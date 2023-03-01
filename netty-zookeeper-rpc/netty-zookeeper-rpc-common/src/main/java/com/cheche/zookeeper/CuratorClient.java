package com.cheche.zookeeper;

import com.cheche.constants.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * zookeeper client
 *
 * @author fudy
 * @date 2023/2/15
 */
public class CuratorClient {

  private CuratorFramework client;

  public CuratorClient(String connectString, String namespace,
                       int sessionTimeout, int connectionTimeout) {
    client = CuratorFrameworkFactory.builder()
      .namespace(namespace)
      .connectString(connectString)
      .sessionTimeoutMs(sessionTimeout)
      .connectionTimeoutMs(connectionTimeout)
      .retryPolicy(new ExponentialBackoffRetry(1000, 0))
      .build();
    client.start();
  }

  public CuratorClient(String connectString, int timeout) {
    this(connectString, Constant.ZK_NAMESPACE, timeout, timeout);
  }

  public CuratorClient(String connectString) {
    this(connectString, Constant.ZK_NAMESPACE, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
  }

  public CuratorFramework getClient() {
    return client;
  }

  /**
   * add connectionStateListener
   *
   * @param connectionStateListener connectionStateListener
   */
  public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
    client.getConnectionStateListenable().addListener(connectionStateListener);
  }

  /**
   * create path data
   *
   * @param path path
   * @param data data
   * @return String
   * @throws Exception exception
   */
  public String createPathData(String path, byte[] data) throws Exception {
    return client.create().creatingParentsIfNeeded()
      .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
      .forPath(path, data);
  }

  /**
   * update path data
   *
   * @param path path
   * @param data data
   * @throws Exception exception
   */
  public void updatePathData(String path, byte[] data) throws Exception {
    client.setData().forPath(path, data);
  }

  /**
   * delete path data
   *
   * @param path path
   * @throws Exception exception
   */
  public void deletePathData(String path) throws Exception {
    client.delete().forPath(path);
  }

  /**
   * watch node
   *
   * @param path path
   * @param watcher watcher
   */
  public void watchNode(String path, Watcher watcher) throws Exception {
    client.getData().usingWatcher(watcher).forPath(path);
  }

  /**
   * get data
   *
   * @param path path
   * @return byte[]
   * @throws Exception exception
   */
  public byte[] getData(String path) throws Exception {
    return client.getData().forPath(path);
  }

  /**
   * get children
   *
   * @param path path
   * @return List<String>
   * @throws Exception exception
   */
  public List<String> getChildren(String path) throws Exception {
    return client.getChildren().forPath(path);
  }

  /**
   * watch treeNode
   *
   * @param path path
   * @param listener listener
   */
  public void watchTreeNode(String path, TreeCacheListener listener) {
    TreeCache treeCache = new TreeCache(client, path);
    treeCache.getListenable().addListener(listener);
  }

  public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
    PathChildrenCache parentCache = new PathChildrenCache(client, path, true);
    //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
    parentCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    parentCache.getListenable().addListener(listener);
  }

  public void close() {
    client.close();
  }

}
