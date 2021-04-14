package com.shp.dev.chat.conf;

import com.shp.dev.chat.utils.BrowserTools;
import com.shp.dev.chat.utils.HtmlUtils;
import lombok.RequiredArgsConstructor;
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

/**
 * @CreateBy: shp
 * @version：1.0
 * @Description: TODO 启动banner
 * @CreateTime: 2021/1/5 15:55
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    public static int serverPort;
    public static String pid;
    public static String ip;

    @Override
    public void run(ApplicationArguments args) throws UnknownHostException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        pid = name.split("@")[0];
        InetAddress ip4 = Inet4Address.getLocalHost();
        ip = ip4.getHostAddress();
        System.out.println("////////////////////////////////////////////////////////////////////\n" +
                "//                          _ooOoo_                               //\n" +
                "//                         o8888888o                              //\n" +
                "//                         88\" . \"88                              //\n" +
                "//                         (| ^_^ |)                              //\n" +
                "//                         O\\  =  /O                              //\n" +
                "//                      ____/`---'\\____                           //\n" +
                "//                    .'  \\\\|     |//  `.                         //\n" +
                "//                   /  \\\\|||  :  |||//  \\                        //\n" +
                "//                  /  _||||| -:- |||||-  \\                       //\n" +
                "//                  |   | \\\\\\  -  /// |   |                       //\n" +
                "//                  | \\_|  ''\\---/''  |   |                       //\n" +
                "//                  \\  .-\\__  `-`  ___/-. /                       //\n" +
                "//                ___`. .'  /--.--\\  `. . ___                     //\n" +
                "//              .\"\" '<  `.___\\_<|>_/___.'  >'\"\".                  //\n" +
                "//            | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |                 //\n" +
                "//            \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /                 //\n" +
                "//      ========`-.____`-.___\\_____/___.-`____.-'========         //\n" +
                "//                           `=---='                              //\n" +
                "//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //\n" +
                "//            佛祖保佑       永不宕机      永无BUG                  　//\n" +
                "////////////////////////////////////////////////////////////////////");
        log.info("++++++++++++++++++++++++++++++++++++++++++++");
        log.info("生意我所欲，抑郁了我去");
        log.info("恭喜您，启动成功!");
        log.info("服务端口:{}", serverPort);
        log.info("进程ID:{}", pid);
        log.info("服务IP:{}", ip);
        log.info("++++++++++++++++++++++++++++++++++++++++++++");

        //获取html文件地址
        String htmlPath = HtmlUtils.getHtmlPath();
        //使用浏览器打开
        BrowserTools.anyStartBrowserToPath(htmlPath);

    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }
}

