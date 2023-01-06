package com.shp.dev.chat.config;

import com.shp.dev.chat.utils.BrowserUtil;
import com.shp.dev.chat.utils.HtmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Slf4j
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    public static int serverPort;
    public static String pid;
    public static String ip;

    @Override
    @SneakyThrows(Exception.class)
    public void run(ApplicationArguments args){

        String name = ManagementFactory.getRuntimeMXBean().getName();
        pid = name.split("@")[0];
        InetAddress ip4 = Inet4Address.getLocalHost();
        ip = ip4.getHostAddress();
        log.info("++++++++++++++++++++++++++++++++++++++++++++");
        log.info("启动成功!");
        log.info("服务端口:{}", serverPort);
        log.info("进程ID:{}", pid);
        log.info("服务IP:{}", ip);
        log.info("++++++++++++++++++++++++++++++++++++++++++++");

        //获取html文件地址
        String htmlPath = HtmlUtil.getHtmlPath();
        //使用浏览器打开
        BrowserUtil.startDefaultBrowser(htmlPath);

    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
    }
}

