package com.tmall.marketing.dingdingrobot.controller;

import com.tmall.marketing.dingdingrobot.model.ResultDTO;
import com.tmall.marketing.dingdingrobot.util.MessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleMessageController {

    @RequestMapping("/msg")
    public ResultDTO<String> sendSimpleMsg(String msg,String atList){
        ResultDTO<String> res;
        try {
            res = MessageHelper.sendTextMsgToXiaoDai(msg, atList, MessageHelper.DAI_CLIENT);
        }catch (Exception e){
            res=ResultDTO.failed(e.getMessage());
        }
        return res;
    }
}
