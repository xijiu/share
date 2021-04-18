package org.xijiu.share.aqs;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/2/4 上午11:54
 */
public class ConditionCompareTest {
  private ReentrantLock lock = new ReentrantLock();
  private Condition condition = lock.newCondition();

  private int threadNum = 10;

  @Test
  public void runTest() throws InterruptedException {
    long begin = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      if (i % 1000 == 0) {
        System.out.println(i);
      }
      test();
    }
    long cost = System.currentTimeMillis() - begin;
    System.out.println("耗时： " + cost);
  }

  @Test
  public void test() throws InterruptedException {
    AtomicInteger lockedNum = new AtomicInteger();
    List<Thread> list = Lists.newArrayList();
    for (int i = 0; i < threadNum; i++) {
      Thread thread = new Thread(() -> {
        try {
          lock.lock();
          lockedNum.incrementAndGet();
          condition.await();
          lock.unlock();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      thread.start();
      list.add(thread);
    }

    while (true) {
      if (lockedNum.get() != threadNum) {
        continue;
      }
      boolean allWaiting = true;
      for (Thread thread : list) {
        if (thread.getState() != Thread.State.WAITING) {
          allWaiting = false;
          break;
        }
      }
      if (allWaiting) {
        break;
      }
    }

    lock.lock();
    condition.signalAll();
    lock.unlock();

    for (Thread thread : list) {
      thread.join();
    }

  }

  @Test
  public void test2() throws InterruptedException {
    Object lock = new Object();
    List<Thread> list = Lists.newArrayList();
    for (int i = 0; i < threadNum; i++) {
      Thread thread = new Thread(() -> {
        try {
          synchronized (lock) {
            lock.wait();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      thread.start();
      list.add(thread);
    }

    while (true) {
      boolean allWaiting = true;
      for (Thread thread : list) {
        if (thread.getState() != Thread.State.WAITING) {
          allWaiting = false;
          break;
        }
      }
      if (allWaiting) {
        break;
      }
    }

    synchronized (lock) {
      lock.notifyAll();
    }

    for (Thread thread : list) {
      thread.join();
    }
  }

}
