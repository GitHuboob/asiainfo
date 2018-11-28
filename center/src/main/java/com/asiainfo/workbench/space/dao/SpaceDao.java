package com.asiainfo.workbench.space.dao;

import com.asiainfo.workbench.space.domain.Space;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表空间持久化处理接口
 * @author Administrator
 *
 */

@Mapper
public interface SpaceDao {

    public List<Space> querySpaceInfoByPeriod();

    public int insertSpaceByList(@Param(value = "list") List<Space> paramList);

}
