package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonInvoke {
    /**
     * java项目调用python脚本
     * 输入：py文件的绝对路径
     * 输出：
     */
    public static void invokePy(String[] para) {
        //System.out.println("执行....");
        try {
            Process process=Runtime.getRuntime().exec(para);
            //process.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK")); //windows下编码GBK防止java读取乱码　　　　　　　　

            String line = null;
            while((line = reader.readLine())!=null) {
                System.out.println(line);
            }
            reader.close();
            process.destroy();   //结束子进程
            //process.waitFor();  //使子进程等待，可能会引起阻塞-待分析
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("结束....");
    }
}
