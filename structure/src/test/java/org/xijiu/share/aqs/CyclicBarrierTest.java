package org.xijiu.share.aqs;

import org.junit.Test;

import java.util.concurrent.CyclicBarrier;

/**
 * @author likangning
 * @since 2021/1/25 下午8:35
 */
public class CyclicBarrierTest {

  @Test
  public void test() throws Exception {
    CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
    cyclicBarrier.await();
  }
}
