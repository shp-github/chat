package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO
 * @CreateTime: 2021/4/4 21:33
 * @PackageName: com.shp.dev.chat.utils
 * @ProjectName: chat
 */

@Slf4j
public class ReadUtils {


    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 读取文本
     * @CreateTime: 2021/4/4 21:33
     * @param: file
     * @return: java.lang.String
     */
    public static String readText(File file) {
        String result = "";
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
