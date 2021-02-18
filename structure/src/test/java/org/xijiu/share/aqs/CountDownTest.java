package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/1/22 上午10:26
 */
public class CountDownTest {

  @Test
  public void test2() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    Thread thread2 = new Thread(() -> {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread2.start();

    countDownLatch.countDown();

    thread2.join();

    LockSupport.park();
    LockSupport.unpark(Thread.currentThread());

  }

  @Test
  public void test() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(-3);

    Thread thread1 = new Thread(() -> {
      try {
        System.out.println("【线程1】开始await");
        countDownLatch.await();
        System.out.println("【线程1】结束await");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread1.start();

    Thread thread2 = new Thread(() -> {
      try {
        System.out.println("【线程2】开始await");
        countDownLatch.await();
        System.out.println("【线程2】结束await");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread2.start();

    Thread.sleep(1000);
    countDownLatch.countDown();
    countDownLatch.countDown();
    countDownLatch.countDown();




    thread1.join();
    thread2.join();

    System.out.println("【当前】开始await");
    countDownLatch.await();
    System.out.println("【当前】结束await");

  }
}
