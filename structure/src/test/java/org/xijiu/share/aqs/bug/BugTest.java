package org.xijiu.share.aqs.bug;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author likangning
 * @since 2021/3/20 下午9:50
 */
public class BugTest {
  private static volatile int sum = 0;

  @Test
  public void test() throws InterruptedException {
    FindBugAQS aqs = new FindBugAQS();

    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < 200000; j++) {
          aqs.lock();
          sum++;
          aqs.unlock();
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(10, TimeUnit.DAYS);
    System.out.println(sum);
  }
}
