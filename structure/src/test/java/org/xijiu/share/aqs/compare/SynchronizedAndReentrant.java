package org.xijiu.share.aqs.compare;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/3/11 下午2:52
 */
public class SynchronizedAndReentrant {

  private static int THREAD_NUM = 5;

  private static int EXECUTE_COUNT = 30000000;

  /**
   * 模拟ReentrantLock处理业务
   */
  @Test
  public void test() throws InterruptedException {
    ReentrantLock reentrantLock = new ReentrantLock();
    long begin = System.currentTimeMillis();
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < THREAD_NUM; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < EXECUTE_COUNT; j++) {
          reentrantLock.lock();
          doBusiness();
          reentrantLock.unlock();
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    System.out.println("ReentrantLock cost : " + (System.currentTimeMillis() - begin));
  }

  private void doBusiness() {
  }

  /**
   * 模拟synchronized处理业务
   */
  @Test
  public void test2() throws InterruptedException {
    long begin = System.currentTimeMillis();
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < THREAD_NUM; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < EXECUTE_COUNT; j++) {
          synchronized (SynchronizedAndReentrant.class) {
            doBusiness();
          }
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    System.out.println("synchronized cost : " + (System.currentTimeMillis() - begin));
  }

}
