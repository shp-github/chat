package com.shp.dev.chat.netty.controller;

import com.shp.dev.chat.netty.service.NettyChannelHandlerPool;
import com.shp.dev.chat.netty.service.NettyWebSocketHandler;
import com.shp.dev.chat.utils.CommonFileUtils;
import com.shp.dev.chat.utils.HtmlUtils;
import com.shp.dev.chat.utils.ReadUtils;
import com.shp.dev.chat.utils.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @CreateBy: Administrator
 * @Version: 1.0
 * @Description: TODO
 * @CreateTime: 2021/4/2 23:11
 * @PackageName: com.shp.dev.chat.netty
 * @ProjectName: chat
 */
@RestController
@RequestMapping("/netty")
@CrossOrigin
@Api("NettyWebSocket暴露接口")
public class NettyController {

    @RequestMapping(value = "/getGroup", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("查询组中所有人")
    public ResultBean getGroup() {
        List<String> list = new ArrayList<>();
        NettyChannelHandlerPool.channelGroup.forEach(
                channel -> {
                    list.add(channel.id().asLongText());
                }
        );
        return ResultBean.success(list);
    }

    @RequestMapping(value = "/getMap", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("查询所有个体map中所有人")
    public ResultBean getMap() {
        return ResultBean.success(NettyChannelHandlerPool.concurrentHashMap);
    }

    @RequestMapping(value = "/selectHashMap", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("下拉选数据")
    public ResultBean selectHashMap() {
        return ResultBean.success(NettyChannelHandlerPool.selectHashMap);
    }

    @RequestMapping(value = "/selectArrayList", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("下拉选数据")
    public ResultBean selectArrayList() {
        List list=new ArrayList();
        for (Map.Entry<String, String> stringStringEntry : NettyChannelHandlerPool.selectHashMap.entrySet()) {
            list.add(stringStringEntry.getValue());
        }
        return ResultBean.success(list);
    }

    @RequestMapping(value = "/sendAll", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("群发消息")
    public ResultBean sendAll(String message) {
        NettyWebSocketHandler.sendAllMessage(message);
        return ResultBean.success();
    }

    @ApiOperation("客户端发送文件到服务端")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResultBean upload(MultipartFile file, String fileName, String frist, String last) {
        frist = System.getProperty("user.dir") + File.separator;
        if (file.isEmpty() || file == null || file.equals("")) {
            return ResultBean.error("上传失败 文件为空");
        }
        String filePath = CommonFileUtils.saveFile(file, fileName, frist, last);
        if (filePath != null) {
            return ResultBean.success("上传成功", filePath);
        }
        return ResultBean.error("上传失败");
    }

    @RequestMapping(value = "/writeHtml", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("读取html输出html到浏览器")
    public void writeHtml(HttpServletResponse res) {
        //设置传输格式
        res.setContentType("text/html;charset=gb2312");
        PrintWriter p = null;
        try {
            //创建bat文件
            p = res.getWriter();
            //复制出html文件
            String htmlPath = HtmlUtils.getHtmlPath();
            //输出页面
            String s = ReadUtils.readText(new File(htmlPath));
            p.print(s);
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
