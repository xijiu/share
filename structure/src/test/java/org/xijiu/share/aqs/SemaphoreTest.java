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
}
