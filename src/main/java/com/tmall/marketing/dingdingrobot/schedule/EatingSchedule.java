package com.tmall.marketing.dingdingrobot.schedule;

import com.google.common.collect.Lists;
import com.tmall.marketing.dingdingrobot.model.WeatherDO;
import com.tmall.marketing.dingdingrobot.util.CommonFields;
import com.tmall.marketing.dingdingrobot.util.EatingHelper;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import com.tmall.marketing.dingdingrobot.util.WeatherHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Component
@Slf4j
public class EatingSchedule {

    @Scheduled(cron = "0 45 11,17 * * ?")
    public void sendEatingMsg(){

        LocalDateTime localDateTime=LocalDateTime.now();
        int hour = localDateTime.getHour();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        EnumSet<DayOfWeek> enumSet=EnumSet.of(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY);
        // 双休日不通知
        if (enumSet.contains(dayOfWeek)){
            return;
        }
        // FIXME: 法定节假日，比如国庆、五一等

        // 获取天气
        WeatherDO weather = WeatherHelper.getWeather();
        // 如果获取失败则重写获取
        if (weather==null){
            // FIXME: 重写获取
            return;
        }

        List<MessageHelper.MarkDownEntity> list= Lists.newArrayList();
        // 添加吃饭地点
        EatingHelper.Restaurant restaurant = EatingHelper.whereToEat(weather, hour <= 11);
        list.add(new MessageHelper.MarkDownEntity("今日餐厅",restaurant.getDescription()));
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
            members=sb.deleteCharAt(sb.length()-1).toString();
        }
        list.add(new MessageHelper.MarkDownEntity("觅食人员",members));

        MessageHelper.sendMarkDownMsgToXiaoDai("小呆觅食助手",list);

        // 记录日志方便统计
        log.info("推荐地点:{}[{}]|人员:{}",restaurant.getIndex(),restaurant.getDescription(),members);

        // 每次调用完毕后重置
        CommonFields.willPresent[0]=true;
        CommonFields.willPresent[1]=true;
        // 中午触发，重置代表晚上，默认不一起吃。晚上触发，重置代表中午，默认一起吃。
        CommonFields.willPresent[2]=hour<=11?false:true;
    }

    // 每天早上5点清空点击信息
    @Scheduled(cron = "0 0 5 * * ? ")
    public void initWillPresent(){
        for (int i=0;i<CommonFields.willPresent.length;i++){
            CommonFields.willPresent[i]=true;
        }
    }
}
