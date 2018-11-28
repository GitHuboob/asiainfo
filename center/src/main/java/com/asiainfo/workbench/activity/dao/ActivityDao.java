package com.asiainfo.workbench.activity.dao;

import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.contacts.domain.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 活动持久化操作接口
 * @author Administrator
 *
 */
@Mapper
public interface ActivityDao {


    List<Channel> getChannelList();

    List<Channel> getChannelByDate(String date);

    int getChannelCountByParams(Channel channel);

    List<Channel> getChannelByParams(@Param("date") String date, @Param("ip") String ip);

    List<Channel> getHistoryChannelByParams(@Param("date") String date, @Param("ip") String ip);

    void insertChannelByList(@Param(value = "list") List<Channel> paramList);

    void updateHistoryChannelByList(@Param(value = "list") List<Channel> paramList);

    List<Channel> queryChannelForPageByCondition(Map<String, Object> map);

	List<Channel> queryChannelByAutoComplate(Map<String, Object> map);

    long queryCountOfChannelByCondition(Map<String, Object> map);

    int queryActivityForId();

	/**
	 * 保存创建的活动
	 * @param activity
	 * @return
	 */
	public int saveCreateMarketActivity(Channel activity);
	/**
	 * 根据条件分页查询活动列表
	 * @param map
	 * @return
	 */
	public List<Channel> queryMarketActivityForPageByCondition(Map<String, Object> map);
	/**
	 * 根据条件查询活动总条数
	 * @param map
	 * @return
	 */
	public long queryCountOfMarketActivityByCondition(Map<String, Object> map);
	/**
	 * 根据ids数组删除活动
	 */
	public int deleteMarketActivityByIds(String[] ids);

	public List<Channel> selectMarketActivityListByIds(String[] ids);
	/**
	 * 根据单个id查询单个活动
	 */
	public Channel queryMarketActivityById(String id);
	/**
	 * 根据单个id更新单个活动
	 */
	public int SaveEditMarketActivityById(Channel activity);
	/**
	 * 根据id查询活动详细信息
	 */
	public Channel queryMarketActivityDetailById(String id);
	/**
	 * 根据条件查询所有活动列表
	 * @param map
	 * @return
	 */
	public List<Channel> queryMarketActivityByCondition(Map<String, Object> map);
	/**
	 * 批量保存创建的活动
	 * @param list
	 * @return
	 */
	public int saveCreateMarketActivityByList(List<Channel> activityList);
	/**
	 * 根据clueId查询相关联的活动
	 */
	public List<Channel> queryMarketActiviytByClueId(String clueId);
	/**
	 * 根据name模糊查询与clueId关联的活动
	 */
	public List<Channel> queryMarketActivityByNameClueId(Map<String, Object> map);
	/**
	 * 根据ids数组查询活动
	 */
	public List<Channel> queryMarketActivityByIds(String[] ids);

}
