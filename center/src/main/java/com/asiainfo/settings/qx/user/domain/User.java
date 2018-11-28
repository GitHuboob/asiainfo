package com.asiainfo.settings.qx.user.domain;
/**
 * 用户实体类
 * @author Administrator
 *
 */
public class User {

	private String id;
	private String name;
	private String loginAct;
	private String loginPwd;
	private String lockStatus;
	private String allowIps;
	private String createTime;
	private String editTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginAct() {
		return loginAct;
	}

	public void setLoginAct(String loginAct) {
		this.loginAct = loginAct;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(String allowIps) {
		this.allowIps = allowIps;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", loginAct='" + loginAct + '\'' +
				", loginPwd='" + loginPwd + '\'' +
				", lockStatus='" + lockStatus + '\'' +
				", allowIps='" + allowIps + '\'' +
				", createTime='" + createTime + '\'' +
				", editTime='" + editTime + '\'' +
				'}';
	}
}
