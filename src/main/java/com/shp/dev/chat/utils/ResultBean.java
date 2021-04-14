package com.shp.dev.chat.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @CreateBy: shp
 * @version：1.0
 * @Description: TODO 返回值类
 * @CreateTime: 2020-07-08 10:54
 */

@ApiModel("统一的返回值类")
public class ResultBean<T> extends HashMap<String, Object> {

    //状态码
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String REAL_TIME = "realTime";

    @ApiModelProperty("返回的状态码: 200-正常 500-代码异常 ")
    public static Object codeVal;
    @ApiModelProperty("返回的提示信息: 默认为操作成功！ ")
    public static Object msgVal;
    @ApiModelProperty("返回的结果：List类型 ")
    public static Collection dataVal;

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回错误信息
     * @CreateTime: 2020-07-12 1:39
     * @param:
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean error() {
        return error("操作失败！！！");
    }

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回自定义错误信息
     * @CreateTime: 2020-07-12 1:39
     * @param: msg
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean error(String msg) {
        codeVal = 500;
        msgVal = msg;
        return result();
    }


    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回成功信息
     * @CreateTime: 2020-07-12 1:39
     * @param:
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean success() {
        return success("操作成功！！！",null);
    }

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回集合
     * @CreateTime: 2020-07-12 1:39
     * @param: list
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean success(List list) {
        return success("操作成功！！！", list);
    }

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回成功信息和集合
     * @CreateTime: 2020-07-12 1:38
     * @param: msg
     * @param: list
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean success(String msg, List list) {
        msgVal = msg;
        codeVal = 200;
        dataVal = list;
        return result();
    }

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回对象
     * @CreateTime: 2020-07-12 1:38
     * @param: obj
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean success(Object obj) {
        return success("操作成功！！！", obj);
    }

    /**
     * @CreateBy: shp
     * @version：1.0
     * @Description: TODO 返回信息 和返回对象
     * @CreateTime: 2020-07-12 1:38
     * @param: msg
     * @param: obj
     * @return: com.shp.dev.network.common.bean.ResultBean
     */
    public static ResultBean success(String msg, Object obj) {
        msgVal = msg;
        codeVal = 200;
        List list = new ArrayList();
        list.add(obj);
        dataVal = list;
        return result();
    }

    //赋值返回
    public static ResultBean result() {
        ResultBean ajaxResult = new ResultBean();
        ajaxResult.put(CODE, codeVal);
        ajaxResult.put(MSG, msgVal);
        ajaxResult.put(DATA, dataVal);
        ajaxResult.put(REAL_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        //清空本次返回产生的缓存
        dataVal = new ArrayList();
        msgVal = null;
        dataVal = null;
        return ajaxResult;
    }
}
