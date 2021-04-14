package com.shp.dev.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @CreateBy: shp
 * @Version: 1.0
 * @Description: TODO 定时同步到redis里
 * @CreateTime: 2020/9/25 16:49
 * @PackageName: com.shp.dev.network.common.util.start
 * @ProjectName: network
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class Scheduling {

    @Scheduled(cron = "*/6 * * * * ?")
    private void process1() {
    }

    //https://www.cnblogs.com/wadmwz/p/10315481.html
    //@Scheduled(fixedRate = 6000)：上一次开始执行时间点之后 6 秒再执行。
    //@Scheduled(fixedDelay = 6000)：上一次执行完毕时间点之后 6 秒再执行。
    //@Scheduled(initialDelay=1000, fixedRate=6000)：第一次延迟 1 秒后执行，之后按 fixedRate 的规则每 6 秒执行一次。
    @Scheduled(fixedRate = 6000)
    private void process2() {
    }

    @Scheduled(initialDelay=3000,fixedRate = Long.MAX_VALUE)
    private void process() {


    }


}
