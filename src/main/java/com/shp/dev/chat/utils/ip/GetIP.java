package com.shp.dev.chat.utils.ip;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetIP {

    public static void main(String[] args) {


        Map<String, String> hostnames = getHostnames(getIPList());
        for (Map.Entry<String, String> stringStringEntry : hostnames.entrySet()) {
            System.out.println(stringStringEntry);
        }

    }


    /**
     * 获取ip列表
     */
    @SneakyThrows(Exception.class)
    public static List<String> getIPList() {
        String localIP = InetAddress.getLocalHost().getHostAddress();
        //String localIP = AddressUtils.getInnetIp();

        String networkSegment = localIP.substring(0, localIP.lastIndexOf("."));
        Runtime r = Runtime.getRuntime();
        Process p;
        try {
            p = r.exec("arp -a");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inline;
            StringBuilder ips = new StringBuilder();
            while ((inline = br.readLine()) != null) {
                if (inline.contains(networkSegment)) {
                    ips.append(inline);
                }
            }
            br.close();
            List<String> ips1 = IPUtils.getIps(ips.toString());
            ips1.remove(networkSegment + ".1");
            ips1.remove(networkSegment + ".255");
            ips1.remove(localIP);
            return ips1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static Map<String, String> getHostnames(List<String> ips) {
        Map<String, String> map = new HashMap<String, String>();
        System.out.println("正在提取hostname...");
        for (String ip : ips) {
            String command = "ping -a " + ip;
            Runtime r = Runtime.getRuntime();
            Process p;
            try {
                p = r.exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inline;
                while ((inline = br.readLine()) != null) {
                    if (inline.contains("[")) {
                         int start = inline.indexOf("Ping ");
                         int end = inline.indexOf("[");
                         String hostname = inline.substring(start + "Ping ".length(), end - 1);
                        map.put(ip, hostname);
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}

