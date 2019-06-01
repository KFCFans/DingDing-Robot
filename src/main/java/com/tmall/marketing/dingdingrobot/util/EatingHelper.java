package com.tmall.marketing.dingdingrobot.util;


import com.tmall.marketing.dingdingrobot.model.WeatherDO;

import java.util.EnumSet;
import java.util.Random;

public class EatingHelper {

    public enum Restaurant{
        MilkingD(5,"麦金地餐厅"),
        NianNianLiXing(2,"礼信年年餐厅"),
        HappyMelt(1,"乐融食堂"),
        HappyWeeked(8,"八号楼食堂"),
        HappySeven(7,"七号楼食堂");

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
                case 5:return MilkingD;
                case 8:return HappyWeeked;
                case 7:return HappySeven;
                default:return HappyMelt;
            }
        }
    }

    public static Restaurant whereToEat(WeatherDO weatherDO, boolean isAM){

        if (weatherDO==null){
            MessageHelper.sendTextMsgToXiaoDai("获取天气失败！",null);
            return calWhere2Eat(isAM);
        }
        // 如果不下雨，全随机
        if (!weatherDO.isRainOrSnow()){
            return calWhere2Eat(isAM);
        }
        // 如果下雨，鉴于鄙人带路的能力，只去就近的两个食堂吃饭
        EnumSet<Restaurant> restaurantEnumSet = EnumSet.of(Restaurant.MilkingD, Restaurant.NianNianLiXing);
        // 循环直到随机到附近的食堂
        Restaurant tmp=calWhere2Eat(isAM);
        while (!restaurantEnumSet.contains(tmp)){
            tmp=calWhere2Eat(isAM);
        }
        return tmp;


    }

    /**
     * 计算去哪个食堂吃饭
     * @return Restaurant的枚举
     */
    private static Restaurant calWhere2Eat(boolean isAM) {
        int seed = new Random().nextInt(100);
        int index;
        if (isAM) {
            index = seed<30 ? 1 : seed<60 ? 2 : seed<80 ? 5 : seed < 95 ? 8 : 7;
        } else {
            index = seed<20 ? 1 : seed<40 ? 2 : seed<70 ? 5 : seed < 90 ? 8 : 7;
        }
        return Restaurant.valueOf(index);
    }
}
