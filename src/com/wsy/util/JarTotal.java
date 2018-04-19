package com.wsy.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/** 
 * 处理jar包工具类
 * @author DiazraelYU 
 * @date 2018.04.19 
 * @version 1.0 
 */  
public class JarTotal {

	// 存放所有的jar的包路径和名称
	public static Set<String> jarList = new HashSet<String>();
	//jad路径
	public static String jadPath = new String();

	/**
	 * jad反编译开始
	 */
	public static void jadRunner() {
		for (String dir : UnZip.classFileList) {
			File dirFile = new File(dir);
			System.out.println(dirFile);
			JarTotal.classToJavaByJad(dirFile, dir);
		}
	}

	public static void classToJavaByJad(File dir, String dirpath) {
		try {
			// 获取当前文件夹下的所有文件和文件夹
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				String classPath = files[i].getAbsolutePath()
						.replace("\\", "/");
				if (classPath.endsWith(".class")) {

					File file = new File(classPath);
					if (file.exists()) {
						// D:\\workspace\\CodeCounter\\jad.exe -o -r -d
						// "C:\\Users\\diazrael\\Desktop\\lib\\ant" -s java
						// "C\\Users\\diazrael\\Desktop\\lib\\ant\\**.class"
						String exeStr = jadPath + " -o -r -d " + dirpath
								+ " -s java " + classPath;
						System.out.println(classPath);
						Process p = Runtime.getRuntime().exec(exeStr);

						p.waitFor();
					}
				}
				// 如果是文件夹，递归
				if (files[i].isDirectory()) {
					classToJavaByJad(files[i], dirpath);
				}

			}
		} catch (Exception e) {
			System.err.println("classToJavaByJad error!");
		}

	}
}