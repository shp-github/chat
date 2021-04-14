package com.shp.dev.chat.netty.service;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO 通道组池，管理所有websocket连接
 * @CreateTime: 2021/3/16 17:20
 * @PackageName: com.shp.dev.network.common.util.netty
 * @ProjectName: network
 */
@Component
public class NettyChannelHandlerPool {

    public NettyChannelHandlerPool() {
    }

    //分组群发
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    //一对一独立通信
    public static ConcurrentHashMap<String, Channel> concurrentHashMap = new ConcurrentHashMap<>();
    //当前服务端所对应的客户端
    public static Channel channel;
    //一对一下拉选列表数据
    public static ConcurrentHashMap<String, String> selectHashMap = new ConcurrentHashMap<>();


}

