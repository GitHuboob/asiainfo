package com.asiainfo.workbench.autorun.service.impl;

import com.asiainfo.workbench.autorun.dao.AutoRunRecordDao;
import com.asiainfo.workbench.autorun.domain.AutoRunRecord;
import com.asiainfo.workbench.autorun.service.AutoRunRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 接口重跑业务处理类
 * @author Administrator
 *
 */
@Service
public class AutoRunRecordServiceImpl implements AutoRunRecordService {

	@Autowired
	private AutoRunRecordDao dao;

	@Override
	public AutoRunRecord queryAutoRunRecordByCondition(Map<String, Object> map) {
		return dao.queryAutoRunRecordByCondition(map);
	}

}
