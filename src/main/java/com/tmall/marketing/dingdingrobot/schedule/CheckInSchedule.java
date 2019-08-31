package com.tmall.marketing.dingdingrobot.schedule;

import com.tmall.marketing.dingdingrobot.util.CommonFields;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@Component
public class CheckInSchedule {

    @Scheduled(cron = "0 0 10 * * ? ")
    public void autoCheckIn(){
        try {
            checkInBookWebSite();
        }catch (Exception e){
            log.error("[CheckInSchedule]", e);
        }
    }

    private static void checkInBookWebSite() throws Exception{
        long time = System.currentTimeMillis() / 1000;
        String testCookie = "Hm_lvt_f4ed6be84f004238df9c881b5ec26c29=" +time+"; gzepvmlusername=Lwnhxw; gzepvmluserid=460399; gzepvmlgroupid=1; gzepvmlrnd=ojkzd8aAWMEoEmpaTBxO; gzepvmlauth=39923c27d7103510e43bca74f0a8fe45; Hm_lpvt_f4ed6be84f004238df9c881b5ec26c29="+time;

        String urlPath = "http://www.iamtxt.com/e/extend/signin.php";
        String cookie = String.format(testCookie, time, time);
        URL url = new URL(urlPath);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Cookie", cookie);
        conn.setDoInput(true);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        MessageHelper.sendTextMsgToXiaoDai(sb.toString(), null);

    }

    public static void main(String[] args) {
        try {
            checkInBookWebSite();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
