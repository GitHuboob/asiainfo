package com.asiainfo.workbench.space.service;

import com.asiainfo.workbench.space.domain.Space;

import java.util.List;

public interface SpaceService {

    public List<Space> querySpaceInfoByPeriod();

    public int insertSpaceByList(List<Space> paramList);

}
