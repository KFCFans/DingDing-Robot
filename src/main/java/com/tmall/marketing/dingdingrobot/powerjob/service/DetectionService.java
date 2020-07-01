package com.tmall.marketing.dingdingrobot.powerjob.service;

import com.google.common.collect.Lists;
import com.tmall.marketing.dingdingrobot.common.ConfigCenter;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 检测 powerjob-server 是否存活
 *
 * @author tjq
 * @since 2020/7/1
 */
@Slf4j
@Service
public class DetectionService {

    private static OkHttpClient okHttpClient=new OkHttpClient();

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void detective() {
        try {
            detective0();
        }catch (Exception e) {
            log.error("[DetectionService] detective failed.", e);
        }
    }

    private void detective0() throws Exception {

        List<MessageHelper.MarkDownEntity> contents = Lists.newLinkedList();

        String url = String.format("http://%s/server/hello", ConfigCenter.powerjobBaseURL);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return;
            }
            contents.add(new MessageHelper.MarkDownEntity("状态码", String.valueOf(response.code())));
        }catch (Exception e) {
            contents.add(new MessageHelper.MarkDownEntity("异常信息", e.toString()));
        }

        log.warn("[DetectionService] can't request {}, maybe server has down.", url);

        // 发送警报
        String title = "PowerJob 服务器宕机警报";
        contents.add(new MessageHelper.MarkDownEntity("请求地址", url));

        MessageHelper.sendMarkDownMsgToXiaoDai(title, contents, MessageHelper.DAI4PJ_CLIENT);
    }
}
