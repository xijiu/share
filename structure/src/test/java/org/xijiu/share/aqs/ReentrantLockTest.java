package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/1/25 下午5:22
 */
public class ReentrantLockTest {

  private ReentrantLock reentrantLock = new ReentrantLock();


  @Test
  public void test() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      System.out.println("线程1准备加锁");
      reentrantLock.lock();
      System.out.println("线程1加锁成功，将睡眠1秒钟");
      PubTools.sleep(1000);
      reentrantLock.unlock();
      System.out.println("线程1结束，释放锁");
    });
    thread1.start();


    System.out.println("线程test准备加锁");
    reentrantLock.lock();
    System.out.println("线程test加锁成功，将睡眠2秒钟");
    PubTools.sleep(1000);
    reentrantLock.unlock();
    System.out.println("线程test结束，释放锁");

    thread1.join();
  }

  @Test
  public void test2() throws Exception {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    condition.await();
    condition.signalAll();

    Object obj = new Object();
    obj.wait();
  }
}
