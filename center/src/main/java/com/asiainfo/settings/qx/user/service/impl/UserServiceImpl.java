package com.asiainfo.settings.qx.user.service.impl;

import com.asiainfo.settings.qx.user.dao.UserDao;
import com.asiainfo.settings.qx.user.domain.User;
import com.asiainfo.settings.qx.user.service.UserService;
import com.asiainfo.workbench.contacts.domain.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 用户业务处理类
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
	 * 根据loginAct和loginPwd查询用户
	 */
	@Override
	public User queryUserByLoginActPwd(Map<String, Object> map) {
		return userDao.queryUserByLoginActPwd(map);
	}
	/**
	 * 查询所有用户返回List集合
	 */
	@Override
	public List<User> queryAllUsers() {
		return userDao.queryAllUsers();
	}

	@Override
	public int updateUserByActPwd(Map<String, Object> map) {
		return userDao.updateUserByActPwd(map);
	}

}
