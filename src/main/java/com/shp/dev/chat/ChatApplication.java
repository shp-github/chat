package com.shp.dev.chat;

import com.shp.dev.chat.netty.service.NettyWebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//运行jar包设置字符集否则会中文乱码
//java -Dfile.encoding=utf-8 -jar xxx.jar

@SpringBootApplication
public class ChatApplication {


    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        new NettyWebSocketServer().start();
    }

}
