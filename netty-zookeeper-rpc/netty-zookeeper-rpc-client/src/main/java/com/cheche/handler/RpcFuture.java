package com.cheche.handler;

import com.cheche.RpcClient;
import com.cheche.codec.RpcRequest;
import com.cheche.codec.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RPCFuture for async RPC call
 *
 * @author fudy
 * @date 2023/2/16
 */
@Slf4j
public class RpcFuture implements Future<Object> {

  private Sync sync;
  private RpcRequest request;
  private RpcResponse response;
  private long startTime;
  /**
   * Threshold: 阈值
   * response time threshold
   */
  private long responseTimeThreshold = 5000;
  private List<AsyncRPCCallback> pendingCallbacks = new ArrayList<>();
  private ReentrantLock lock = new ReentrantLock();

  public RpcFuture(RpcRequest request) {
    this.sync = new Sync();
    this.request = request;
    this.startTime = System.currentTimeMillis();
  }

  @Override
  public boolean isDone() {
    return sync.isDone();
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isCancelled() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object get() {
    sync.acquire(1);
    if (Objects.nonNull(this.response)) {
      return this.response.getResult();
    }
    return null;
  }

  @Override
  public Object get(long timeout, TimeUnit unit) throws InterruptedException {
    boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
    if (success) {
      if (Objects.nonNull(this.response)) {
        return this.response.getResult();
      }
      return null;
    } else {
      throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
        + ". Request class name: " + this.request.getClassName()
        + ". Request method: " + this.request.getMethodName());
    }
  }

  public void done(RpcResponse response) {
    this.response = response;
    sync.release(1);
    // 回调
    invokeCallbacks();
    long responseTime = System.currentTimeMillis() - startTime;
    if (responseTime > this.responseTimeThreshold) {
      log.warn("Service response time is too slow. Request id = " +
        response.getRequestId() + ". Response Time = " + responseTime + "ms");
    }
  }

  private void invokeCallbacks() {
    lock.lock();
    try {
        for(AsyncRPCCallback callback : this.pendingCallbacks) {
          runCallback(callback);
        }
    } finally {
      lock.unlock();
    }
  }

  public RpcFuture addCallback(AsyncRPCCallback callback) {
    lock.lock();
    try {
        if (isDone()) {
          runCallback(callback);
        } else {
          this.pendingCallbacks.add(callback);
        }
    } finally {
        lock.unlock();
    }
    return this;
  }

  private void runCallback(final AsyncRPCCallback callback) {
    final RpcResponse response = this.response;
    RpcClient.submit(() -> {
      if (!response.isError()) {
        callback.success(response.getResult());
      } else {
        callback.fail(new RuntimeException("Response error", new Throwable(response.getError())));
      }
    });
  }

  /**
   * RpcFuture Sync
   */
  static class Sync extends AbstractQueuedSynchronizer {

    private final int done = 1;
    private final int pending = 0;

    @Override
    protected boolean tryAcquire(int arg) {
      return getState() == done;
    }

    @Override
    protected boolean tryRelease(int arg) {
      if (getState() == pending) {
        if (compareAndSetState(pending, done)) {
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    }

    protected boolean isDone() {
      return getState() == done;
    }
  }

}
