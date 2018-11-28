package com.asiainfo.settings.qx.user.service;

import com.asiainfo.settings.qx.user.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户业务处理接口
 * @author Administrator
 *
 */
public interface UserService {
	/**
	 * 根据loginAct和loginPwd查询用户
	 * @param map
	 * @return
	 */
	public User queryUserByLoginActPwd(Map<String, Object> map);
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers();

	public int updateUserByActPwd(Map<String, Object> map);
}
