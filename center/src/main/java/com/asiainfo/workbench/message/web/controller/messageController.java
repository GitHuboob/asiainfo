package com.asiainfo.workbench.message.web.controller;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.message.domain.Message;
import com.asiainfo.workbench.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class messageController {

    @Autowired
    private MessageService ms;

    @RequestMapping("/workbench/messages/queryMessageForPageByCondition")
    @ResponseBody
    public Object queryMessageForPageByConditionController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", request.getParameter("tableName"));
        map.put("createTime", request.getParameter("createTime"));

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        long pageNo = CommonConstants.PAGE_NUMBER;
        if (pageNoStr != null && pageNoStr.length() > 0) {
            pageNo = Long.parseLong(pageNoStr.trim());
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr.trim());
        }

        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        PaginationVO<Message> vo = ms.queryMessagesForPageByCondition(map);
        return vo;
    }

}
