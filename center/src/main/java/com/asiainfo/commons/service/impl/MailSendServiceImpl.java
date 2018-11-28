package com.asiainfo.commons.service.impl;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.service.MailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Set;

@Service
public class MailSendServiceImpl implements MailSendService {

    @Autowired
    private JavaMailSender mailSender;

    //@Value("spring.mail.username")
    private static final String sender = CommonConstants.EMAIL_SENDER;

    @Override
    public void sendSimpleMail(String[] to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            System.out.println("文本格式邮件发送成功");
        }catch (Exception e){
            System.out.println("文本格式邮件发送失败");
        }
    }

    @Override
    public void sendHtmlMail(String[] to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart Message
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            System.out.println("html格式邮件发送成功");
        }catch (Exception e){
            System.out.println("html格式邮件发送失败");
        }
    }

    @Override
    public void sendAttachmentsMail(String[] to, String subject, String content, Map<String, File> files) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            //注意项目路径问题，自动补用项目路径
            //FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/picture.jpg"));
            //加入邮件
            //helper.addAttachment("图片.jpg", file);
            Set<Map.Entry<String,File>> fileSet = files.entrySet();
            for (Map.Entry f : fileSet) {
                helper.addAttachment((String) f.getKey(), (File) f.getValue());
            }
            mailSender.send(message);
            System.out.println("带附件的邮件发送成功");
        }catch (Exception e){
            System.out.println("带附件的邮件发送失败");
        }
    }

    @Override
    public void sendInlineMail(String[] to, String subject, String html, Map<String, File> files) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            //第二个参数指定发送的是HTML格式,同时cid:是固定的写法
            //"<html><body>带静态资源的邮件内容 图片:<img src='cid:picture' /></body></html>"
            helper.setText(html, true);
            Set<Map.Entry<String,File>> fileSet = files.entrySet();
            for (Map.Entry f : fileSet) {
                helper.addInline((String) f.getKey(), (File) f.getValue());
            }
            //FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/image/picture.jpg"));
            //helper.addInline("picture",file);
            mailSender.send(message);
            System.out.println("带静态资源的邮件发送成功");
        }catch (Exception e){
            System.out.println("带静态资源的邮件发送失败");
        }
    }

}
