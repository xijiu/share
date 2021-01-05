package org.xijiu.share;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author likangning
 * @since 2020/12/30 上午9:25
 */
public class LongAdderCompareAtomicLong {

  private static int THREAD_NUM = 4;

//  private static long MAX_VALUE = (long) Integer.MAX_VALUE;
  private static long MAX_VALUE = 1000000000L;

  @Test
  public void test() throws Exception {
    AtomicLong atomicLong = new AtomicLong();
    ExecutorService executorService = Executors.newCachedThreadPool();

    long begin = System.currentTimeMillis();
    for (int i = 0; i < THREAD_NUM; i++) {
      executorService.submit(() -> {
        while (true) {
          long value = atomicLong.incrementAndGet();
          if (value >= MAX_VALUE) {
            break;
          }
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    long cost = System.currentTimeMillis() - begin;
    System.out.println("atomic cost : " + cost);
  }

  @Test
  public void test2() throws Exception {
    LongAdder longAdder = new LongAdder();
    ExecutorService executorService = Executors.newCachedThreadPool();

    long begin = System.currentTimeMillis();
    for (int i = 0; i < THREAD_NUM; i++) {
      executorService.submit(() -> {
        int num = 0;
        while (true) {
          longAdder.increment();
          if (++num % 10000 == 0 && longAdder.sum() >= MAX_VALUE) {
            break;
          }
        }
      });
    }
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    long cost = System.currentTimeMillis() - begin;
    System.out.println("longAdder cost : " + cost);
  }
}
