package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/1/5 上午11:30
 */
public class AQSTest {

  @Test
  public void test() throws InterruptedException {
    Semaphore semaphore = new Semaphore(10);
    semaphore.acquire();
    System.out.println("已经抢到锁并执行");
    semaphore.release();


    ReentrantLock reentrantLock = new ReentrantLock();
    reentrantLock.lock();
    reentrantLock.unlock();


//    System.out.println(Thread.interrupted());
//    Thread.currentThread().interrupt();
//    System.out.println(Thread.interrupted());
//    System.out.println(Thread.interrupted());
//
//    ReentrantLock reentrantLock = new ReentrantLock();
//    reentrantLock.lock();
  }

  @Test
  public void test2() throws InterruptedException {
    final Object obj = new Object();

    Thread thread1 = new Thread(() -> {
      synchronized (obj) {
        try {
          System.out.println("线程A获取到锁");
          obj.wait(2000);
          System.out.println("线程A结束");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread thread2 = new Thread(() -> {
      synchronized (obj) {
        try {
          System.out.println("线程B获取到锁");
          obj.notify();
          System.out.println("线程B结束");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

  }


  @Test
  public void test3() throws InterruptedException {
    StringBuilder sb = new StringBuilder();
    int begin = 1;
    int end = 2001;
    for (int i = begin; i < end; i++) {
      sb.append(i).append(",");
    }
    System.out.println(sb.toString());
  }



}
