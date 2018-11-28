package com.asiainfo.workbench.contacts.service.impl;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.contacts.dao.ContactsDao;
import com.asiainfo.workbench.contacts.domain.Group;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.domain.StaffGroupRelation;
import com.asiainfo.workbench.contacts.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 联系人业务处理类
 * @author Administrator
 *
 */

@Service
public class ContactsServiceImpl implements ContactsService {

	@Autowired
	private ContactsDao contactsDao;
	/**
	 * 创建联系人
	 */
	@Override
	public int SaveCreateContacts(Staff contacts) {
		return contactsDao.SaveCreateContacts(contacts);
	}
	/**
	 * 根据条件分页查询联系人(查名字)和总条数
	 */
	@Override
	public PaginationVO<Staff> queryContactsForPageByCondition(Map<String, Object> map) {
		List<Staff> contactsList = contactsDao.queryContactsForPageByCondition(map);
		long count = contactsDao.queryContactsForCountByCondition(map);
		
		PaginationVO<Staff> vo = new PaginationVO<Staff>();
		vo.setDataList(contactsList);
		vo.setCount(count);
		return vo;
	}

	@Override
	public PaginationVO<Staff> queryStaffByAutoComplate(Map<String, Object> map) {
		List<Staff> contactsList = contactsDao.queryStaffByAutoComplate(map);

		PaginationVO<Staff> vo=new PaginationVO<>();
		vo.setDataList(contactsList);
		return vo;
	}

	/**
	 * 根据ids批量删除联系人
	 */
	@Override
    @Transactional //此方法需要事务管理
	public int deleteContactsByIds(String[] ids) {
		return contactsDao.deleteContactsByIds(ids);
	}

    @Override
    public List<Group> queryGroupList() {
        return contactsDao.queryGroupList();
    }

	@Override
	public List<Group> queryGroupListById(String id) {
		return contactsDao.queryGroupListById(id);
	}

	@Override
	public Staff queryContactsById(String id) {
		return contactsDao.queryContactsById(id);
	}

	@Override
    @Transactional //此方法需要事务管理
	public int SaveCreateStaffGroupRelation(StaffGroupRelation staffGroupRelation) {
		return contactsDao.SaveCreateStaffGroupRelation(staffGroupRelation);
	}

	@Override
    @Transactional //此方法需要事务管理
	public int SaveEditContacts(Staff staff) {
		return contactsDao.SaveEditContacts(staff);
	}

	@Override
    public int queryContactsForId() {
        return contactsDao.queryContactsForId();
    }

	@Override
	public List<Staff> selectStaffListByIds(Set set) {
		return contactsDao.selectStaffListByIds(set);
	}

	@Override
    @Transactional //此方法需要事务管理
	public int deleteRelationByIds(Map map) {
		return contactsDao.deleteRelationByIds(map);
	}

	@Override
    @Transactional //此方法需要事务管理
	public int insertRelationByIds(Map map) {
		return contactsDao.insertRelationByIds(map);
	}

	@Override
	public Staff getStaffById (Integer id) {
		return contactsDao.selectByPrimaryKey(id);
	}

	@Override
	public int saveCreateStaffByList(List<Staff> staffList) {
		return contactsDao.saveCreateStaffByList(staffList);
	}
}
