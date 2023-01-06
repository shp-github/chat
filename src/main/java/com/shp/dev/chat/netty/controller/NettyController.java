package com.shp.dev.chat.netty.controller;

import com.shp.dev.chat.model.R;
import com.shp.dev.chat.netty.service.NettyChannelHandlerPool;
import com.shp.dev.chat.netty.service.NettyWebSocketHandler;
import com.shp.dev.chat.utils.CommonFileUtils;
import com.shp.dev.chat.utils.HtmlUtil;
import com.shp.dev.chat.utils.ReadUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/netty")
@CrossOrigin
public class NettyController {

    @RequestMapping(value = "/getGroup", method = {RequestMethod.POST, RequestMethod.GET})
    public R getGroup() {
        List<String> list = new ArrayList<>();
        NettyChannelHandlerPool.channelGroup.forEach(
                channel -> {
                    list.add(channel.id().asLongText());
                }
        );
        return R.success(list);
    }

    @RequestMapping(value = "/getMap", method = {RequestMethod.POST, RequestMethod.GET})
    public R getMap() {
        return R.success(NettyChannelHandlerPool.concurrentHashMap);
    }

    @RequestMapping(value = "/selectHashMap", method = {RequestMethod.POST, RequestMethod.GET})
    public R selectHashMap() {
        return R.success(NettyChannelHandlerPool.selectHashMap);
    }

    @RequestMapping(value = "/selectArrayList", method = {RequestMethod.POST, RequestMethod.GET})
    public R selectArrayList() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> stringStringEntry : NettyChannelHandlerPool.selectHashMap.entrySet()) {
            list.add(stringStringEntry.getValue());
        }
        return R.success(list);
    }

    @SneakyThrows(Exception.class)
    @RequestMapping(value = "/sendAll", method = {RequestMethod.POST, RequestMethod.GET})
    public R sendAll(String message) {
        String ip = InetAddress.getLocalHost().getHostAddress();
        NettyWebSocketHandler.sendAllMessage("对方IP：" + ip + "群发消息：" + message);
        return R.success();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public R upload(MultipartFile file, String fileName, String frist, String last) {
        frist = System.getProperty("user.dir") + File.separator + "zip/file/";
        if (file.isEmpty()) {
            return R.error("上传失败 文件为空");
        }
        String filePath = CommonFileUtils.saveFile(file, fileName, frist, last);
        if (filePath != null) {
            return R.success("上传成功", filePath);
        }
        return R.error("上传失败");
    }

    @RequestMapping(value = "/writeHtml", method = {RequestMethod.POST, RequestMethod.GET})
    public void writeHtml(HttpServletResponse res) {
        //设置传输格式
        res.setContentType("text/html;charset=gb2312");
        PrintWriter p = null;
        try {
            //创建bat文件
            p = res.getWriter();
            //复制出html文件
            String htmlPath = HtmlUtil.getHtmlPath();
            //输出页面
            String s = ReadUtils.readText(new File(htmlPath));
            p.print(s);
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
