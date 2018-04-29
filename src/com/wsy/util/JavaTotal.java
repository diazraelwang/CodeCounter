package com.wsy.util;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.io.PrintWriter;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** 
 * 统计指定项目的目录下的*.java代码行数 
 * @author DiazraelYU 
 * @date 2018.04.19 
 * @version 1.0 
 */  
public class JavaTotal {  
  
    //项目java文件所在目录  
    public static String javaPath = "";  
    //统计代码行数  
    public static Integer countCode = 0;  
      
    public static int runJavaTotal(){
        try {  
            File filetxtPath = new File(new File(".").getAbsolutePath()+File.separator+"javaFileCount.txt");//输出要统计的文件信息  
            PrintWriter pw = new PrintWriter(new FileWriter(filetxtPath));  
            
            List<File> list = total(javaPath);  
            for (File file : list) {  
                String javaName = file.getAbsolutePath().replace("\\", "/");  
                if(javaName.endsWith(".java") || javaName.endsWith(".jsp")){  
                    pw.println(javaName);
                }
            }  
            pw.println("总java与jsp文件数量 ：" + list.size());  
            
            System.err.println("Valid files total:"+list.size());  
            countJavaLine(list);  
            pw.println("总行数 ：" + countCode);  
            pw.close();  
            System.err.println("line total:" + countCode);  
              
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
          
        return countCode;  
    }  
    /** 
     * 获取所有的文件 
     * @param path 获取文件的路径 
     * @return 
     */  
    public static List<File> total(String path){  
        List<File> fileList = null;  
        try {  
            fileList = new ArrayList<File>();  
            File filePath = new File(path);  
            File[] files = filePath.listFiles();//listFiles能够获取当前文件夹下的所有文件和文件夹  
            for (File file : files) {  
                if(file.isFile() && file.getName().endsWith(".java")){  
                    fileList.add(file);  
                }else if(file.isFile() && file.getName().endsWith(".jsp")){
                	fileList.add(file);
                }else{  
                    fileList.addAll(fileList.size(), total(file.getPath()));  
                }  
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
          
        return fileList;  
    }  
      
    /** 
     * 统计项目中java代码的行数 
     * @param listFile 文件的集合 
     */  
    public static void countJavaLine(List<File> listFile){  
        try {  
            for (File file : listFile) {  
                if(file.getName().endsWith(".java")){ 
                    FileReader fr = new FileReader(file);  
                    BufferedReader br = new BufferedReader(fr);  
                    String line = "";  
                    while((line = br.readLine()) != null){  
                    	String realline = line.replace(" ","");
                    	realline = realline.replace("\t","");
                    	if(realline.length() <= 0){
                    		continue;
                    	}
                    	
                    	//排除所有注释
                    	if(realline.startsWith("//")||realline.startsWith("/*") ||realline.startsWith("*")){
                    		continue;
                    	}
                        countCode ++;  
                    }  
                } else if(file.getName().endsWith(".jsp")){
                	FileReader fr = new FileReader(file);
                	BufferedReader br = new BufferedReader(fr);
                	String line = "";
                	boolean jflag = false;
                	while((line = br.readLine()) != null){
                		if(line.indexOf("<%") > -1){
                			if(line.indexOf("%>") > -1){
                				countCode++;
                			}else{
                				String realline = line.replace(" ","");
                            	realline = realline.replace("\t","");
                            	if(!realline.endsWith("<%")){
                            		countCode++;
                            	}
                            	jflag = true;
                			}
                		}else if(line.indexOf("%>") > -1){
                			String realline = line.replace(" ","");
                        	realline = realline.replace("\t","");
                        	if(!realline.startsWith("%>")){
                        		countCode++;
                        	}
                        	jflag = false;
                		}
                		//判断是否已经进入jsp代码块
                		else if(jflag == true){
                			String realline = line.replace(" ","");
                        	realline = realline.replace("\t","");
                        	if(realline.length() <= 0){
                        		continue;
                        	}
                        	//排除所有注释
                        	if(realline.startsWith("//")||realline.startsWith("/*") ||realline.startsWith("*")){
                        		continue;
                        	}
                            countCode ++;  
                		}
                	}
                }
            }  
        } catch (Exception e) {  
            System.err.println("countJavaLine error!");  
        }  
          
    }  
}  