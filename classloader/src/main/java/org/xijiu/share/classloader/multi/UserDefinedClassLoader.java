package org.xijiu.share.classloader.multi;

import java.io.*;

/**
 * @author likangning
 * @since 2020/7/25 下午3:21
 */
public class UserDefinedClassLoader extends ClassLoader {

	private String rootDir;

	public UserDefinedClassLoader() {
		// 注：父类的无参构造方法，会将当前类的parent类指定为AppClassLoader
		this.rootDir = "/Users/likangning/study/share/classloader/ud_class/";
	}

	// 获取类的字节码
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = getClassData(name);  // 获取类的字节数组
		if (classData == null) {
			throw new ClassNotFoundException();
		} else {
			return defineClass(name, classData, 0, classData.length);
		}
	}

	private byte[] getClassData(String className) {
		// 读取类文件的字节
		String path = classNameToPath(className);
		try {
			InputStream ins = new FileInputStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bufferSize = 4096;
			byte[] buffer = new byte[bufferSize];
			int bytesNumRead = 0;
			// 读取类文件的字节码
			while ((bytesNumRead = ins.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesNumRead);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String classNameToPath(String className) {
		// 得到类文件的完全路径
		return rootDir + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
	}
}
