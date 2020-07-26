package org.xijiu.share;


import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	private String name;
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() throws Exception {
//		Class<AppTest> clazz = AppTest.class;
//		Field nameField = clazz.getDeclaredField("name");
//		System.out.println(nameField);
//		System.out.println(clazz.getClassLoader());
//
//		Class<? extends Class> aClass = clazz.getClass();
//		System.out.println(aClass.getClassLoader());
//		Field[] fields = aClass.getDeclaredFields();
//		for (Field field : fields) {
//			field.setAccessible(true);
//			System.out.println(field.getName());
//		}

//		Field[] declaredFields = Class.class.getDeclaredFields();
//		for (Field declaredField : declaredFields) {
//			declaredField.setAccessible(true);
//			System.out.println(declaredField.getName());
//		}

		System.out.println(AppTest.class.getClassLoader());
		Class.forName("org.xijiu.share.AppTest", true, AppTest.class.getClassLoader().getParent());
	}

	@Test
	public void shouldAnswerWithTrue2() throws Exception {
		Class<?> clazz = Class.forName("org.xijiu.share.User", false, AppTest.class.getClassLoader());
		System.out.println(clazz.getName());
	}

}
