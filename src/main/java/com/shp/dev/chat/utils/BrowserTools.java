package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO 浏览器工具
 * @CreateTime: 2021/4/4 21:21
 * @PackageName: com.shp.dev.chat.utils
 * @ProjectName: chat
 */

@Slf4j
public class BrowserTools {


    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 使用默认浏览器打开网址
     * @CreateTime: 2021/4/3 22:06
     * @param: url
     * @return: void
     */
    public static void startBrowserToURL(String url) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (Exception e) {
            log.error("Error executing progarm.{}", e.getMessage());
        }
    }

    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 使用默认浏览器打开文件
     * @CreateTime: 2021/4/4 19:23
     * @param: path
     * @return: void
     */
    public static void startBrowserToPath(String path) {
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (Exception e) {
            log.error("Error executing progarm.{}", e.getMessage());
        }
    }

    /**
     * @CreateBy: Administrator
     * @version：1.0
     * @Description: TODO 使用默认浏览器打开文件
     * @CreateTime: 2021/4/4 21:28
     * @param: url
     * @return: void
     */
    public static void anyStartBrowserToPath(String url) {
        try {
            String osName = System.getProperty("os.name", "");// 获取操作系统的名字

            if (osName.startsWith("Windows")) {
                // windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (osName.startsWith("Mac OS")) {// Mac
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else {
                // Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) { // 执行代码，在brower有值后跳出，
                    // 这里是如果进程创建成功了，==0是表示正常结束。
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new RuntimeException("未找到任何可用的浏览器");
                } else {// 这个值在上面已经成功的得到了一个进程。
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
