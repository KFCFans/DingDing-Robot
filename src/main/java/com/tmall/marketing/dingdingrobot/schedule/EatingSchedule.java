package com.tmall.marketing.dingdingrobot.schedule;

import com.google.common.collect.Lists;
import com.tmall.marketing.dingdingrobot.model.WeatherDO;
import com.tmall.marketing.dingdingrobot.util.CommonFields;
import com.tmall.marketing.dingdingrobot.util.EatingHelper;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import com.tmall.marketing.dingdingrobot.util.WeatherHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class EatingSchedule {

    @Scheduled(cron = "0 55 11,17 * * ?")
    public void sendEatingMsg(){

        // 获取天气
        WeatherDO weather = WeatherHelper.getWeather();
        // 如果获取失败则重写获取
        if (weather==null){
            // FIXME: 重写获取
            return;
        }

        List<MessageHelper.MarkDownEntity> list= Lists.newArrayList();
        // 添加吃饭地点
        list.add(new MessageHelper.MarkDownEntity("今日餐厅", EatingHelper.whereToEat(weather)));
        // 添加天气预报,eg:阴,22度,东南风1级,相对湿度57%
        list.add(new MessageHelper.MarkDownEntity("天气播报",weather.getXiaoDaiWeather()));
        // 添加吃饭人数
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<3;i++){
            if (CommonFields.willPresent[i]){
                sb.append(CommonFields.NAMEARRAY[i]).append(",");
            }
        }
        String members="无。今天是各奔东西的一天～";
        if (sb.length()!=0){
            members=sb.deleteCharAt(sb.length()).toString();
        }
        list.add(new MessageHelper.MarkDownEntity("吃饭人员",members));

        MessageHelper.sendMarkDownMsgToXiaoDai("吃饭助手",list);

        // 每次调用完毕后重置
        LocalTime localTime=LocalTime.now();
        int hour = localTime.getHour();
        CommonFields.willPresent[0]=true;
        CommonFields.willPresent[1]=true;
        // 中午触发，重置代表晚上，默认不一起吃。晚上触发，重置代表中午，默认一起吃。
        CommonFields.willPresent[2]=hour<11?false:true;

    }

}
