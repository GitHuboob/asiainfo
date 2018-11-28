package com.asiainfo.workbench.autorun.dao;

import com.asiainfo.workbench.autorun.domain.AutoRunRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 活动持久化操作接口
 * @author Administrator
 *
 */
@Mapper
public interface AutoRunRecordDao {

    AutoRunRecord queryAutoRunRecordByCondition(Map<String, Object> map);

    int queryInterfaceRunCountByAutoRunRecord(AutoRunRecord autoRunRecord);

    void updateChannelSysJobConfigByAutoRunRecord(AutoRunRecord autoRunRecord);

}
