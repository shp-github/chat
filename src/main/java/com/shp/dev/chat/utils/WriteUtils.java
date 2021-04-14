package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO 输出工具
 * @CreateTime: 2021/4/4 21:22
 * @PackageName: com.shp.dev.chat.utils
 * @ProjectName: chat
 */

@Slf4j
public class WriteUtils {

    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 输出文本本地
     * @CreateTime: 2021/4/4 21:23
     * @param: path
     * @param: str
     * @return: java.lang.String
     */
    public static String writeTxt(String path, String str) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.flush();
            bw.close();
            fw.close();
            return path;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 输出bat
     * @CreateTime: 2021/4/4 21:25
     * @param:
     * @return: java.lang.String
     */
    public static String writeBat() {
        try {
            String serverPath = System.getProperty("user.dir") + File.separator;
            String batPath = serverPath + "run.bat";
            return writeTxt(batPath, "java -Dfile.encoding=utf-8 -jar chat-0.0.1-SNAPSHOT.jar");
        } catch (Exception e) {
            log.error("复制文件错误，{}", e.getMessage());
            return null;
        }
    }

}
