package com.asiainfo.commons.controller;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.ResponseInfo;
import com.asiainfo.commons.util.DateUtil;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.activity.domain.ChannelInfo;
import com.asiainfo.workbench.activity.service.ActivityService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class FileStatusMonitorController extends HttpServlet {

    @Autowired
    private ActivityService activityService;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(FileStatusMonitorController.class);

    /**
     * 下载当前时间的文件监控数据
     */
    @RequestMapping(method=RequestMethod.GET,value="monitor/downloadMonitorDatas/{ip:.+}")
    public Object downloadMonitorDatas(HttpServletRequest request, @PathVariable("ip") String ip) throws Exception{
        System.out.println("ip >>>>>> "+ip);

        String todayDate = DateUtil.getSystemTime("yyyyMMdd");
        //List<Channel> channels = activityService.getChannelByParams(todayDate, ip);
        List<Channel> channels = activityService.getHistoryChannelByParams(todayDate, ip);

        //将最大发送次数map放入session中
        /*Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            String jobName = channel.getJobName();
            int maxSendCount = Integer.valueOf(channel.getMaxSendCount());
            map.put(jobName, maxSendCount);
        }
        System.out.println("map >>>>>> " + map);
        request.getServletContext().setAttribute("channelsMap", map);*/

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setChannels(channels);
        channelInfo.setIp(ip);
        return channelInfo;
    }

    /**
     * 下载指定时间的文件监控数据
     */
    @RequestMapping(method=RequestMethod.GET,value="monitor/downloadMonitorDatas/{ip}/{date}")
    public Object downloadMonitorDatas(@PathVariable("ip") String ip, @PathVariable("date") String date){
        System.out.println("ip >>>>>> "+ip);
        System.out.println("date >>>>>> "+date);

        return null;
    }

    /**
     * 上传文件监控数据
     */
    @RequestMapping(method=RequestMethod.POST,value="monitor/uploadMonitorDatas")
    public Object uploadMonitorDatas2(@RequestBody ChannelInfo channelInfo) {
        System.out.println("上传监控数据 channelInfo >>>>>> "+ channelInfo.toString());

        /*try{*/
            List<Channel> channels = channelInfo.getChannels();
            activityService.updateChannelByList(channels);
        /*} catch (Exception e) {
            logger.info("数据库操作异常!");
            throw new CommonException(CommonConstants.DB_ERROR_CODE, "数据库操作异常");
        }*/

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(CommonConstants.SUCCESS);
        responseInfo.setMessage("成功");
        return responseInfo;
    }

}
