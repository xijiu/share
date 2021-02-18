package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * @author likangning
 * @since 2021/2/2 上午9:54
 */
public class ParkAndUnParkTest {

  @Test
  public void test() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      long sum = 0;
      long begin = System.currentTimeMillis();
      while (true) {
        sum = sum + 1;
        if (System.currentTimeMillis() - begin > 1000) {
          System.out.println("线程 1 准备挂起");
          LockSupport.park(this);
          System.out.println("线程 1 唤醒");

        }
      }
    });

    Thread thread2 = new Thread(() -> {
      long sum = 0;
      long begin = System.currentTimeMillis();
      while (true) {
        sum = sum + 2;
        if (System.currentTimeMillis() - begin > 1000) {
          System.out.println("线程 2 准备挂起");
          LockSupport.park(this);
          System.out.println("线程 2 唤醒");
        }
      }
    });

    thread1.start();
    thread2.start();

    Thread.sleep(10000);
    System.out.println("准备对 两 个线程执行打断");
//    LockSupport.park(this);
//    System.out.println(1);

    thread1.interrupt();
    thread2.interrupt();

    Thread.sleep(1000);
    System.out.println("over");
  }
}
