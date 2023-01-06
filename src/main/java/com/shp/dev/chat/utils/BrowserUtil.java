package com.shp.dev.chat.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 浏览器工具
 */
@Slf4j
public class BrowserUtil {


    @SneakyThrows(Exception.class)
    public static void main(String[] args) {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "http://baidu.com/");
    }

    /**
     *  使用默认浏览器打开文件
     */
    @SneakyThrows(Exception.class)
    public static void startDefaultBrowser(String url) {

        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");

        if (osName.startsWith("Windows")) {
            // windows
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (osName.startsWith("Mac OS")) {
            // Mac
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            Method method = fileMgr.getDeclaredMethod("openURL", String.class);
            method.invoke(null, url);
        } else {
            // Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            // 执行代码，在browser有值后跳出，
            for (int count = 0; count < browsers.length && browser == null; count++) {
                // 这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new RuntimeException("未找到任何可用的浏览器");
            } else {
                // 这个值在上面已经成功得到了一个进程。
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        }

    }


}
