package com.asiainfo.workbench.activity.service;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.activity.domain.Channel;

import java.util.List;
import java.util.Map;

/**
 * 活动业务处理接口
 * @author Administrator
 *
 */
public interface ActivityService {


	public abstract List<Channel> getChannelList();

	public abstract List<Channel> getChannelByDate(String date);

	public abstract List<Channel> getChannelByScheduled(String date, String ip);

    public abstract void insertChannelByList(List<Channel> paramList);

	public abstract List<Channel> getChannelByParams(String date, String ip);

	public abstract List<Channel> getHistoryChannelByParams(String date, String ip);

	public abstract void updateChannelByList(List<Channel> paramList);

	public PaginationVO<Channel> queryChannelForPageByCondition(Map<String, Object> map);

	public PaginationVO<Channel> queryChannelByAutoComplate(Map<String, Object> map);

	public int queryActivityForId();

	public Channel queryActivityById(String id);

	public int saveCreateMarketActivity(Channel activity);

	public PaginationVO<Channel> queryMarketActivityForPageByCondition(Map<String, Object> map);

	public int deleteMarketActivityByIds(String[] ids);

	public Channel queryMarketActivityById(String id);

	public int SaveEditMarketActivityById(Channel activity);

	public Channel queryMarketActivityDetailById(String id);

	public List<Channel> queryMarketActivityByCondition(Map<String, Object> map);

	public int saveCreateMarketActivityByList(List<Channel> activityList);

	public List<Channel> queryMarketActiviytByClueId(String clueId);

	public List<Channel> queryMarketActivityByNameClueId(Map<String, Object> map);

	public List<Channel> queryMarketActivityByIds(String[] ids);

	public List<Channel> selectMarketActivityListByIds(String[] ids);

}
