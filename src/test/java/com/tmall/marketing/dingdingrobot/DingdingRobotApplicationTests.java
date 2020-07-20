package com.tmall.marketing.dingdingrobot;

import com.tmall.marketing.dingdingrobot.dai.DaiScheduler;
import com.tmall.marketing.dingdingrobot.dai.model.Weather;
import com.tmall.marketing.dingdingrobot.dai.service.EatingRecommendService;
import com.tmall.marketing.dingdingrobot.common.utils.MessageHelper;
import com.tmall.marketing.dingdingrobot.dai.service.WeatherService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DingdingRobotApplicationTests {

    @Resource
    private DaiScheduler daiScheduler;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testWeather(){
        Weather weather = WeatherService.getWeather();
        Objects.requireNonNull(weather);
        System.out.println(weather.getWeather());
        System.out.println(weather.getXiaoDaiWeather());
    }

    @Test
    public void testEat(){
        for (int i=0;i<10;i++){
            System.out.println("AM" + ":" + EatingRecommendService.whereToEat(WeatherService.getWeather(), true).getDescription());
        }
        for (int i=0;i<10;i++){
            System.out.println("PM" + ":" + EatingRecommendService.whereToEat(WeatherService.getWeather(), false).getDescription());
        }
    }

    @Test
    public void testSendMsg(){
//        MessageHelper.sendTextMsgToXiaoDai("## 哈哈哈哈\n#### 哈哈哈\n都会对我说的\n#### 哈哈哈哈\n等哈就大好时机",null);
        List<MessageHelper.MarkDownEntity> list= Lists.newArrayList();
        list.add(new MessageHelper.MarkDownEntity("哈啊哈哈","打算的撒打算打算"));
        list.add(new MessageHelper.MarkDownEntity("哈啊哈哈2","打算的撒打算打算2"));
        MessageHelper.sendMarkDownMsg("我是小呆" ,list, MessageHelper.DAI_CLIENT);

    }

    @Test
    public void testSendRecommend() {
        daiScheduler.sendEatingMsg();
    }

}
