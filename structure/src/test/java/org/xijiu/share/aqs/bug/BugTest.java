package org.xijiu.share.aqs.bug;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.xijiu.share.aqs.PubTools;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author likangning
 * @since 2021/3/20 下午9:50
 */
public class BugTest {
  private static volatile int sum = 0;

  @Test
  public void test() throws InterruptedException {
    FindBugAQSBak aqs = new FindBugAQSBak();

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

  private static volatile int number = 0;

  public static void main(String[] args) throws InterruptedException {
    new BugTest().test2();
  }

  @Test
  public void test2() throws InterruptedException {
    List<Thread> list = Lists.newArrayList();
    FindBugAQS aqs = new FindBugAQS();
    Thread thread1 = new Thread(() -> {
      aqs.lock();
      PubTools.sleep(5000);
      number++;
      aqs.unlock();
    });
    thread1.start();
    list.add(thread1);

    PubTools.sleep(500);

    for (int i = 0; i < 4; i++) {
      Thread thread2 = new Thread(() -> {
        aqs.lock();
        PubTools.sleep(500);
        number++;
        aqs.unlock();
      });
      thread2.start();
      list.add(thread2);
    }

    for (Thread thread : list) {
      thread.join();
    }
    System.out.println("number is " + number);
  }


  @Test
  public void test3() throws InterruptedException {
    Semaphore semaphore = new Semaphore(2);
    List<Thread> list = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      Thread thread = new Thread(() -> {
        semaphore.acquireUninterruptibly();
        PubTools.sleep(500);
        semaphore.release();
      });
      thread.start();
      list.add(thread);
    }

    for (Thread thread : list) {
      thread.join();
    }
  }

  @Test
  public void test4() {
    String userIds = "";

    String[] split = userIds.split("\n");
    List<Long> list = Lists.newArrayList();
    for (String str : split) {
      list.add(Long.parseLong(str));
    }
  }

}
