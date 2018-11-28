package com.asiainfo.workbench.message.service.impl;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.message.dao.MessageDao;
import com.asiainfo.workbench.message.domain.Message;
import com.asiainfo.workbench.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public PaginationVO<Message> queryMessagesForPageByCondition(Map<String, Object> map) {
        List<Message> messageList = messageDao.queryMessagesForPageByCondition(map);
        long count = messageDao.queryMessagesForCountByCondition(map);

        PaginationVO<Message> vo = new PaginationVO<Message>();
        vo.setDataList(messageList);
        vo.setCount(count);
        return vo;
    }

    @Override
    public List<Message> queryMessagesByDate(String date) {
        return messageDao.queryMessagesByDate(date);
    }

}
