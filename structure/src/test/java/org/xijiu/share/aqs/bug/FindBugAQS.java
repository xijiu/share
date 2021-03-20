package org.xijiu.share.aqs.bug;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author likangning
 * @since 2021/1/22 上午10:09
 */
public class FindBugAQS {

  private Sync sync = new Sync();

  private static class Sync extends AbstractQueuedSynchronizer {

    private Sync() {
      setState(1);
    }

    public void lock() {
      int state = getState();
      if (state == 1 && compareAndSetState(state, 0)) {
        return;
      }
      acquire(1);
    }

    @Override
    protected boolean tryAcquire(int acquires) {
      int state = getState();
      if (state == 1 && compareAndSetState(state, 0)) {
        return true;
      }
      return false;
    }

    @Override
    protected final boolean tryRelease(int releases) {
      setState(1);
      return true;
    }

    public void unlock() {
      release(1);
    }
  }

  public void lock() {
    sync.lock();
  }

  public void unlock() {
    sync.unlock();
  }

}
