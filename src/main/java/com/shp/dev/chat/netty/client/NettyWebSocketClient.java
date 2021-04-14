package com.shp.dev.chat.netty.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO websocket客户端模板
 * @CreateTime: 2021/4/3 14:02
 * @PackageName: com.shp.dev.chat.netty.client
 * @ProjectName: chat
 */

@Slf4j
public class NettyWebSocketClient extends WebSocketClient {


    @SneakyThrows
    public static void main(String[] args) {
        URI uri = new URI("ws://192.168.1.17:8888/ws/chat");
        NettyWebSocketClient webSocketClient = new NettyWebSocketClient(uri);
        webSocketClient.send("pong");
    }

    @SneakyThrows
    public NettyWebSocketClient(URI serverUri) {
        super(serverUri);
        super.connectBlocking();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("{}websocket已连接，链接信息{}",this.getClass(),serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        log.info("{}websocket接收到的消息{}",this.getClass(),s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("{}websocket已关闭i{}s{}b{}",this.getClass(),i,s,b);
    }

    @Override
    public void onError(Exception e) {
        log.error("{}websocket发生错误,错误信息为{}",this.getClass(),e.getMessage());
    }
}
