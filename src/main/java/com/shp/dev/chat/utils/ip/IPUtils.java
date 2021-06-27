package com.shp.dev.chat.utils.ip;


import lombok.SneakyThrows;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 扫描局域网ip
 */
public class IPUtils {


    /**
     * 获取局域网ip
     *
     * @return
     */
    @SneakyThrows
    public static List<String> getIps() {
        List<String> ipList = new ArrayList<>();
        //本地ip
        String localIP = InetAddress.getLocalHost().getHostAddress();
        //网段
        String networkSegment = localIP.substring(0, localIP.lastIndexOf("."));
        //更新网段中的ip
        for (int i = 1; i < 256; i++) {
//            String ip="ping -w 2 -n 1 " + networkSegment + "." + i;
            String ip = "ping " + networkSegment + "." + i;
            Runtime.getRuntime().exec(ip).destroy();
        }
        //获取网关中的ip
        Process process = Runtime.getRuntime().exec("arp -a");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        bufferedReader.readLine();
        String temp = null;
        while (((temp = bufferedReader.readLine()) != null && !temp.isEmpty())) {
            String ip = temp.trim().split(" ")[0];
            if (ip.contains(networkSegment)) {
                ipList.add(ip);
            }
        }
        //关闭资源
        process.destroy();
        bufferedReader.close();
        return ipList;
    }

    public static void main(String[] args) {
        List<String> ips = getIps();
        ips.forEach(System.out::println);

    }


    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        return Pattern.compile("^[-\\+]?[\\d]*$").matcher(str).matches();
    }


}
