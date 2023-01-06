package com.shp.dev.chat.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 防止 XSS 跨站脚本攻击
 */
@Component
public class XSSFilter implements Filter {

    // XSS处理Map
    private static final Map<String,String> XSS_MAP = new LinkedHashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 含有脚本：script
        XSS_MAP.put("[s|S][c|C][r|R][i|C][p|P][t|T]", "");
        // 含有脚本 javascript
        XSS_MAP.put("[\\\"\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\']", "\"\"");
        // 含有函数： eval
        XSS_MAP.put("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
        // 含有符号 <
        XSS_MAP.put("<", "&lt;");
        // 含有符号 >
        XSS_MAP.put(">", "&gt;");
        // 含有符号 (
        XSS_MAP.put("\\(", "(");
        // 含有符号 )
        XSS_MAP.put("\\)", ")");
        // 含有符号 '
        XSS_MAP.put("'", "'");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // 强制类型转换 HttpServletRequest
        HttpServletRequest httpReq = (HttpServletRequest)request;
        // 构造HttpRequestWrapper对象处理XSS
        HttpRequestWrapper httpReqWarp = new HttpRequestWrapper(httpReq,XSS_MAP);
        chain.doFilter(httpReqWarp, response);
 
    }

    @Override
    public void destroy() {
         
    }
}
