package com.tmall.marketing.dingdingrobot.controller;

import com.tmall.marketing.dingdingrobot.util.CommonFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.DriverManager;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ServiceLoader;

@RestController
@RequestMapping("/eat")
public class EatingController {

    private Logger logger=LoggerFactory.getLogger(EatingController.class);

   @RequestMapping("/init")
    public String initNameArray(String names){
        CommonFields.NAMEARRAY=names.split(",");
        return Arrays.stream(CommonFields.NAMEARRAY).reduce((m1, m2) -> m1 + m2).get();

    }

    /*
    id: t:0 cr:1 cpp:2
    该逻辑被移入EatingSchedule中
    午餐：一般都是一起吃的，所以如果上午报备则说明不一起吃
    晚餐：
        2：一般不一起吃，因此报备说明一起吃
        others：一般一起吃，报备说明不一起吃
     */
    @RequestMapping("/accident")
    public String accidentHappen(@RequestParam(required = false) String id){

        // CPP使用频率应该最高，赋予默认使用权限
        if (StringUtils.isEmpty(id)){
            id="2";
        }
        int index=Integer.valueOf(id);

        if (index<0||index>2){
            return "不要欺负小呆！";
        }
        CommonFields.willPresent[index]=!CommonFields.willPresent[index];
        String flag=CommonFields.willPresent[index]?"一起吃":"不一起吃";


        return "您的选择是："+flag+"。";
    }

}
