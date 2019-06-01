package com.tmall.marketing.dingdingrobot;

import com.google.common.base.Splitter;
import com.tmall.marketing.dingdingrobot.model.WeatherDO;
import com.tmall.marketing.dingdingrobot.util.EatingHelper;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import com.tmall.marketing.dingdingrobot.util.WeatherHelper;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DingdingRobotApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testWeather(){
        WeatherDO weather = WeatherHelper.getWeather();
        System.out.println(weather.getWeather());
        System.out.println(weather.getXiaoDaiWeather());
    }

    @Test
    public void testEat(){
        for (int i=0;i<10;i++){
            System.out.println("AM" + ":" + EatingHelper.whereToEat(WeatherHelper.getWeather(), true).getDescription());
        }
        for (int i=0;i<10;i++){
            System.out.println("PM" + ":" + EatingHelper.whereToEat(WeatherHelper.getWeather(), false).getDescription());
        }
    }

    @Test
    public void testSendMsg(){
//        MessageHelper.sendTextMsgToXiaoDai("## 哈哈哈哈\n#### 哈哈哈\n都会对我说的\n#### 哈哈哈哈\n等哈就大好时机",null);
        List<MessageHelper.MarkDownEntity> list= Lists.newArrayList();
        list.add(new MessageHelper.MarkDownEntity("哈啊哈哈","打算的撒打算打算"));
        list.add(new MessageHelper.MarkDownEntity("哈啊哈哈2","打算的撒打算打算2"));
        MessageHelper.sendMarkDownMsgToXiaoDai("我是小呆",list);

    }


}
