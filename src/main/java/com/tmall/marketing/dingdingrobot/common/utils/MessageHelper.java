package com.tmall.marketing.dingdingrobot.common.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.tmall.marketing.dingdingrobot.common.CommonFields;
import com.tmall.marketing.dingdingrobot.common.ResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MessageHelper {

    public static final DingTalkClient DAI_CLIENT = new DefaultDingTalkClient(CommonFields.OAIP_ADDRESS);
    public static final DingTalkClient DAI4PJ_CLIENT = new DefaultDingTalkClient(CommonFields.POWERJOB_ADDRESS);

    @Data
    @AllArgsConstructor
    public static class MarkDownEntity{
        private String title;
        private String detail;
    }

    /**
     * 让小呆发送简单文本消息
     * @param msg 需要发送的消息
     * @param atList @人的名单，用逗号隔开，如12345678900,12345678901
     * @return 发送结果
     */
    public static ResultDTO<String> sendTextMsg(String msg, String atList, DingTalkClient client) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(msg);
        request.setText(text);

        // 设置需要被@的人
        if (!StringUtils.isEmpty(atList)){
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            String[] atListArr=atList.split(",");
            List<String> list= Arrays.stream(atListArr).collect(Collectors.toList());
            at.setAtMobiles(list);
            request.setAt(at);
        }

        OapiRobotSendResponse response;

        try {
            response = client.execute(request);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultDTO.failed(e.getMessage());
        }

        if (response.isSuccess()) {
            return ResultDTO.success(response.getMessage());
        }
        else {
            log.error(response.getErrmsg());
            return ResultDTO.failed(response.getErrmsg());
        }
    }

    /**
     * 让小呆发送MarkDown格式的信息
     * @param title 大标题
     * @param entities 小标题+内容
     * @return 结果
     */
    public static ResultDTO<String> sendMarkDownMsg(String title, List<MarkDownEntity> entities, DingTalkClient client) {

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        StringBuilder mdBuilder=new StringBuilder();
        mdBuilder.append("## ").append(title).append("\n");
        for (MarkDownEntity entity:entities){
            mdBuilder.append("#### ").append(entity.getTitle()).append("\n");
            mdBuilder.append("> ").append(entity.getDetail()).append("\n\n");
        }
        markdown.setText(mdBuilder.toString());
        request.setMarkdown(markdown);
        OapiRobotSendResponse response;
        try {
            response = client.execute(request);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultDTO.failed(e.getMessage());
        }

        return ResultDTO.success(response.getMessage());
    }

}
