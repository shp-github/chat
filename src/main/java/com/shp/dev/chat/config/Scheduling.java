package com.shp.dev.chat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shp.dev.chat.netty.service.NettyChannelHandlerPool;
import com.shp.dev.chat.utils.ip.GetIP;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 定时获取ip
 */
@Slf4j
@Configuration
@EnableScheduling
public class Scheduling {


    @Scheduled(cron = "0/5 * * * * ? ") // 间隔5秒执行
    private void sendLocal() {
        //发送消息到本服务的客户端
        List<String> ips = GetIP.getIPList();
        ips.add(0, "请选择");
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(ips));

        Channel channel = NettyChannelHandlerPool.channel;
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(array.toString()));
            log.info("搜索到的ip段{}", ips);
        }

    }

}

