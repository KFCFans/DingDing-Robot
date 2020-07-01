package com.tmall.marketing.dingdingrobot.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置中心
 *
 * @author tjq
 * @since 2020/7/1
 */
@Slf4j
@Service
public class ConfigCenter {

    public static String powerjobAppName;
    public static String powerjobBaseURL;

    public ConfigCenter() throws Exception {
        load0();
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void load() {
        try {
            load0();
        }catch (Exception e) {
            log.error("[ConfigCenter] load dai.properties failed", e);
        }
    }

    private void load0() throws IOException {
        try (InputStream is = ConfigCenter.class.getClassLoader().getResourceAsStream("dai.properties")) {

            if (is == null) {
                throw new RuntimeException("can't find dai.properties");
            }

            Properties properties = new Properties();
            properties.load(is);

            powerjobBaseURL = properties.getProperty("powerjob.base.url");
            powerjobAppName = properties.getProperty("powerjob.try.appname");
        }
    }

}
