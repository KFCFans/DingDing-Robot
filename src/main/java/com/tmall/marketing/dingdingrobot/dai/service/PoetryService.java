package com.tmall.marketing.dingdingrobot.dai.service;

import com.alibaba.fastjson.JSONObject;
import com.tmall.marketing.dingdingrobot.common.utils.HttpUtils;
import com.tmall.marketing.dingdingrobot.dai.model.Poetry;
import lombok.extern.slf4j.Slf4j;

/**
 * 古诗词
 * 每日一句，陶冶情操
 *
 * @author tjq
 * @since 2020/7/20
 */
@Slf4j
public class PoetryService {

    public static final String API = "https://v1.jinrishici.com/all.json";

    public static Poetry fetchPoetry() {
        try {
            String res = HttpUtils.get(API);
            return JSONObject.parseObject(res, Poetry.class);
        }catch (Exception e) {
            log.error("[PoetryService] fetch Poetry failed.", e);
        }
        return Poetry.defaultPoetry();
    }
}
