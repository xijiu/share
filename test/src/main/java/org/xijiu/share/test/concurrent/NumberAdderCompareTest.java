package org.xijiu.share.test.concurrent;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author likangning
 * @since 2020/12/8 下午8:15
 */
public class NumberAdderCompareTest {

	private static final long THRESHOLD = 100000000;

	private transient volatile long baseCount = 0;

	/** Generates per-thread initialization/probe field */
	private static final AtomicInteger probeGenerator = new AtomicInteger();

	private static final AtomicLong seeder = new AtomicLong(initialSeed());

	private static long initialSeed() {
		String pp = java.security.AccessController.doPrivileged(
				new sun.security.action.GetPropertyAction(
						"java.util.secureRandomSeed"));
		if (pp != null && pp.equalsIgnoreCase("true")) {
			byte[] seedBytes = java.security.SecureRandom.getSeed(8);
			long s = (long)(seedBytes[0]) & 0xffL;
			for (int i = 1; i < 8; ++i)
				s = (s << 8) | ((long)(seedBytes[i]) & 0xffL);
			return s;
		}
		return (mix64(System.currentTimeMillis()) ^
				mix64(System.nanoTime()));
	}


	/**
	 * The increment of seeder per new instance
	 */
	private static final long SEEDER_INCREMENT = 0xbb67ae8584caa73bL;

	/**
	 * The increment for generating probe values
	 */
	private static final int PROBE_INCREMENT = 0x9e3779b9;

	@Test
	public void singleBaseCountAdder() throws Exception {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 8; i++) {
			cachedThreadPool.submit(() -> {
				long successNum = 0;
				while (baseCount < THRESHOLD) {
					long originValue = baseCount;
					boolean result = U.compareAndSwapLong(this, BASECOUNT, originValue, originValue + 1);
					if (result) {
						successNum++;
					}
				}
				System.out.println("线程执行成功个数：" + successNum);
			});
		}
		cachedThreadPool.shutdown();
		cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		long cost = System.currentTimeMillis() - begin;
		System.out.println("baseCount 当前值为：" + baseCount);
		System.out.println("累加至" + THRESHOLD + "，单个变量累加耗时：" + cost);
	}

	@Test
	public void aaa() throws InterruptedException {
		AtomicLong atomicLong = new AtomicLong();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 8; i++) {
			cachedThreadPool.submit(() -> {
				long tmp = 0;
				while (tmp < THRESHOLD) {
					tmp = atomicLong.incrementAndGet();
				}
			});
		}
		cachedThreadPool.shutdown();
		cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		long cost = System.currentTimeMillis() - begin;
		System.out.println("baseCount 当前值为：" + atomicLong.get());
		System.out.println("累加至" + THRESHOLD + "，单个变量累加耗时：" + cost);
	}



	@Test
	public void test() throws Exception {
		int probe = getProbe();
		System.out.println(probe);
		localInit();
		probe = getProbe();
		System.out.println(probe);

		Thread thread1 = new Thread(this::runMethod);
		thread1.start();
		thread1.join();

		Thread thread2 = new Thread(this::runMethod);
		thread2.start();
		thread2.join();
	}

	private void runMethod() {
		localInit();
		int probe = getProbe();
		long id = Thread.currentThread().getId();
		for (int i = 0; i < 10; i++) {
			System.out.println(probe = advanceProbe(probe));
			System.out.println(id + " : " + ((probe) & 7));
			System.out.println("\n");
		}
	}

	/**
	 * Pseudo-randomly advances and records the given probe value for the
	 * given thread.
	 */
	static final int advanceProbe(int probe) {
		probe ^= probe << 13;   // xorshift
		probe ^= probe >>> 17;
		probe ^= probe << 5;
		U.putInt(Thread.currentThread(), PROBE, probe);
		return probe;
	}


	static final void localInit() {
		int p = probeGenerator.addAndGet(PROBE_INCREMENT);
		int probe = (p == 0) ? 1 : p; // skip 0
		long seed = mix64(seeder.getAndAdd(SEEDER_INCREMENT));
		Thread t = Thread.currentThread();
		U.putLong(t, SEED, seed);
		U.putInt(t, PROBE, probe);
	}

	private static long mix64(long z) {
		z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
		z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
		return z ^ (z >>> 33);
	}

	static final int getProbe() {
		return U.getInt(Thread.currentThread(), PROBE);
	}




	@sun.misc.Contended static final class CounterCell {
		volatile long value;
		CounterCell(long x) { value = x; }
	}

	// Unsafe mechanics
	private static final sun.misc.Unsafe U;
	private static final long BASECOUNT;
//	private static final long CELLSBUSY;
//	private static final long CELLVALUE;
	private static final long PROBE;
	private static final long SEED;

	static {
		try {
			U = getUnsafe();
			Class<?> k = NumberAdderCompareTest.class;
			Class<?> tk = Thread.class;

			BASECOUNT = U.objectFieldOffset(k.getDeclaredField("baseCount"));
			PROBE = U.objectFieldOffset(tk.getDeclaredField("threadLocalRandomProbe"));
			SEED = U.objectFieldOffset
					(tk.getDeclaredField("threadLocalRandomSeed"));

		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		}
	}

	private static Unsafe getUnsafe() throws Exception {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		return (Unsafe) f.get(null);
	}
}
