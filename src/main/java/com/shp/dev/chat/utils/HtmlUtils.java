package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO
 * @CreateTime: 2021/4/3 21:30
 * @PackageName: com.shp.dev.chat.utils
 * @ProjectName: chat
 */

@Slf4j
public class HtmlUtils {


    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 获取html文件地址
     * @CreateTime: 2021/4/4 21:19
     * @param:
     * @return: java.lang.String
     */
    public static String getHtmlPath() {
        try {
            String serverPath = System.getProperty("user.dir") + File.separator;
            ClassPathResource cpr = new ClassPathResource("zip/html.zip");
            InputStream in = cpr.getInputStream();
            String htmlPath = serverPath + "zip/html.zip";
            FileUtils.copyInputStreamToFile(in, new File(htmlPath));

            //解压压缩包
            UnZipUtils.unZip(new File(htmlPath), serverPath);

            return serverPath + "html" + File.separator + "index.html";
        } catch (Exception e) {
            log.error("复制文件错误，{}", e.getMessage());
            return null;
        }
    }



}
