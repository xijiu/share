package org.xijiu.share.classloader;

import com.sun.javafx.UnmodifiableArrayList;

import java.sql.DriverManager;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		String property = System.getProperty("java.ext.dirs");
		String[] split = property.split(":");
		for (String s : split) {
			System.out.println(s);
		}
		System.out.println();


		ClassLoader classLoader = App.class.getClassLoader();
		System.out.println("app classLoader is " + classLoader.toString());
		System.out.println("app classLoader is " + String.class.getClassLoader());
		System.out.println("UnmodifiableArrayList classLoader is " + UnmodifiableArrayList.class.getClassLoader());
		System.out.println("thread classloader is " + Thread.currentThread().getContextClassLoader().toString());

//		DriverManager.getConnection()

	}
}
