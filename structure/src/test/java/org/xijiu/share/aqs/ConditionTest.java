package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/2/4 上午11:54
 */
public class ConditionTest {

  @Test
  public void test() throws InterruptedException {
    Lock lock = new ReentrantLock();
    lock.lock();
    Condition condition = lock.newCondition();
    Thread.yield();

    Thread thread1 = new Thread(() -> {
      try {
        lock.lock();
        System.out.println("线程1即将进入等待");
//        condition.await();
        condition.await(111, TimeUnit.DAYS);
        System.out.println("线程1等待结束");
        lock.unlock();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Thread thread2 = new Thread(() -> {
      try {
        lock.lock();
        System.out.println("线程2即将进入等待");
        condition.await();
        System.out.println("线程2等待结束");
        lock.unlock();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread1.start();
    thread2.start();
    PubTools.sleep(1000);


    lock.lock();
    PubTools.sleep(2000);
    System.out.println("准备开始执行signalAll方法");
    condition.signalAll();
    System.out.println("signalAll方法执行完毕");
    lock.unlock();

    thread1.join();
    thread2.join();
  }

  @Test
  public void test2() {
    String userIds = "2742\n" +
        "210718478";
    String[] split = userIds.split("\n");
    System.out.println(split.length);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < split.length; i++) {
      sb.append(split[i]).append(",");
    }
    System.out.println(sb.toString());
  }
}
