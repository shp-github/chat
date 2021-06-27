package com.shp.dev.chat.utils.ip;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 扫描局域网ip
 */
@Slf4j
public class IPUtils {


    //private static final String  localIP = InetAddress.getLocalHost().getHostAddress();
    private static final String localIP = AddressUtils.getInnetIp();
    private static final String networkSegment = localIP.substring(0, localIP.lastIndexOf("."));


    public static void main(String[] args) {
        List<String> ips = IPUtils.getIpsPlus();
        ips.forEach(System.out::println);
    }


    /**
     * 获取局域网ip
     *
     * @return
     */
    @SneakyThrows
    public static List<String> getIpsPlus() {
        List<String> ipList = new ArrayList<>();
        Runtime runtime = Runtime.getRuntime();

        //更新trp表
        for (int i = 1; i < 104; i++) {
            String ip = "ping  " + networkSegment + "." + i;
            runtime.exec(ip);
        }

        //更新trp表
        callCmd(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "bat/ping.bat");

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


    /**
     * 获取局域网ip
     *
     * @return
     */
    @SneakyThrows
    public static List<String> getIps() {
        List<String> ipList = new ArrayList<>();
        //更新网段中的ip
        for (int i = 1; i < 256; i++) {
            String ip = "ping -w 2 -n 1 " + networkSegment + "." + i;
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


    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        return Pattern.compile("^[-\\+]?[\\d]*$").matcher(str).matches();
    }


    private static void callCmd(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            callCmd(file);
        } else {
            log.error("bat文件未找到");
        }
    }

    /**
     * 执行bat脚本
     *
     * @param file
     */
    private static void callCmd(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            Process child = Runtime.getRuntime().exec(file.getAbsolutePath());
            InputStream in = child.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            try {
                child.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            log.info(sb.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
