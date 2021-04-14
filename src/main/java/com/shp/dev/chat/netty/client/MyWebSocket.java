package com.shp.dev.chat.netty.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO 网上案例
 * @CreateTime: 2021/4/3 13:53
 * @PackageName: com.shp.dev.chat.netty.client
 * @ProjectName: chat
 */
@Slf4j
public class MyWebSocket extends WebSocketClient {
    private int code;
    private String message;
    private boolean isPagefinished;


    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("ws://192.168.1.17:8888/ws/chat");
        MyWebSocket myWebSocket = new MyWebSocket(uri);
        myWebSocket.send("AAAAAAAAAAAAAAA");
    }

    public MyWebSocket(URI serverUri) {
        super(serverUri);
        this.waitConnect();

    }

    public void waitConnect() {
        try {
            this.connectBlocking();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.warn("###建立连接");
    }

    @Override
    public void onMessage(String s) {
        log.warn("接受消息："+s);
        synchronized (this) {
            if (!StringUtils.isEmpty(s)) {
                JSONObject obj = JSON.parseObject(s);
                if (obj.containsKey("id") && obj.getIntValue("id") == code) {
                    //处理交互消息
                    this.message = s;
                    this.notify();
                    if (obj.getIntValue("id") != 900) log.warn("接受内容消息：" + s);
                } else if (obj.containsKey("method") && "Page.lifecycleEvent".equals(obj.getString("method"))) {
                    //处理事件消息
                    JSONObject eventObj = obj.getJSONObject("params");
                    //渲染完成事件
                    if (eventObj.containsKey("name") && ("firstMeaningfulPaint".equals(eventObj.getString("name")) || "networkIdle".equals(eventObj.getString("name")))) {
                        this.setPagefinished(true);
                    }
                    log.warn("接受事件消息：" + s);
                }
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.warn("###关闭连接:" + this.getURI() + "-" + i + "-" + s + "-" + b);
    }

    @Override
    public void onError(Exception e) {
        log.error("###通信错误" + e.getMessage(), e);
    }

    public String sendAndGet(String text, int code) {
        synchronized (this) {
            message = null;
            this.code = code;
            try {

                this.send(text);
                this.wait(120000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        return message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPagefinished() {
        return isPagefinished;
    }

    public void setPagefinished(boolean pagefinished) {
        isPagefinished = pagefinished;
    }
}
