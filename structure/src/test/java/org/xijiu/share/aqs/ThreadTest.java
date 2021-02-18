package org.xijiu.share.aqs;

import org.junit.Test;

import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author likangning
 * @since 2021/2/8 上午10:23
 */
public class ThreadTest {

  @Test
  public void test() {
    Object obj = new Object();
    Thread thread1 = new Thread(() -> {
      synchronized (obj) {
        while (1 == 1) {
          if (1 == 3) {
            break;
          }
        }
        try {
          System.out.println("进入等待");
          obj.wait(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    thread1.start();
    PubTools.sleep(100);
    System.out.println(thread1.getState());
  }

  @Test
  public void test2() {
    Thread thread1 = new Thread(() -> {
      PubTools.sleep(1000000);
    });
    thread1.start();

    Thread thread2 = new Thread(() -> {
      try {
        thread1.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread2.start();




    PubTools.sleep(1000);
    System.out.println(thread1.getState());
    System.out.println(thread2.getState());
  }


  @Test
  public void test3() throws InterruptedException {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    Thread thread1 = new Thread(() -> {
      try {
        lock.lock();
        System.out.println("线程a即将进入等待");
        condition.await(111, TimeUnit.DAYS);
        System.out.println("线程a等待结束");
        lock.unlock();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread1.start();

    PubTools.sleep(1000);
    System.out.println(thread1.getState());
  }


  @Test
  public void test4() {
    Thread[] threadArr = new Thread[5];
    for (int i = 0; i < threadArr.length; i++) {
      threadArr[i] = new Thread(() -> {
        int sum = 0;
        while (true) {
          sum = sum + 1;
        }
      });
      threadArr[i].start();
    }

    PubTools.sleep(1000);
    for (Thread thread : threadArr) {
      System.out.println(thread.getState());
    }
  }

  private volatile boolean stop = false;

  @Test
  public void test5() throws Exception {
    Object obj = new Object();
    Thread thread1 = new Thread(() -> {
      synchronized (obj) {
        int sum = 0;
        // 模拟线程运行
        while (!stop) {
          sum++;
        }
      }
    });
    thread1.start();

    // 停顿1秒钟后再启动线程2，保证线程1已启动运行
    Thread.sleep(1000);

    Thread thread2 = new Thread(() -> {
      synchronized (obj) {
        System.out.println("进入锁中");
      }
    });
    thread2.start();

    System.out.println("线程1状态：" + thread1.getState());
    System.out.println("线程2状态：" + thread2.getState());

    thread2.interrupt();
    stop = true;
    Thread.sleep(1000);
    System.out.println("线程2状态：" + thread2.getState());
  }



  @Test
  public void test6() throws Exception {
    Object obj = new Object();
    Thread[] threads = new Thread[2];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(() -> {
        synchronized (obj) {
          try {
            obj.wait();
            // 模拟后续运算，线程不会马上结束
            while (1 == 1) {
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
      threads[i].setName("线程" + (i + 1));
      threads[i].start();
    }

    Thread.sleep(1000);

    // 激活所有阻塞线程
    synchronized (obj) {
      obj.notifyAll();
    }

    Thread.sleep(1000);

    System.out.println("线程1状态：" + threads[0].getState());
    System.out.println("线程2状态：" + threads[1].getState());
  }

  @Test
  public void test7() throws Exception {
    Thread thread1 = new Thread(() -> {
      // 死循环，模拟运行
      while (1 == 1) {
      }
    });
    thread1.start();

    Thread thread2 = new Thread(() -> {
      try {
        thread1.join();
        System.out.println("线程2开始执行");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    thread2.start();

    Thread.sleep(1000);

    System.out.println("线程2状态：" + thread2.getState());
  }

  @Test
  public void test8() throws Exception {
    Thread thread1 = new Thread(LockSupport::park);
    thread1.start();

    Thread.sleep(1000);

    System.out.println("线程1状态：" + thread1.getState());
    thread1.interrupt();
    Thread.sleep(1000);
    System.out.println("线程1状态：" + thread1.getState());
  }

  @Test
  public void test9() throws Exception {
    Object lock = new Object();

    Thread thread1 = new Thread(() -> {
      synchronized (lock) {
        try {
          Thread.sleep(1000000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          System.out.println("执行 finally");
        }
      }
    });
    thread1.start();

    Thread.sleep(500);
    System.out.println("thread1 状态：" + thread1.getState());
    thread1.stop();
    Thread.sleep(500);
    System.out.println("调用了stop方法后的thread1 状态：" + thread1.getState());

    synchronized (lock) {
      System.out.println("我抢到锁了");
    }

    thread1.suspend();
    thread1.resume();

    thread1.destroy();
  }


  @Test
  public void test10() throws Exception {
    ReentrantLock reentrantLock = new ReentrantLock();
    Thread thread1 = new Thread(() -> {
      reentrantLock.lock();
      try {
        Thread.sleep(1000000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      reentrantLock.unlock();
    });
    thread1.start();

    Thread.sleep(500);
    System.out.println("thread1 状态：" + thread1.getState());
    thread1.stop();
    // 等待线程1结束
    while (thread1.getState() != Thread.State.TERMINATED) {
    }

    System.out.println("主线程尝试获取锁");
    reentrantLock.lock();
    System.out.println("主线程拿到了锁");
  }

  @Test
  public void test11() throws Exception {
    Object lock = new Object();
    Thread thread1 = new Thread(() -> {
      synchronized (lock) {
        try {
          Thread.sleep(2000000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          System.out.println("执行 finally");
        }
      }
    });
    thread1.start();

    Thread.sleep(500);

    thread1.suspend();
    System.out.println("已经将线程1暂停");

    System.out.println("准备获取lock锁");
    synchronized (lock) {
      System.out.println("主抢到锁了");
    }

  }
}
