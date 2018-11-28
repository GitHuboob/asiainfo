package com.asiainfo.workbench.space.service.impl;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.contacts.domain.Group;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.domain.StaffGroupRelation;
import com.asiainfo.workbench.space.dao.SpaceDao;
import com.asiainfo.workbench.space.domain.Space;
import com.asiainfo.workbench.space.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 表空间业务处理类
 * @author Administrator
 *
 */

@Service
public class SpaceServiceImpl implements SpaceService {

	@Autowired
	private SpaceDao spaceDao;

	@Override
	public List<Space> querySpaceInfoByPeriod() {
		return spaceDao.querySpaceInfoByPeriod();
	}

	@Override
	@Transactional
	public int insertSpaceByList(List<Space> paramList) {
		return spaceDao.insertSpaceByList(paramList);
	}
}
