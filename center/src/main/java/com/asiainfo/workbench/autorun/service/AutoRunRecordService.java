package com.asiainfo.workbench.autorun.service;

import com.asiainfo.workbench.autorun.domain.AutoRunRecord;

import java.util.Map;

/**
 * 接口重跑处理接口
 * @author Administrator
 *
 */
public interface AutoRunRecordService {

	/**
	 * 根据条件查询重跑记录
	 * @param map
	 * @return
	 */
	public AutoRunRecord queryAutoRunRecordByCondition(Map<String, Object> map);

}
