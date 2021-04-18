package org.xijiu.share.aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author likangning
 * @since 2021/3/26 上午8:54
 */
public class MyReadWriteLock {

  private volatile AtomicInteger writeThreadNum = new AtomicInteger();

  private volatile int myState = 0;

  private class ReadSync extends AbstractQueuedSynchronizer {

    private ReadSync(int permits) {
      setState(permits);
    }

    @Override
    protected int tryAcquireShared(int acquires) {
      // 如果写入队列中已经有值，那么将读线程挂起
      if (writeThreadNum.get() > 0) {
        return -1;
      }
      int state = myState;
      if (state >= 0 && compareAndSetMyState(state, state + acquires)) {
        return state;
      } else {
        // 多个读线程在抢资源，导致cas失败
        return tryAcquireShared(acquires);
      }
    }

    @Override
    protected final boolean tryReleaseShared(int releases) {
      // 被写线程唤起
      if (releases == 0) {
        return true;
      }
      int rest;
      while (true) {
        int state = myState;
        if (compareAndSetMyState(state, (rest = state - releases))) {
          break;
        }
      }
      // 唤醒写线程
      if (writeThreadNum.get() > 0 && rest == 0) {
        writeSync.release(0);
      }
      return false;
    }
  }


  private class WriteSync extends AbstractQueuedSynchronizer {

    private WriteSync(int permits) {
      setState(permits);
    }

    @Override
    protected boolean tryAcquire(int acquires) {
      int state = myState;
      if (state == 0 && compareAndSetMyState(0, -1)) {
        return true;
      }
      return false;
    }

    @Override
    protected final boolean tryRelease(int releases) {
      // 被读线程唤醒，仅做unpark操作即可
      if (releases == 0) {
        return true;
      }
      myState = 0;
      System.out.println("write 将state设置为0 ： " + myState);
      if (writeThreadNum.get() == 1) {
        // 所有的写线程执行完毕后，需要唤醒读线程
        readSync.releaseShared(0);
      }
      return true;
    }
  }

  private ReadSync readSync;
  private WriteSync writeSync;

  public MyReadWriteLock() {
    readSync = new ReadSync(0);
    writeSync = new WriteSync(0);
  }

  public void readLock() {
    readSync.acquireShared(1);
  }

  public void unlockRead() {
    readSync.releaseShared(1);
  }

  public void writeLock() {
    writeThreadNum.incrementAndGet();
    writeSync.acquire(1);
  }

  public void unlockWrite() {
    writeSync.release(1);
    writeThreadNum.decrementAndGet();
  }


  private static Unsafe unsafe;

  private static final long myStateOffset;

  static {
    try {
      //1.最简单的使用方式是基于反射获取Unsafe实例
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      unsafe = (Unsafe) f.get(null);
      myStateOffset = unsafe.objectFieldOffset
          (MyReadWriteLock.class.getDeclaredField("myState"));
    } catch (Exception ex) { throw new Error(ex); }
  }

  private boolean compareAndSetMyState(int expect, int update) {
    // See below for intrinsics setup to support this
    return unsafe.compareAndSwapInt(this, myStateOffset, expect, update);
  }

}
