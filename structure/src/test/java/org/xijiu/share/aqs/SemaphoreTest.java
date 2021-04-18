package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author likangning
 * @since 2021/1/25 下午8:18
 */
public class SemaphoreTest {

  @Test
  public void test() throws InterruptedException {
    Semaphore semaphore = new Semaphore(10);
    semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
    semaphore.acquire();
    semaphore.acquireUninterruptibly();
    semaphore.tryAcquire(55555, TimeUnit.MILLISECONDS);
    semaphore.release();
  }

  @Test
  public void test2() {
    MyAQS myAQS = new MyAQS(10);
    for (int i = 0; i < 11; i++) {
      if (i == 10) {
        myAQS.release();
      }
      myAQS.require();
    }
    System.out.println("over");
  }


  @Test
  public void test3() throws InterruptedException {
    Semaphore semaphore = new Semaphore(10);
    long time1 = System.nanoTime();
    semaphore.acquire();
    long time2 = System.nanoTime();
    PubTools.sleep(1);
    long time3 = System.nanoTime();
    semaphore.release();
    long time4 = System.nanoTime();

    System.out.println("lock cost : " + (time2 - time1));
    System.out.println("calculate cost : " + (time3 - time2));
    System.out.println("unlock cost : " + (time4 - time3));
  }
}
