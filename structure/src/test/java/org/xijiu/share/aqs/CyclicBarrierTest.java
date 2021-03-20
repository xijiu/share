package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author likangning
 * @since 2021/1/25 下午8:35
 */
public class CyclicBarrierTest {

  @Test
  public void test() throws Exception {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 2; i++) {
      executorService.submit(() -> {
        PubTools.sleep(1000);
        try {
          System.out.println("开始进入等待");
          cyclicBarrier.await();
          System.out.println("结束await，开始执行逻辑");
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
  }
}
