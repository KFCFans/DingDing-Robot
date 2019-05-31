package com.tmall.marketing.dingdingrobot.util;

import com.alibaba.fastjson.JSONObject;
import com.tmall.marketing.dingdingrobot.model.WeatherDO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class WeatherHelper {

    public static WeatherDO getWeather(){
        // https://free-api.heweather.net/s6/weather/now?location=yuhang&key=81ff5d6c263441d8bfbed6c043ca6bcb

        String url="https://free-api.heweather.net/s6/weather/now?location=yuhang&key=81ff5d6c263441d8bfbed6c043ca6bcb";
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()){
                // 解析JSON
                WeatherDO weatherDO = JSONObject.parseObject(response.body().string(), WeatherDO.class);
                return weatherDO;

            }else {
                // 由小呆发送失败原因
                MessageHelper.sendTextMsgToXiaoDai("获取天气失败："+response.toString(),null);
                log.error(response.toString());

            }
        }catch (Exception e){
            log.error(e.getMessage());
            // 由小呆发送错误信息
            try {
                MessageHelper.sendTextMsgToXiaoDai("获取天气异常："+e.getMessage(),null);
            }catch (Exception ignore){
                // 自生自灭吧，小呆～
            }
        }

        return null;
    }
}
