package com.tmall.marketing.dingdingrobot.common;

import lombok.Data;

@Data
public class ResultDTO<T> {
    private boolean success;
    private String msg;
    private T t;

    private ResultDTO(boolean success, String msg, T t) {
        this.success = success;
        this.msg = msg;
        this.t = t;
    }

    public static <T> ResultDTO<T> success(T t){
        return new ResultDTO<T>(true,null,t);
    }

    public static <T> ResultDTO<T> failed(String msg){
        return new ResultDTO<T>(false,msg,null);
    }

}
