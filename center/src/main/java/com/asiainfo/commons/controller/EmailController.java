package com.asiainfo.commons.controller;

import com.asiainfo.commons.service.MailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@RestController
public class EmailController {

    //private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private MailSendService mailSendService;

    @RequestMapping("sendEmail")
    public String sendEmail() throws Exception {
        mailSendService.sendSimpleMail(new String[]{"huojg@asiainfo.com"},"这是一封测试邮件","<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>");
        return "已发送邮件";
    }

}