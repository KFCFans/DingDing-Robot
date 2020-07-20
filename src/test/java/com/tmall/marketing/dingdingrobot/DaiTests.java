package com.tmall.marketing.dingdingrobot;

import com.tmall.marketing.dingdingrobot.dai.model.Poetry;
import com.tmall.marketing.dingdingrobot.dai.service.PoetryService;
import org.junit.Test;

/**
 * 小呆测试
 *
 * @author tjq
 * @since 2020/7/21
 */
public class DaiTests {

    @Test
    public void testPoetry() {
        Poetry poetry = PoetryService.fetchPoetry();
        System.out.println(poetry);
        System.out.println(poetry.mini());
    }

}
