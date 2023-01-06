package com.shp.dev.chat.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;


@Slf4j
public class HtmlUtil {


    /**
     * 解压zip文件，获取html地址
     * @return html地址
     */
    public static String getHtmlPath() {
        try {
            String serverPath = System.getProperty("user.dir") + File.separator;
            ClassPathResource cpr = new ClassPathResource("zip/html.zip");
            InputStream in = cpr.getInputStream();
            String htmlPath = serverPath + "zip/html.zip";

            FileUtils.copyInputStreamToFile(in, new File(htmlPath));

            //解压压缩包
            UnZipUtils.unZip(new File(htmlPath), serverPath+"zip/");

            return serverPath + "zip/html" + File.separator + "index.html";
        } catch (Exception e) {
            log.error("复制文件错误，{}", e.getMessage());
            return "";
        }
    }




}
