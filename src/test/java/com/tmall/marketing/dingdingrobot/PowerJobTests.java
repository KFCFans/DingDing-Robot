package com.tmall.marketing.dingdingrobot;

import com.tmall.marketing.dingdingrobot.powerjob.service.DetectionService;
import com.tmall.marketing.dingdingrobot.powerjob.service.ResetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * PowerJob 相关测试
 *
 * @author tjq
 * @since 2020/7/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PowerJobTests {

    @Resource
    private ResetService resetService;
    @Resource
    private DetectionService detectionService;

    @Test
    public void testDetectionService() {
        detectionService.detective();
    }

    @Test
    public void testResetAll() {
        resetService.resetAll();
    }

}
