package com.asiainfo.workbench.message.dao;

import com.asiainfo.workbench.message.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageDao {

    List<Message> queryMessagesForPageByCondition(Map<String, Object> map);

    Long queryMessagesForCountByCondition(Map<String, Object> map);

    List<Message> queryMessagesByDate(@Param("date") String date);

}
