package com.asiainfo.workbench.activity.service.impl;

import com.asiainfo.commons.constant.GlobalVariables;
import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.commons.util.DateUtil;
import com.asiainfo.commons.util.EmailUtil;
import com.asiainfo.commons.util.MessageUtil;
import com.asiainfo.workbench.activity.dao.ActivityDao;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.activity.service.ActivityService;
import com.asiainfo.workbench.autorun.dao.AutoRunRecordDao;
import com.asiainfo.workbench.autorun.domain.AutoRunRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动业务处理类
 * @author Administrator
 *
 */
@Service
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao dao;

    @Autowired
    private AutoRunRecordDao autoRunRecordDao;

    @Autowired
    private HttpServletRequest request;

    @Override
	public List<Channel> getChannelList() {
		return dao.getChannelList();
	}

	@Override
	public List<Channel> getChannelByDate(String date) {
		return dao.getChannelByDate(date);
	}

	@Override
	public List<Channel> getChannelByScheduled(String date, String ip) {
		List<Channel> channelList = dao.getChannelByParams(date, ip);
		return channelList;
	}

    @Override
    public void insertChannelByList(List<Channel> paramList) {
        List<Channel> insertList = new ArrayList<Channel>();
        for (int i = 0; i < paramList.size(); i++) {
            Channel channel = paramList.get(i);

            int count = dao.getChannelCountByParams(channel);
            if (count == 0) {
                insertList.add(channel);
            }
        }
        dao.insertChannelByList(insertList);
    }

    @Override
	@Transactional
	public List<Channel> getChannelByParams(String date, String ip) {
		List<Channel> channelList = dao.getChannelByParams(date, ip);
		List<Channel> historyChannelList = dao.getHistoryChannelByParams(date, ip);
		channelList.addAll(historyChannelList);
		return channelList;
	}

    @Override
    public List<Channel> getHistoryChannelByParams(String date, String ip) {
        List<Channel> historyChannelList = dao.getHistoryChannelByParams(date, ip);
        return historyChannelList;
    }

	@Override
	@Transactional
	public void updateChannelByList(List<Channel> paramList) {

		List<Channel> insertList = new ArrayList<Channel>();
		List<Channel> updateList = new ArrayList<Channel>();
		List<Channel> sendList1 = new ArrayList<Channel>();
		List<Channel> sendList3 = new ArrayList<Channel>();

		for (int i = 0; i < paramList.size(); i++) {
			Channel channel = paramList.get(i);
            String jobName = channel.getJobName();
			String state = channel.getState();
			String sendTime = channel.getSendTime();
            String fileName = channel.getFileName();
            String workType = channel.getWorkType();
            String nextStartTime = channel.getNextStartTime();
            String systemTime = DateUtil.getSystemTime("yyyyMMdd");

			//接口成功只发送一次。接口失败时发送一次，成功时再发送一次。
            //如果channelsMap包含接口名称则取出配置的发送次数，否则使用默认发送次数。
            Integer sendCount = 1;
            if(null != GlobalVariables.channelsMap && GlobalVariables.channelsMap.containsKey(jobName)){
                sendCount = GlobalVariables.channelsMap.get(jobName);
            }
			if("1".equals(state) && sendCount!=-1 && systemTime.equals(sendTime)){
                channel.setRemark("文件已到！");
                sendList1.add(channel);
                GlobalVariables.channelsMap.put(jobName,-1);

                //查询CHANNEL_SYS_JOB_AUTORUN_RECORD是否存在记录
                /*Map<String,Object> map=new HashMap<String,Object>();
                map.put("jobName",jobName);
                map.put("fileName",fileName);
                System.out.println("map >>>>>> " + map);
                AutoRunRecord autoRunRecord = autoRunRecordDao.queryAutoRunRecordByCondition(map);
                System.out.println("autoRunRecord >>>>>> " + autoRunRecord);
                if(null != autoRunRecord){
                        autoRunRecord.setNextStartTime(DateUtil.modifyDay(nextStartTime,workType));
                        //查询是否已经手动执行成功
                        int runCount = autoRunRecordDao.queryInterfaceRunCountByAutoRunRecord(autoRunRecord);

                        System.out.println("runCount >>>>>> " + runCount);
                        //自动更新CHANNEL_SYS_JOB_CONFIG
                        if (runCount == 0){
                            System.out.println("未查到接口成功执行的记录，对config表进行更新操作 >>>>>> " + autoRunRecordDao);
                            //autoRunRecordDao.updateChannelSysJobConfigByAutoRunRecord(autoRunRecord);
                            //autoRunRecordDao.insertChannelSysJobConfigByAutoRunRecord(autoRunRecord);
                        } else {
                            System.out.println("已查到接口成功执行的记录 >>>>>> " + autoRunRecordDao);
                        }
                }*/
            } else if("3".equals(state) && sendCount > 0 && systemTime.equals(sendTime)){
                channel.setRemark("文件超时未到！");
                sendList3.add(channel);
                GlobalVariables.channelsMap.put(jobName, --sendCount);
            }

            //当前时间且数据库中无记录，则插入数据，否则更新数据。历史时间则更新数据。
			if(systemTime.equals(sendTime)){
				int count = dao.getChannelCountByParams(channel);
				if(count == 0){
					insertList.add(channel);
				} else {
                    if(!"2".equals(state)){
                        updateList.add(channel);
                    }
				}
			} else {
                if(!"2".equals(state)){
                    updateList.add(channel);
                }
			}
		}

		// 插入今天记录
        if(null != insertList && insertList.size() > 0 ){
            System.out.println("插入数据 insertList >>>>>> " + insertList);
            dao.insertChannelByList(insertList);
        }
		// 更新历史记录
		if(null != updateList && updateList.size() > 0 ){
			System.out.println("更新数据 updateList >>>>>> " + updateList);
			dao.updateHistoryChannelByList(updateList);
		}
		// 成功发送邮件短信提醒
		if(null != sendList1 && sendList1.size() > 0 ){
			System.out.println("文件已到 sendList1 >>>>>> " + sendList1);
            //EmailUtil.sendEmailByChannelList(sendList1);
			MessageUtil.sendMessageByChannelList(sendList1);
		}
		// 失败发送邮件短信提醒
		if(null != sendList3 && sendList3.size() > 0 ){
			System.out.println("文件未到 sendList3 >>>>>> " + sendList3);
            //EmailUtil.sendEmailByChannelList(sendList3);
			MessageUtil.sendMessageByChannelList(sendList3);
		}

        //Map channelsMap = (Map) request.getServletContext().getAttribute("channelsMap");
        System.out.println("channelsMap >>>>>> " + GlobalVariables.channelsMap);
	}

	@Override
	public PaginationVO<Channel> queryChannelForPageByCondition(Map<String, Object> map) {
		List<Channel> activityList = dao.queryChannelForPageByCondition(map);
		long count = dao.queryCountOfChannelByCondition(map);

		PaginationVO<Channel> vo=new PaginationVO<>();
		vo.setDataList(activityList);
		vo.setCount(count);
		return vo;
	}

	@Override
	public PaginationVO<Channel> queryChannelByAutoComplate(Map<String, Object> map) {
		List<Channel> activityList = dao.queryChannelByAutoComplate(map);

		PaginationVO<Channel> vo=new PaginationVO<>();
		vo.setDataList(activityList);
		return vo;
	}

	@Override
	public int queryActivityForId() {
		return dao.queryActivityForId();
	}

	@Override
	public Channel queryActivityById(String id) {
		return null;
	}


	/**
	 * 保存创建的活动
	 */
	@Override
	public int saveCreateMarketActivity(Channel activity) {
		return dao.saveCreateMarketActivity(activity);
	}

	/**
	 * 根据条件分页查询活动
	 */
	@Override
	public PaginationVO<Channel> queryMarketActivityForPageByCondition(Map<String, Object> map) {
		List<Channel> activityList = dao.queryMarketActivityForPageByCondition(map);
		long count = dao.queryCountOfMarketActivityByCondition(map);
		
		//将activityList和count封装成map
		PaginationVO<Channel> vo=new PaginationVO<>();
		vo.setDataList(activityList);
		vo.setCount(count);
		return vo;
	}
	/**
	 * 根据ids数组删除活动
	 */
	@Override
	public int deleteMarketActivityByIds(String[] ids) {
		//1.删除父表时要删除子表，即先删除线索备注，在删除线索
		//2.或者给数据库添加级联删除cascade
		return dao.deleteMarketActivityByIds(ids);
	}

	@Override
	public List<Channel> selectMarketActivityListByIds(String[] ids) {
		return dao.selectMarketActivityListByIds(ids);
	}

	/**
	 * 根据单个id查询单个活动
	 */
	@Override
	public Channel queryMarketActivityById(String id) {
		return dao.queryMarketActivityById(id);
	}
	/**
	 * 根据单个id更新单个活动
	 */
	@Override
	public int SaveEditMarketActivityById(Channel activity) {
		return dao.SaveEditMarketActivityById(activity);
	}
	/**
	 * 根据id查询活动详细信息
	 */
	@Override
	public Channel queryMarketActivityDetailById(String id) {
		return dao.queryMarketActivityDetailById(id);
	}
	/**
	 * 根据条件查询所有活动列表
	 */
	@Override
	public List<Channel> queryMarketActivityByCondition(Map<String, Object> map) {
		return dao.queryMarketActivityByCondition(map);
	}
	/**
	 * 批量保存创建的活动
	 * @param activityList
	 * @return
	 */
	@Override
	public int saveCreateMarketActivityByList(List<Channel> activityList) {
		return dao.saveCreateMarketActivityByList(activityList);
	}
	/**
	 * 根据clueId查询相关联的活动
	 */
	@Override
	public List<Channel> queryMarketActiviytByClueId(String clueId) {
		return dao.queryMarketActiviytByClueId(clueId);
	}
	/**
	 * 根据name模糊查询与clueId关联的活动
	 */
	@Override
	public List<Channel> queryMarketActivityByNameClueId(Map<String,Object> map){
		return dao.queryMarketActivityByNameClueId(map);
	}
	/**
	 * 根据ids数组查询活动
	 */
	@Override
	public List<Channel> queryMarketActivityByIds(String[] ids) {
		return dao.queryMarketActivityByIds(ids);
	}

}
