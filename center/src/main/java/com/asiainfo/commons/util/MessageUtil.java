package com.asiainfo.commons.util;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.webservice.SOAPEventSourceBindingStub;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.service.ContactsService;
import com.asiainfo.workbench.message.domain.Message;
import com.asiainfo.workbench.search.domain.SendInfo;
import com.asiainfo.workbench.search.service.SendInfoService;
import org.apache.axis.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class MessageUtil {

    @Autowired
    private ContactsService cs;

    @Autowired
    private SendInfoService ss;

    private static MessageUtil messageUtil;

    private static String source = CommonConstants.MESSAGE_SOURCE;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行
    public void init() {
        messageUtil = this;
        messageUtil.cs = this.cs;
        messageUtil.ss = this.ss;
    }

    public ContactsService getCs() {
        return cs;
    }
    public void setCs(ContactsService cs) {
        this.cs = cs;
    }

    public SendInfoService getSs() {
        return ss;
    }
    public void setSs(SendInfoService ss) {
        this.ss = ss;
    }

    public static String sendMessageByChannelList(List<Channel> channelList) {

        //1. 通过接口id联查接口信息, 将返回结果拼接为msg
        //2. 将组信息放入set中, 通过组信息查询用户手机，循环遍历发送短信

        String msg = "";
        String remarks = "备注";
        Set groupInfoSet = new HashSet();

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
            msg += "接口名称：" + jobName + "\n接口类型：" + workType + "\n执行时间：" + nextStartTime + "\n文件路径：" + filePath + fileName + "\n文件状态：" + remark + "\n";
        }

        System.out.println("groupInfoSet >>>>>> " + groupInfoSet);
        System.out.println("msg >>>>>> " + msg);

        List<Staff> staffList = messageUtil.cs.selectStaffListByIds(groupInfoSet);
        String[] to = new String[staffList.size()];
        for (int i = 0; i < staffList.size(); i++) {
            Staff staff = staffList.get(i);
            if(null != staff.getStaffPhone()){
                to[i] = staff.getStaffPhone();
            }
        }

        System.out.println("to >>>>>> " + Arrays.toString(to));

        String flag = "true";
        try {
            SOAPEventSourceBindingStub stub = new SOAPEventSourceBindingStub(new java.net.URL(CommonConstants.MSG_SERVICE_ADDRESS), new org.apache.axis.client.Service());
            for (int i = 0; i < to.length; i++) {
                String result = stub.recv_msg(to[i], source, msg, remarks);
                if(!"0000".equals(result)) { // 0000表示成功
                    flag = "false";
                    System.out.println(to[i] + " >>>>>> 发送短信失败！");
                }else {
                    System.out.println(to[i] + " >>>>>> 发送短信成功！");

                    SendInfo sendInfo = new SendInfo(IdGenUtil.randomLong(),null,null, to[i], msg,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    boolean res = messageUtil.ss.insert(sendInfo);
                    if(res){
                        System.out.println(to[i] + " >>>>>> 已将短信存入ES！");
                    }
                }
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String sendWarningForJobContentFillIn() {

        String msg = "请各位及时在DMP系统中完成工作内容填写！";
        String remarks = "备注";
        String[] to = new String[]{CommonConstants.MESSAGE_RECEIVER_01, CommonConstants.MESSAGE_RECEIVER_02};

        String flag = "true";
        try {
            SOAPEventSourceBindingStub stub = new SOAPEventSourceBindingStub(new java.net.URL(CommonConstants.MSG_SERVICE_ADDRESS), new org.apache.axis.client.Service());
            for (int i = 0; i < to.length; i++) {
                String result = stub.recv_msg(to[i], source, msg, remarks);

                if (!"0000".equals(result)) { // 0000表示成功
                    flag = "false";
                    System.out.println(to[i] + " >>>>>> 发送工作内容填写提醒短信失败！");
                } else {
                    System.out.println(to[i] + " >>>>>> 发送工作内容填写提醒短信成功！");
                }
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String sendMessageByMessageList(List<Message> messageList) {

        String msg = "";
        String flag = "true";
        String remarks = "备注";
        String[] to = new String[]{CommonConstants.MESSAGE_RECEIVER_02};

        if(messageList.isEmpty()){
            flag = "false";
            return flag;
        }

        for (int i = 0; i < messageList.size(); i++) {
            Message message = messageList.get(i);
            String tableName = message.getTableName();
            String createTime = message.getCreateTime();
            int repeatCount = message.getRepeatCount();
            String remark = message.getRemark();
            String orderName = message.getOrderName();

            msg += "表名：" + tableName + "\n创建时间：" + createTime + "\n重复次数：" + repeatCount + "\n订单编号：" + remark + "\n订单名称：" + orderName + "\n";
        }

        try {
            SOAPEventSourceBindingStub stub = new SOAPEventSourceBindingStub(new java.net.URL(CommonConstants.MSG_SERVICE_ADDRESS), new org.apache.axis.client.Service());
            for (int i = 0; i < to.length; i++) {
                String result = stub.recv_msg(to[i], source, msg, remarks);
                if(!"0000".equals(result)) { // 0000表示成功
                    flag = "false";
                    System.out.println(to[i] + " >>>>>> 发送表重复信息短信失败！");
                }else {
                    System.out.println(to[i] + " >>>>>> 发送表重复信息短信成功！");
                }
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
