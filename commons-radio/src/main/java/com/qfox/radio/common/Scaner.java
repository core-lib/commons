package com.qfox.radio.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * 字节码扫描器
 * 
 * @author Payne
 * 
 */
public class Scaner {
	private static final String CLASSPATH = System.getProperty("java.class.path").split(";")[0];

	/**
	 * 扫描指定的包下面的所有字节码
	 * 
	 * @param pkg
	 *            包名
	 * @param recursion
	 *            是否递归式扫描
	 * @return 如果recursion == true 则返回该包下及其子包的所有字节码 否则只是该包下的所有字节码
	 * @throws FileNotFoundException
	 *             包不存在
	 * @throws ClassNotFoundException
	 *             类不存在
	 */
	public static Set<Class<?>> scan(String pkg, boolean recursion) throws FileNotFoundException, ClassNotFoundException {
		String filepath = CLASSPATH + File.separatorChar + pkg.replace(".", File.separatorChar + "");
		return scan(new File(filepath), recursion);
	}

	/**
	 * 扫描指定文件的字节码 该文件必须在运行的工程中
	 * 
	 * @param file
	 *            文件
	 * @param recursion
	 *            是否递归式扫描
	 * @return 如果recursion == true 则返回该文件及其子文件的所有字节码 否则只是该文件的所有字节码
	 * @throws FileNotFoundException
	 *             文件不存在
	 * @throws ClassNotFoundException
	 *             类不存在
	 */
	public static Set<Class<?>> scan(File file, boolean recursion) throws FileNotFoundException, ClassNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}

		if (!file.getAbsolutePath().startsWith(CLASSPATH)) {
			throw new UnsupportedOperationException("can not scan file or directory outside of this project's class path");
		}

		Set<Class<?>> classes = new HashSet<Class<?>>();

		if (file.isDirectory()) {
			File[] files = file.listFiles();

			for (File sub : files) {
				if (sub.isFile()) {
					classes.addAll(scan(sub, recursion));
				} else if (recursion) {
					classes.addAll(scan(sub, recursion));
				}
			}

			return classes;
		} else {
			if (file.getName().endsWith(".class")) {
				String classname = file.getAbsolutePath().substring(CLASSPATH.length() + 1, file.getAbsolutePath().lastIndexOf(".class"));
				Class<?> _class = Class.forName(classname.replace(File.separatorChar + "", "."));
				classes.add(_class);
				return classes;
			} else {
				return classes;
			}
		}
	}

}
