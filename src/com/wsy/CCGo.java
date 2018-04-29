package com.wsy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.wsy.util.JarTotal;
import com.wsy.util.JavaTotal;
import com.wsy.util.UnZip;

/**
 * CodeCounterGo！
 * 
 * @author DiazraelYU
 * @date 2018.04.19
 * @version 1.0
 */
public class CCGo {
	public static void main(String[] args) {
		long start = System.nanoTime();
		JarTotal.jadPath = new File(".").getAbsolutePath()+File.separator+"jad.exe";
		
		if(args.length <= 0){
			System.out.println("Please input a project path!");
			System.out.println("Usage:java -jar CodeCounter.jar \"C:/Users/diazrael/Desktop/lib\" ");
			return;
		}
		System.out.println("注意：本工具仅计算有效java代码，可用于报价的辅助工具。");
		System.out.println("以下情况算作有效代码：");
		System.out.println("1.Java文件中的有效Java代码（可排除注释）。"
				+ "2.jsp文件中的Java代码（可排除注释）。"
				+ "3.jar包中反编译出来的java代码（此项过程中可选）。");
		
		JavaTotal.javaPath = args[0];
		//判断是否计算jar包中的行数
		String jarflag = readDataFromConsole("Whether include jars?(Y/N)");  
		if("Y".equalsIgnoreCase(jarflag)){
			// 解压缩Jar
			UnZip.UnZipJar(new File(JavaTotal.javaPath));
			// jad反编译class文件到原目录结构
			JarTotal.jadRunner();
		}
		
		// 调用javacounter计数
		JavaTotal.runJavaTotal();
		//是否删除反编译之后的文件夹
		if("Y".equalsIgnoreCase(jarflag)){
			String delflag = readDataFromConsole("Whether to delete the directory compiled?(Y/N)");  
			if("Y".equalsIgnoreCase(delflag)){
				for (String dir : UnZip.classFileList) {
					UnZip.deleteDir(new File(dir));
				}
			}
		}
		
		long end = System.nanoTime();
		System.out.println("Result saved in ./javaFileCount.txt.");
		System.out.print("cost: " + (end - start) / 1e9 + " seconds");
	}
	
	/** 
     * Use InputStreamReader and System.in to read data from console 
     *  
     * @param prompt 
     *             
     * @return input string 
     */  
    private static String readDataFromConsole(String prompt) {  
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
        String str = null;  
        try {  
        	System.out.print(prompt);  
            str = br.readLine();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
}
