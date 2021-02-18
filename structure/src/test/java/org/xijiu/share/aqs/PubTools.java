package org.xijiu.share.aqs;

/**
 * @author likangning
 * @since 2021/1/5 下午2:45
 */
public class PubTools {

  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
