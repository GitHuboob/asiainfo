package com.asiainfo.commons.util;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.StatusInfo;
import com.asiainfo.commons.service.MailSendService;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.service.ContactsService;
import com.asiainfo.workbench.space.domain.Space;
import com.asiainfo.workbench.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
public class EmailUtil {

    @Autowired
    private ContactsService cs;

    @Autowired
    private SpaceService ss;

    @Autowired
    private MailSendService mailSendService;

    private static EmailUtil emailUtil;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行
    public void init() {
        emailUtil = this;
        emailUtil.cs = this.cs;
        emailUtil.ss = this.ss;
        emailUtil.mailSendService = this.mailSendService;
    }

    public ContactsService getCs() {
        return cs;
    }
    public void setCs(ContactsService cs) {
        this.cs = cs;
    }

    public SpaceService getSs() {
        return ss;
    }
    public void setSs(SpaceService ss) {
        this.ss = ss;
    }

    public MailSendService getMailSendService() {
        return mailSendService;
    }
    public void setMailSendService(MailSendService mailSendService) {
        this.mailSendService = mailSendService;
    }

    public static String sendEmailByChannelList(List<Channel> channelList) {

        //1. 通过接口id联查接口信息，将返回结果拼接为content
        //2. 将组信息放入set中，通过组信息查询用户邮箱，将返回结果封装到字符串数组to中

        String subject = "这是一封接口文件提醒邮件";
        Set groupInfoSet = new HashSet();

        StringBuffer content = new StringBuffer();
        content.append("<html>")
                .append("<body>")
                .append("<table border='2' width='100%' cellpadding='5' cellspacing='0'>")
                .append("<tr><td colspan='5' align='center'>接口文件信息</td></tr>")
                .append("<tr><td width='10%'>接口名称</td><td width='10%'>接口类型</td><td width='10%'>执行时间</td><td width='50%'>文件路径</td><td width='20%'>备注</td></tr>");

        for (int i = 0; i < channelList.size(); i++) {
            Channel channel = channelList.get(i);
            String jobName = channel.getJobName();
            String workType = channel.getWorkType();
            String nextStartTime = channel.getNextStartTime();
            String groupCode = channel.getGroupCode();
            String filePath = channel.getFilePath();
            String fileName = channel.getFileName();
            String remark = channel.getRemark();

            groupInfoSet.add(groupCode);
            content.append("<tr><td width='10%'>" + jobName + "</td><td width='10%'>" + workType + "</td><td width='10%'>" + nextStartTime + "</td><td width='50%'>" + filePath + fileName + "</td><td width='20%'>"+remark+"</td></tr>");
        }
		
		content.append("</body>")
                .append("</html>");		

        System.out.println("groupInfoSet >>>>>> " + groupInfoSet);
        System.out.println("content >>>>>> " + content);

        List<Staff> staffList = emailUtil.cs.selectStaffListByIds(groupInfoSet);
        String[] to = new String[staffList.size()];
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);
            to[i] = staff.getStaffEmail();
        }

        System.out.println("to >>>>>> " + Arrays.toString(to));

        String flag = "true";
        try {
            //emailUtil.mailSendService.sendSimpleMail(to, subject, content);
            emailUtil.mailSendService.sendHtmlMail(to, subject, content.toString());
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送接口文件提醒邮件成功！");
        } catch (Exception e) {
            flag = "false";
            e.printStackTrace();
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送接口文件提醒邮件失败！");
        }
        return flag;
    }

    public static Boolean sendEmailBySpaceList(List<Space> spaceList) {

        Boolean flag = false;
        String[] to = new String[]{CommonConstants.EMAIL_RECEIVER_02};
        String subject = "这是一封表空间提醒邮件";

        StringBuffer content = new StringBuffer();
        content.append("<html>")
                .append("<body>")
                .append("<table border='2' width='100%' cellpadding='5' cellspacing='0'>")
                .append("<tr><td colspan='8' align='center'>表空间信息</td></tr>")
                .append("<tr><td>表空间名称</td><td>表空间总大小(GB)</td><td>空闲大小(GB)</td><td>占用大小(GB)</td>" +
                        "<td>空闲百分比</td><td>占用百分比</td><td>最大扩展空间(GB)</td><td>当前时间</td></tr>");


        List<Space> insertList = new ArrayList<Space>();
        for (int i = 0; i < spaceList.size(); i++) {
            Space space = spaceList.get(i);

            String spaceName = space.getSpaceName();
            String megsAlloc = space.getMegsAlloc();
            String megsFree = space.getMegsFree();
            String megsUsed = space.getMegsUsed();
            String pctFree = space.getPctFree();
            String pctUsed = space.getPctUsed();
            String extendMax = space.getExtendMax();
            String createTime = space.getCreateTime();

            if(Double.valueOf(pctUsed) >= 85){
                flag = true;
                insertList.add(space);
                content.append("<tr><td>" + spaceName + "</td><td>" + megsAlloc + "</td><td>" + megsFree + "</td><td>" + megsUsed + "</td>" +
                        "<td>" + pctFree + "</td><td>" + pctUsed + "</td><td>" + extendMax + "</td><td>" + createTime + "</td></tr>");
            }
        }

        content.append("</body>")
                .append("</html>");

        System.out.println("content >>>>>> " + content);

        if (!flag) {
            return flag;
        }

        int i = emailUtil.ss.insertSpaceByList(insertList);
        System.out.println(" >>>>>> 成功插入" + i + "条记录");

        try {
            emailUtil.mailSendService.sendHtmlMail(to, subject, content.toString());
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送表空间提醒邮件成功！");
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送表空间提醒邮件失败！");
        }
        return flag;
    }

    public static Boolean sendEmailByStatusInfoList(List<StatusInfo> statusInfoList) {

        Boolean flag = true;
        String[] to = new String[]{CommonConstants.EMAIL_RECEIVER_02};
        String subject = "这是一封进程状态提醒邮件";

        StringBuffer content = new StringBuffer();
        content.append("<html>")
                .append("<body>")
                .append("<table border='2' width='100%' cellpadding='3' cellspacing='0'>")
                .append("<tr><td colspan='3' align='center'>表空间信息</td></tr>")
                .append("<tr><td>进程名称</td><td>进程状态</td><td>当前时间</td></tr>");

        for (int i = 0; i < statusInfoList.size(); i++) {
            StatusInfo statusInfo = statusInfoList.get(i);

            String interfaceName = statusInfo.getInterfaceName();
            String status = statusInfo.getStatus();
            String systemTime = DateUtil.getSystemTime();
            content.append("<tr><td>" + interfaceName + "</td><td>" + status + "</td><td>" + systemTime + "</td></tr>");
        }

        content.append("</body>")
                .append("</html>");

        System.out.println("content >>>>>> " + content);

        try {
            emailUtil.mailSendService.sendHtmlMail(to, subject, content.toString());
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送进程状态提醒成功！");
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送进程状态提醒失败！");
        }
        return flag;
    }

    public static Boolean sendWarningForJobContentFillIn() {

        Boolean flag = true;
        String[] to = new String[]{CommonConstants.ALL_EMAIL_RECEIVER};
        String subject = "请各位及时在DMP系统中完成工作内容填写";

        StringBuffer html = new StringBuffer();

        html.append("<html>");
        html.append("<body>");
        html.append("各位好：");
        html.append("<p>");
        html.append("请各位于月底前登陆DMP系统填写本月的工作内容 DMP：http://112.25.233.117:58080/OnlineServer/LoginAction.action");
        html.append("<br><br>");
        html.append("用户名初始密码为NT同账号名和密码，请各位尽快完成填写。");
        html.append("</p>");
        html.append("<img src='cid:dmp'/>");
        html.append("</body>");
        html.append("</html>");

        System.out.println("html >>>>>> " + html);

        try {
            Map<String, File> files = new HashMap<String, File>();
            //File file = new File("F:\\monitoringCenter\\center\\src\\main\\resources\\dmp.jpg");
            Resource resource = new ClassPathResource("dmp.jpg");
            File file = resource.getFile();
            files.put("dmp", file);

            emailUtil.mailSendService.sendInlineMail(to, subject, html.toString(), files);
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送工作内容填写提醒成功！");
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            //System.out.println(Arrays.toString(to) + " >>>>>> 发送工作内容填写提醒失败！");
        }
        return flag;
    }
}
