package org.xijiu.share;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author likangning
 * @since 2020/7/23 上午11:40
 */
public class User {

	@Test
	public void test() throws Exception {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1, 2);
//		Field[] fields = HashMap.class.getDeclaredFields();
//		for (Field field : fields) {
//			System.out.println(field.getName());
//		}
		Field field = HashMap.class.getDeclaredField("threshold");
		field.setAccessible(true);
		Object val = field.get(map);
		System.out.println(val);

		System.out.println(30 & 16);
		System.out.println(31 & 16);
	}

	@Test
	public void testMap() {
		Map<Integer, Integer> map = new ConcurrentHashMap<>();
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			map.put(i, i);
		}
		System.out.println("cost : " + (System.currentTimeMillis() - begin));
	}
}
