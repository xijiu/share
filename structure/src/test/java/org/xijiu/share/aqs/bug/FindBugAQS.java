package org.xijiu.share.aqs.bug;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author likangning
 * @since 2021/1/22 上午10:09
 */
public class FindBugAQS {

  public volatile static int FLAG = 0;

  private static ThreadLocal<Integer> FLAG_STORE = new ThreadLocal<>();

  private static ThreadLocal<Integer> TIMES = ThreadLocal.withInitial(() -> 0);

  private Sync sync = new Sync();

  private static class Sync extends AbstractQueuedSynchronizer {

    private Sync() {
      setState(1);
    }

    public void lock() {
      FLAG_STORE.set(++FLAG);
      int state = getState();
      if (state == 1 && compareAndSetState(state, 0)) {
        return;
      }
      acquire(1);
    }

    @Override
    protected boolean tryAcquire(int acquires) {
      if (FLAG_STORE.get() == 2) {
        Integer time = TIMES.get();
        if (time == 0) {
          TIMES.set(1);
        } else {
          // 模拟发生异常，第二个节点在第二次访问tryAcquire方法时，将会扔出运行期异常
          System.out.println("发生异常");
          throw new RuntimeException("lkn aqs bug");
        }
      }
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