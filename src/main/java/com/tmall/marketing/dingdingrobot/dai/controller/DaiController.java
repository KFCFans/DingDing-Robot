package com.tmall.marketing.dingdingrobot.dai.controller;

import com.tmall.marketing.dingdingrobot.common.ResultDTO;
import com.tmall.marketing.dingdingrobot.common.utils.MessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dai")
public class DaiController {

    @GetMapping("/msg")
    public ResultDTO<String> sendSimpleMsg(String msg,String atList){
        ResultDTO<String> res;
        try {
            res = MessageHelper.sendTextMsg(msg, atList, MessageHelper.DAI_CLIENT);
        }catch (Exception e){
            res=ResultDTO.failed(e.getMessage());
        }
        return res;
    }
}
