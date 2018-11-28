package com.asiainfo.commons.service;

import java.io.File;
import java.util.Map;

public interface MailSendService {

    /**
     * 发送简单文本邮件
     * @param to 收件人，一组，不能为空
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws Exception
     */
    void sendSimpleMail(String[] to, String subject, String content) throws Exception;

    /**
     * 发送html格式的邮件
     * @param to 收件人，一组，不能为空
     * @param subject 邮件主题
     * @param content 邮件内容(html格式)
     */
    void sendHtmlMail(String[] to,String subject,String content) throws Exception;

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param files 附件，key(请将文件的扩展名也加上，否则无法预览。如：picture.jpg)
     * @throws Exception
     */
    void sendAttachmentsMail(String[] to, String subject, String content, Map<String, File> files) throws Exception;

    /**
     * 发送带静态资源的邮件<br />
     * 将文件内容显示在邮件内容中，一般为图片，如果为文件，有待测试
     * @param to 收件人
     * @param subject 邮件主题
     * @param html html文本
     * @param files 要加入html中的静态资源<br />
     * example: <br />
     * html: "带静态资源的邮件内容 图片:img src='cid:dog'    img src='cid:pig'"<br />
     * files: [{key:dog, value: dog.jpg},{key:pig, value:pig.jpg}]<br />
     * files中的key要和html中的cid一一对应。
     * @throws Exception
     */
    void sendInlineMail(String[] to, String subject, String html, Map<String, File> files) throws Exception;

}
