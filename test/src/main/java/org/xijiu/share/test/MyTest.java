package org.xijiu.share.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class MyTest {

	private static User[] users = new User[10];

	static {
		for (int i = 0; i < users.length; i++) {
			users[i] = new User(i, "name" + i);
		}
	}

	@Test
	public void test() throws InterruptedException {
		User mid = users[5];
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executorService.submit(() -> {
				synchronized (mid) {
					System.out.println("进入lock");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	}



	@Getter
	@Setter
	@AllArgsConstructor
	private static class User {

		private long id;

		private String name;
	}

	@Test
	public void hashTest2() {
		int abc = 2147483647;
		System.out.println(Integer.toBinaryString(abc));

		abc = -5;
		System.out.println(Integer.toBinaryString(abc));
	}


	@Test
	public void hashTest() throws Exception {
//		System.out.println(Integer.highestOneBit(10));
//		System.out.println(Integer.lowestOneBit(17));
//
//		System.out.println(63 & 15);
//
//		String abc = "abceadsfljalfja";
//		System.out.println(abc.hashCode());


//		Thread thread1 = new Thread(this::a);
//		thread1.start();
//
//		Thread thread2 = new Thread(this::b);
//		thread2.start();
//
//		thread1.join();
//		thread2.join();

		int n = 16;
		System.out.println(n - (n >>> 2));

		System.out.println(12 & 1);
		System.out.println(13 & 1);
		System.out.println(14 & 1);
	}

	private synchronized void a() {
		try {
			System.out.println("开始a");
			Thread.sleep(1000);
			System.out.println("结束a");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void b() {
		try {
			System.out.println("开始b");
			Thread.sleep(2000);
			System.out.println("结束b");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void aaa() {

	}

}
