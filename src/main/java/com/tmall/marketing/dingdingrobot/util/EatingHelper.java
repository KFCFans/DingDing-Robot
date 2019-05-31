package com.tmall.marketing.dingdingrobot.util;


import com.tmall.marketing.dingdingrobot.model.WeatherDO;

import java.util.EnumSet;
import java.util.Random;

public class EatingHelper {

    public enum Restaurant{
        MilkingD(5,"麦金地餐厅"),
        NianNianLiXing(2,"礼信年年餐厅"),
        HappyMelt(1,"乐融食堂"),
        HappyWeeked(8,"八号楼食堂");

        private int index;
        private String description;

        Restaurant(int index,String description){
            this.index=index;
            this.description=description;
        }

        public String getDescription(){
            return description;
        }

        public int getIndex(){
            return index;
        }

        public static Restaurant valueOf(int index){
            switch (index){
                case 2:return NianNianLiXing;
                case 1:return HappyMelt;
                case 8:return HappyWeeked;
                default:return MilkingD;
            }
        }
    }

    public static Restaurant whereToEat(WeatherDO weatherDO){

        if (weatherDO==null){
            MessageHelper.sendTextMsgToXiaoDai("获取天气失败！",null);
            return calWhere2Eat();
        }
        // 如果不下雨，全随机
        if (!weatherDO.isRainOrSnow()){
            return calWhere2Eat();
        }
        // 如果下雨，鉴于鄙人带路的能力，只去就近的两个食堂吃饭
        EnumSet<Restaurant> restaurantEnumSet = EnumSet.of(Restaurant.MilkingD, Restaurant.NianNianLiXing);
        // 循环直到随机到附近的食堂
        Restaurant tmp=calWhere2Eat();
        while (!restaurantEnumSet.contains(tmp)){
            tmp=calWhere2Eat();
        }
        return tmp;


    }

    /**
     * 计算去哪个食堂吃饭
     * @return Restaurant的枚举
     */
    private static Restaurant calWhere2Eat() {
        int seed = new Random().nextInt(100);
        int index;
        if (seed < 40) {
            index=2;
        } else if (seed < 80) {
            index=5;
        } else if (seed < 90) {
            index=1;
        } else {
            index=8;
        }
        return Restaurant.valueOf(index);
    }

}
