package com.shp.dev.chat.utils.ip;


import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * * ip地址工具类
 * * @author ACGkaka
 * *
 */
public class AddressUtils {


    public static final String localIP = AddressUtils.getInnetIp();
    public static final String networkSegment = localIP.substring(0, localIP.lastIndexOf("."));

    /**
     * 获取本机的内网ip地址
     *
     * @return String
     */
    @SneakyThrows(Exception.class)
    public static String getInnetIp() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(":")) {
                    localip = ip.getHostAddress();
                }
            }
        }
        return localip;
    }

    /**
     * 获取本机的外网ip地址
     */
    @SneakyThrows(Exception.class)
    public static String getIpv4() {
        String ip = "";
        String read = "";
        StringBuilder inputLine = new StringBuilder();
        //http://ip.tool.chinaz.com/
        URL url = new URL("http://ip.chinaz.com");
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        while ((read = in.readLine()) != null) {
            inputLine.append(read).append("\r\n");
        }
        in.close();
        Pattern p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
        }
        return ip;
    }


}
