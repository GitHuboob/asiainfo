package com.asiainfo.commons.schedule;

import com.asiainfo.commons.constant.GlobalVariables;
import com.asiainfo.commons.util.DateUtil;
import com.asiainfo.commons.util.EmailUtil;
import com.asiainfo.commons.util.MessageUtil;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.activity.service.ActivityService;
import com.asiainfo.workbench.message.domain.Message;
import com.asiainfo.workbench.message.service.MessageService;
import com.asiainfo.workbench.space.domain.Space;
import com.asiainfo.workbench.space.service.SpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Spring Task定时任务
 */
@Slf4j
@Component
public class ScheduledService {

    private static ScheduledService scheduledService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MessageService messageService;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行
    public void init() {
        scheduledService = this;
        scheduledService.spaceService = this.spaceService;
        scheduledService.activityService = this.activityService;
        scheduledService.messageService = this.messageService;
    }

    public SpaceService getSpaceService() {
        return spaceService;
    }
    public void setSpaceService(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * cron：通过表达式来配置任务执行时间
     * 每天凌晨执行
     */
    @Scheduled(cron = "1 0 0 * * *")
    public void scheduled(){
        String todayDate = DateUtil.getSystemTime("yyyyMMdd");
        //List<Channel> channels = scheduledService.activityService.getChannelByParams(todayDate, null);
        //scheduledService.activityService.updateChannelByList(channels);
        List<Channel> channels = scheduledService.activityService.getChannelByScheduled(todayDate, null);
        scheduledService.activityService.insertChannelByList(channels);
        System.out.println(DateUtil.getSystemTime() + " 初始化channels历史表 >>>>>> " + channels);

        //将最大发送次数放入全局变量中
        GlobalVariables.channelsMap.clear();
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            String jobName = channel.getJobName();
            int maxSendCount = Integer.valueOf(channel.getMaxSendCount());
            GlobalVariables.channelsMap.put(jobName, maxSendCount);
        }
        System.out.println(DateUtil.getSystemTime() + " 初始化channelsMap发送次数 >>>>>> " + GlobalVariables.channelsMap);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void scheduled0(){
        List<Space> spaceList = scheduledService.spaceService.querySpaceInfoByPeriod();
        EmailUtil.sendEmailBySpaceList(spaceList);
        System.out.println("spaceList >>>>>> " + spaceList);
    }

    /**
     * 发送工作内容填写提醒(每月20、25号上午10点)
     */
    @Scheduled(cron = "0 0 9 20 * *")
    public void scheduled1(){
        //EmailUtil.sendWarningForJobContentFillIn();
        MessageUtil.sendWarningForJobContentFillIn();
    }

    /**
     * 发送表信息提醒(每天上午9点)
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void scheduled2(){
        String todayDate = DateUtil.getSystemTime("yyyyMMdd");
        List<Message> messageList = scheduledService.messageService.queryMessagesByDate(todayDate);
        MessageUtil.sendMessageByMessageList(messageList);
    }


    /**
     * fixedRate：定义一个按一定频率执行的定时任务(当前为10分钟)
     */
    @Scheduled(fixedRate = 1000*60*10)
    public void scheduled3() {
        System.out.println("系统正在运行 >>>>>> "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    /**
     * fixedDelay：定义一个按一定频率执行的定时任务(当前为10分钟）
     * 与上面不同的是，改属性可以配合initialDelay，定义该任务延迟执行时间
     */
    @Scheduled(fixedDelay = 1000*60*10)
    public void scheduled4() {

    }

}