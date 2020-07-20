package com.tmall.marketing.dingdingrobot.dai.service;

import com.alibaba.fastjson.JSONObject;
import com.tmall.marketing.dingdingrobot.common.utils.HttpUtils;
import com.tmall.marketing.dingdingrobot.common.utils.MessageHelper;
import com.tmall.marketing.dingdingrobot.dai.model.Weather;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class WeatherService {

    private static final String API = "https://free-api.heweather.net/s6/weather/now?location=yuhang&key=81ff5d6c263441d8bfbed6c043ca6bcb";

    public static Weather getWeather() {

        try {
            String weatherJSON = HttpUtils.get(API);
            return JSONObject.parseObject(weatherJSON, Weather.class);
        } catch (Exception e) {
            log.error("[WeatherService] fetch weather failed.", e);
            // 由小呆发送错误信息
            try {
                MessageHelper.sendTextMsg("获取天气异常：" + e.getMessage(), null, MessageHelper.DAI_CLIENT);
            } catch (Exception ignore) {
                // 自生自灭吧，小呆～
            }
        }
        return null;
    }
}
