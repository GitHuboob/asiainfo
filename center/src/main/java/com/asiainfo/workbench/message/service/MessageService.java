package com.asiainfo.workbench.message.service;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.message.domain.Message;

import java.util.List;
import java.util.Map;

public interface MessageService {

    public PaginationVO<Message> queryMessagesForPageByCondition(Map<String, Object> map);

    List<Message> queryMessagesByDate(String date);
}
