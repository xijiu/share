package org.xijiu.share.aqs;

import org.junit.Test;

/**
 * @author likangning
 * @since 2021/1/29 下午3:49
 */
public class ObjectWaitTest {

  private final Object obj = new Object();

  @Test
  public void test() throws Exception {
    Thread thread1 = new Thread(() -> {
      synchronized (obj) {
        System.out.println("线程1进入");
        try {
          obj.wait();
          System.out.println("线程1--啦啦啦");
          Thread.sleep(2222222222000L);
        } catch (InterruptedException e) {
          System.out.println("线程1发生了中断");
          e.printStackTrace();
        }
        System.out.println("线程1彻底结束");
      }
    });

    Thread thread2 = new Thread(() -> {
      synchronized (obj) {
        System.out.println("线程2进入");
        try {
          obj.wait();
          System.out.println("线程2--啦啦啦");
          Thread.sleep(2222222222000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
          System.out.println("线程2发生了中断");
        }
        System.out.println("线程2彻底结束");
      }
    });


    thread1.start();
    thread2.start();

    PubTools.sleep(3000);

    thread1.interrupt();
    thread2.interrupt();
//    synchronized (obj) {
//      System.out.println("释放所有锁");
//      obj.notifyAll();
//    }

    thread1.join();
    thread2.join();
  }

  public StringBuilder sb = new StringBuilder();


  @Test
  public void test2() throws InterruptedException {
    obj.wait();
//    synchronized (obj) {  // "阻塞1"
//      obj.wait(); // notify后进入 "阻塞2"
//      System.out.println("线程1--啦啦啦");
//    }
  }
}
