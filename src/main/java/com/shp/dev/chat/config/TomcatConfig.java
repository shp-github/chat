package com.shp.dev.chat.config;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {

        TomcatServletWebServerFactory tomcatServletContainerFactory = new TomcatServletWebServerFactory();

        tomcatServletContainerFactory.addContextCustomizers(context -> {
            SecurityConstraint constraint = new SecurityConstraint();
            SecurityCollection collection = new SecurityCollection();
            //http方法
            collection.addMethod("PUT");
            collection.addMethod("DELETE");
            collection.addMethod("HEAD");
            collection.addMethod("OPTIONS");
            collection.addMethod("TRACE");
            //url匹配表达式 所有路径
            collection.addPattern("/*");
            constraint.addCollection(collection);
            //设置以上HTTP方法在指定路径下需要身份验证约束
            constraint.setAuthConstraint(true);
            context.addConstraint(constraint);

            //设置使用httpOnly
            context.setUseHttpOnly(true);
        });
        return tomcatServletContainerFactory;
    }
}
