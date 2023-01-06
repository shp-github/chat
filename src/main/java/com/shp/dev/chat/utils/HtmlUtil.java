package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

            copyInputStreamToFile(in, new File(htmlPath));

            //解压压缩包
            UnZipUtil.unZip(new File(htmlPath), serverPath+"zip/");

            return serverPath + "zip/html" + File.separator + "index.html";
        } catch (Exception e) {
            log.error("复制文件错误，{}", e.getMessage());
            return "";
        }
    }



    private static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtil.copy(source, output);
                output.close(); // don't swallow close Exception if copy completes normally
            } finally {
                IOUtil.closeQuietly(output);
            }
        } finally {
            IOUtil.closeQuietly(source);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }



}
