package org.xijiu.share.aqs;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/2/4 上午11:54
 */
public class ConditionCompareTest {
  private ReentrantLock lock = new ReentrantLock();
  private Condition condition = lock.newCondition();

  @Test
  public void runTest() throws InterruptedException {
    long begin = System.currentTimeMillis();
    for (int i = 0; i < 10000; i++) {
      if (i % 1000 == 0) {
        System.out.println(i);
      }
      aqsTest();
    }
    long cost = System.currentTimeMillis() - begin;
    System.out.println("耗时： " + cost);
  }

  @Test
  public void aqsTest() throws InterruptedException {
    AtomicInteger lockedNum = new AtomicInteger();
    List<Thread> list = Lists.newArrayList();
    // 步骤一：启动10个线程，并进入wait等待
    for (int i = 0; i < 10; i++) {
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

    // 步骤二：等待10个线程全部进入wait方法
    while (true) {
      if (lockedNum.get() != 10) {
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

    // 步骤三：唤醒10个线程
    lock.lock();
    condition.signalAll();
    lock.unlock();

    // 步骤四：等待10个线程全部执行完毕
    for (Thread thread : list) {
      thread.join();
    }

  }

  @Test
  public void jdkTest() throws InterruptedException {
    Object lock = new Object();
    List<Thread> list = Lists.newArrayList();
    // 步骤一：启动10个线程，并进入wait等待
    for (int i = 0; i < 10; i++) {
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

    // 步骤二：等待10个线程全部进入wait方法
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

    // 步骤三：唤醒10个线程
    synchronized (lock) {
      lock.notifyAll();
    }

    // 步骤四：等待10个线程全部执行完毕
    for (Thread thread : list) {
      thread.join();
    }
  }

}
