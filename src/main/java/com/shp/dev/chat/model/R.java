package com.shp.dev.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@ApiModel("统一的返回值类")
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
@Component
public class R extends HashMap<String, Object> {

    @ApiModelProperty("返回状态码的key")
    public static final String CODE = "code";
    @ApiModelProperty("返回信息的key")
    public static final String MSG = "msg";
    @ApiModelProperty("返回数据的key")
    public static final String DATA = "data";
    @ApiModelProperty("返回操作时间key")
    public static final String REAL_TIME = "realTime";
    @ApiModelProperty("操作成功的信息")
    public static final String ERROR_MSG = "操作失败！！！";
    @ApiModelProperty("操作失败的信息")
    public static final String SUCESSES_MSG = "操作成功！！！";
    @ApiModelProperty("操作成功的状态")
    public static final Integer SUCESSES_CODE = 200;
    @ApiModelProperty("操作失败的状态")
    public static final Integer ERROR_CODE = 500;
    @ApiModelProperty("返回状态key")
    public static final String RESULT_STATUS = "pd";
    @ApiModelProperty("返回状态成功值")
    public static final Boolean SUCESSES_RESULT_STATUS = true;
    @ApiModelProperty("返回状态失败值")
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
