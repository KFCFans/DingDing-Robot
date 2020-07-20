package com.tmall.marketing.dingdingrobot.dai;

import com.google.common.collect.Lists;
import com.tmall.marketing.dingdingrobot.dai.model.Weather;
import com.tmall.marketing.dingdingrobot.common.CommonFields;
import com.tmall.marketing.dingdingrobot.dai.service.EatingRecommendService;
import com.tmall.marketing.dingdingrobot.common.utils.MessageHelper;
import com.tmall.marketing.dingdingrobot.dai.service.PoetryService;
import com.tmall.marketing.dingdingrobot.dai.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@Component
public class DaiScheduler {

    @Scheduled(cron = "0 45 11,17 * * ?")
    public void sendEatingMsg() {

        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        EnumSet<DayOfWeek> enumSet=EnumSet.of(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY);
        // 双休日不通知
        if (enumSet.contains(dayOfWeek)){
            return;
        }
        // FIXME: 法定节假日，比如国庆、五一等

        // 获取天气
        Weather weather = WeatherService.getWeather();

        List<MessageHelper.MarkDownEntity> list= Lists.newArrayList();

        // 添加吃饭地点
        EatingRecommendService.Restaurant restaurant = EatingRecommendService.whereToEat(weather, hour <= 11);
        list.add(new MessageHelper.MarkDownEntity("今日餐厅", restaurant.getDescription()));
        // 添加天气预报,eg:阴,22度,东南风1级,相对湿度57%
        list.add(new MessageHelper.MarkDownEntity("天气播报", weather == null ? "N/A" : weather.getXiaoDaiWeather()));
        // 每日一句
        list.add(new MessageHelper.MarkDownEntity("每日一句", PoetryService.fetchPoetry().mini()));

        MessageHelper.sendMarkDownMsg("小呆觅食助手", list, MessageHelper.DAI_CLIENT);
    }
}
