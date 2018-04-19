package com.wsy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * 解压缩工具类
 * 
 * @author DiazraelYU
 * @date 2018.04.19
 * @version 1.0
 */
public class UnZip {

	/**
	 * class目录集合
	 */
	public static Set<String> classFileList = new HashSet<String>();

	/**
	 * 解压zip文件
	 * 
	 * @param zipFilePath
	 *            压缩文件路径名 E:/b/a.zip
	 * @param base
	 *            解压文件存放路径 E:/b/c
	 * @param deleteFile
	 *            是否删除压缩包
	 * @return boolean 是否解压成功
	 * */
	public static boolean unZip(String zipFilePath, String base,
			boolean deleteFile) {
		try {
			File file = new File(zipFilePath);
			if (!file.exists()) {
				throw new RuntimeException("解压文件不存在!");
			}
			ZipFile zipFile = new ZipFile(file);
			Enumeration e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(base + "/" + name);
					f.mkdirs();
				} else {
					File f = new File(base + zipEntry.getName());
					f.getParentFile().mkdirs();
					f.createNewFile();
					InputStream is = zipFile.getInputStream(zipEntry);
					FileOutputStream fos = new FileOutputStream(f);
					int length = 0;
					byte[] b = new byte[1024];
					while ((length = is.read(b, 0, 1024)) != -1) {
						fos.write(b, 0, length);
					}
					is.close();
					fos.close();
				}
			}
			if (zipFile != null) {
				zipFile.close();
			}
			if (deleteFile) {
				file.deleteOnExit();
			}
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	/**
	 * 解压缩
	 * 
	 * @param dir
	 *            目标路径
	 */
	public static void UnZipJar(File dir) {

		// 存放所有的zip的包路径和名称
		Set<String> ZipList = new HashSet<String>();

		try {
			// 获取当前文件夹下的所有文件和文件夹
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				String jspPath = files[i].getAbsolutePath().replace("\\", "/");
				if (jspPath.endsWith(".jar")) {
					File file = new File(jspPath);
					if (file.exists()) {
						String newPath = jspPath.replace(".jar", ".zip");
						// System.out.println(newPath);
						file.renameTo(new File(newPath));

						String base = newPath.replace(".zip", "/");
						System.out.println(base);
						UnZip.unZip(newPath, base, false);
						classFileList.add(base);
						ZipList.add(newPath);
					}
				}
				// 如果是文件夹，递归
				if (files[i].isDirectory()) {
					UnZipJar(files[i]);
				}

			}
		} catch (Exception e) {
			System.err.println("UnZipJar error!");
		}
		for (String path : ZipList) {
			File file = new File(path);
			if (file.exists()) {
				String newPath = path.replace(".zip", ".jar");
				// System.out.println(newPath);
				file.renameTo(new File(newPath));

			}
		}
	}

	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		dir.delete();
	}

}
