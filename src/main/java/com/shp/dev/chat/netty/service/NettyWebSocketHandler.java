package com.shp.dev.chat.netty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO
 * @CreateTime: 2021/3/16 17:21
 * @PackageName: com.shp.dev.network.common.util.netty
 * @ProjectName: network
 */
@Slf4j
public class NettyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        log.info("与客户端建立连接，通道开启！{}", ctx);
        //添加到channelGroup通道组
        Channel channel = ctx.channel();
        //群发 一对多
        NettyChannelHandlerPool.channelGroup.add(channel);
        //单发 一对一
        NettyChannelHandlerPool.concurrentHashMap.put(channel.id().asLongText(), channel);

        //如果是本机链接，则存存到本服务器
        if (channel.remoteAddress().toString().split(":")[0].equals(channel.localAddress().toString().split(":")[0])) {
            NettyChannelHandlerPool.channel = channel;
            System.out.println("链接者为本机，添加到本服务的客户端");
        } else {
            //一对一下拉选列表
            String client = channel.remoteAddress().toString().split(":")[0].substring(1);
            NettyChannelHandlerPool.selectHashMap.put(client, "ws://" + client + ":8888/ws/chat");
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        NettyChannelHandlerPool.channelGroup.remove(ctx.channel());
        NettyChannelHandlerPool.concurrentHashMap.remove(ctx.channel().id().asLongText());
    }

    @SneakyThrows
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //首次连接是FullHttpRequest，处理链接地址中拼接的参数
        //ws://127.0.0.1:12345/ws?uid=666&gid=777
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            //获取完整ws的URL地址
            String uri = request.uri();
            Map paramMap = getUrlParams(uri);
            log.info("nettywebsocket接收到的参数是：{}", JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
        } else if (msg instanceof TextWebSocketFrame) {
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            String text = frame.text();
            if (text.equalsIgnoreCase("pong")) {
                //恢复客户端pong，保持心跳
                NettyChannelHandlerPool.concurrentHashMap.get(ctx.channel().id().asLongText()).writeAndFlush(new TextWebSocketFrame(text));
                return;
            }
            try {

                //发送消息到本服务的客户端
                NettyChannelHandlerPool.channel.writeAndFlush(new TextWebSocketFrame(text));

                //转换json格式，扩展使用
                JSONObject jsonObject = JSON.parseObject(text);
                //两个客户端之间通信id为另一个客户端的ctx.channel().id().asLongText()
                NettyChannelHandlerPool.concurrentHashMap.get(jsonObject.getString("id")).writeAndFlush(new TextWebSocketFrame(jsonObject.getString("msg")));

            } catch (Exception e) {
                log.error("转换json格式失败{}", e.getMessage());
            }
            log.info("NettyWebSocket客户端收到服务器数据：{}", text);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {
        log.info(channelHandlerContext.toString());
        log.info(textWebSocketFrame.toString());
    }

    public static void sendAllMessage(String message) {
        //收到信息后，群发给所有channel
        NettyChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * 提取参数
     */
    private static Map getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;
        } else {
            return map;
        }
    }
}
