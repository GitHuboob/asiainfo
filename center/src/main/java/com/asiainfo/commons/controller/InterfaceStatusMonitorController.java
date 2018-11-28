package com.asiainfo.commons.controller;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.ResponseInfo;
import com.asiainfo.commons.model.StatusInfo;
import com.asiainfo.commons.util.EmailUtil;
import com.asiainfo.workbench.activity.service.ActivityService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InterfaceStatusMonitorController extends HttpServlet {

    @Autowired
    private ActivityService activityService;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(InterfaceStatusMonitorController.class);

    /**
     * 上传接口状态监控数据
     */
    @RequestMapping(method=RequestMethod.POST,value="monitor/uploadInterfaceStatusDatas")
    public Object uploadInterfaceStatusMonitorDatas(@RequestBody StatusInfo statusInfo) {
        //System.out.println("statusInfo >>>>>> "+ statusInfo.toString());

        List<StatusInfo> statusInfoList = new ArrayList<>();
        String status = statusInfo.getStatus();
        if("DOWN".equalsIgnoreCase(status)){
            statusInfoList.add(statusInfo);
            //System.out.println("statusInfoList >>>>>> " + statusInfoList);
            EmailUtil.sendEmailByStatusInfoList(statusInfoList);
        }

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(CommonConstants.SUCCESS);
        responseInfo.setMessage("已接收到信息！");
        return responseInfo;
    }

}
