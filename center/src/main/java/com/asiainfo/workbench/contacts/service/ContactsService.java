package com.asiainfo.workbench.contacts.service;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.contacts.domain.Group;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.domain.StaffGroupRelation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 联系人业务处理接口
 * @author Administrator
 *
 */
public interface ContactsService {
	/**
	 * 创建联系人
	 */
	public int SaveCreateContacts(Staff contacts);
	/**
	 * 根据条件分页查询联系人(查名字)和总条数
	 */
	public PaginationVO<Staff> queryContactsForPageByCondition(Map<String, Object> map);

	public PaginationVO<Staff> queryStaffByAutoComplate(Map<String, Object> map);

	/**
	 * 根据ids批量删除联系人
	 */
	public int deleteContactsByIds(String[] ids);

	public List<Group> queryGroupList();

	public List<Group> queryGroupListById(String id);

	public Staff queryContactsById(String id);

	public int SaveCreateStaffGroupRelation(StaffGroupRelation staffGroupRelation);

    public int SaveEditContacts(Staff staff);

    public int queryContactsForId();

	public List<Staff> selectStaffListByIds(Set set);

    public int deleteRelationByIds(Map map);

    public int insertRelationByIds(Map map);

	public Staff getStaffById(Integer id);

	public int saveCreateStaffByList(List<Staff> staffList);
}
