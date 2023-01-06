package com.shp.dev.chat.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
@Component
public class R extends HashMap<String, Object> {

    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String REAL_TIME = "realTime";
    public static final String ERROR_MSG = "操作失败！！！";
    public static final String SUCESSES_MSG = "操作成功！！！";
    public static final Integer SUCESSES_CODE = 200;
    public static final Integer ERROR_CODE = 500;
    public static final String RESULT_STATUS = "pd";
    public static final Boolean SUCESSES_RESULT_STATUS = true;
    public static final Boolean ERROR_RESULT_STATUS = false;

    /**
     * 返回错误信息
     */
    public static R error() {
        return error(ERROR_MSG);
    }

    /**
     * 返回自定义错误信息
     */
    public static R error(Object msg) {
        return error(msg, null);
    }

    /**
     * 返回错误信息和值
     */
    public static R error(Object msg, Object val) {
        return result(ERROR_CODE, msg, val);
    }


    /**
     * 返回成功信息
     */
    public static R success() {
        return success(SUCESSES_MSG);
    }

    /**
     * 返回成功数据
     */
    public static R success(Object obj) {
        return success(SUCESSES_MSG, obj);
    }

    /**
     * 返回成功信息和值
     */
    public static R success(Object msg, Object val) {
        return result(SUCESSES_CODE, msg, val);
    }


    /**
     * 赋值返回
     */
    public static R result(Object code, Object msg, Object val) {
        R res = new R();
        res.put(CODE, code);
        res.put(MSG, msg);
        res.put(DATA, val);
        res.put(REAL_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        res.put(RESULT_STATUS, code == SUCESSES_CODE);
        return res;
    }


}
