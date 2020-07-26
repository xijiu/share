package org.xijiu.share.classloader.multi;

import org.junit.Test;

/**
 * @author likangning
 * @since 2020/7/25 下午3:33
 */
public class MultiTest {

	@Test
	public void test() throws Exception {
		UserDefinedClassLoader userDefinedClassLoader1 = new UserDefinedClassLoader();
		UserDefinedClassLoader userDefinedClassLoader2 = new UserDefinedClassLoader();

		Class<?> clazz1 = Class.forName("org.xijiu.share.classloader.multi.User", true, userDefinedClassLoader1);
		System.out.println("clazz1 类加载器类名：" + clazz1.getClassLoader());
		Class<?> clazz2 = Class.forName("org.xijiu.share.classloader.multi.User", true, userDefinedClassLoader2);
		System.out.println("clazz2 类加载器类名：" + clazz2.getClassLoader());
		System.out.println("两个  User class 是否相等：" + (clazz1 == clazz2));

		Class<?> strClazz1 = Class.forName("java.lang.String", true, userDefinedClassLoader1);
		Class<?> strClazz2 = Class.forName("java.lang.String", true, userDefinedClassLoader2);
		System.out.println("两个String class 是否相等：" + (strClazz1 == strClazz2));
	}
}
