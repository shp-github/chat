package com.shp.dev.chat.netty.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置NettyWebSocket服务
 */
@Slf4j
public class NettyWebSocketServer {

    @SneakyThrows(Exception.class)
    public void start() {

        EventLoopGroup parentGroup = new NioEventLoopGroup(), childGroup = new NioEventLoopGroup();
        ChannelFuture cf = null;

        try {

            //获取空闲端口
            SysFreePort sysFreePort = new SysFreePort();
            //int portAndFree = sysFreePort.getPortAndFree();
            int portAndFree=8888;
            //创建服务引导
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            // 绑定线程池
            bootstrap.group(parentGroup, childGroup)
                    // 指定使用的channel
                    .channel(NioServerSocketChannel.class)
                    //指定端口
                    .localAddress(portAndFree)
                    // 绑定客户端连接时候触发操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {

                            System.out.println("收到新连接");
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            ch.pipeline().addLast(new NettyWebSocketHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws/chat", null, true, 65536 * 10));
                        }
                    });

            // 服务器异步创建绑定
            cf = bootstrap.bind().sync();
            System.out.println(NettyWebSocketServer.class + " 启动正在监听： " + cf.channel().localAddress());

        } finally {
            if (cf != null) {
                // 关闭服务器通道
                cf.channel().closeFuture().sync();
            }
            // 释放线程池资源
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
        }
    }

}

