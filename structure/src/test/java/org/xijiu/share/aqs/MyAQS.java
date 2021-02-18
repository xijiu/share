package org.xijiu.share.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author likangning
 * @since 2021/1/22 上午10:09
 */
public class MyAQS {

  private Sync sync;

  private static class Sync extends AbstractQueuedSynchronizer {

    private Sync(int permits) {
      setState(permits);
    }

    @Override
    protected int tryAcquireShared(int acquires) {
      for (;;) {
        int available = getState();
        int remaining = available - acquires;
        if (remaining < 0 ||
            compareAndSetState(available, remaining))
          return remaining;
      }
    }

    @Override
    protected final boolean tryReleaseShared(int releases) {
      for (;;) {
        int current = getState();
        int next = current + releases;
        if (next < current)
          throw new Error("Maximum permit count exceeded");
        if (compareAndSetState(current, next))
          return true;
      }
    }
  }

  public MyAQS(int permits) {
    sync = new Sync(permits);
  }

  public void require() {
    sync.acquireShared(1);
  }

  public void release() {
    sync.releaseShared(1);
  }

}
