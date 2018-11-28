package com.asiainfo.commons.constant;

/**
 * 通用常量类
 */
public final class CommonConstants {

    private CommonConstants(){}

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public static final String FILE_STATE_1 = "1"; //文件状态：1文件存在
    public static final String FILE_STATE_2 = "2"; //文件状态：2文件不存在且未超时
    public static final String FILE_STATE_3 = "3"; //文件状态：3文件不存在且已超时

    public static final String DB_ERROR_CODE = "100"; //数据库操作异常

    public static final String MSG_SERVICE_ADDRESS = "http://10.4.44.239:8815/recv_msg/recv_msg"; //短信远程服务接口

    public static final String EMAIL_SENDER = "m17600908262@163.com";
    public static final String MESSAGE_SOURCE = "10086";

    public static final Long PAGE_NUMBER = 1L; //页码
    public static final Integer PAGE_SIZE = 5; //每页显示条数

    public static final String EMAIL_RECEIVER_01 = "huojg@asiainfo.com"; //接收表空间提醒邮件
    public static final String EMAIL_RECEIVER_02 = "jianglei3@asiainfo.com"; //接收表空间提醒邮件

    public static final String ALL_EMAIL_RECEIVER = "bj-channel-support@asiainfo.com"; //工作内容填写提醒邮件

    public static final String MESSAGE_RECEIVER_01 = "18233108262"; //工作内容填写提醒短信
    public static final String MESSAGE_RECEIVER_02 = "15022100491"; //工作内容填写提醒短信

}