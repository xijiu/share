package org.xijiu.share.classloader;

import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Vector;

/**
 * classloader相关的工具方法测试
 *
 * @author likangning
 * @since 2020/7/24 上午10:06
 */
public class ClassLoaderUtils {


	public static void main(String[] args) throws Exception {
		Field f = ClassLoader.class.getDeclaredField("classes");
		f.setAccessible(true);
		Vector classes = (Vector) f.get(ClassLoader.getSystemClassLoader());
		for (Object clazz : classes) {
			System.out.println(clazz);
		}
	}

	@Test
	public void getExtDirsPath() {
		String values = System.getProperty("java.ext.dirs");
		String[] split = values.split(":");
		for (String str : split) {
			System.out.println(str);
		}
	}

	@Test
	public void getBootstrapPath() {
		URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
		for (URL url : urls) {
			System.out.println(url.toExternalForm());
		}
	}
}
