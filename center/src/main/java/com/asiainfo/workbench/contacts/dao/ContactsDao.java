package com.asiainfo.workbench.contacts.dao;

import com.asiainfo.workbench.contacts.domain.Group;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.domain.StaffGroupRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 联系人持久化处理接口
 * @author Administrator
 *
 */

@Mapper
public interface ContactsDao {
    /**
     * 创建联系人
     */
    public int SaveCreateContacts(Staff contacts);
    /**
     * 根据人名和手机号精确查询客户
     * @param map
     * @return
     */
    public Staff queryContactsByFullNameMphone(Map<String, Object> map);
    /**
     * 根据条件分页查询联系人(查名字)
     */
    public List<Staff> queryContactsForPageByCondition(Map<String, Object> map);

    public List<Staff> queryStaffByAutoComplate(Map<String, Object> map);

    /**
     * 根据条件查询联系人总条数
     */
    public Long queryContactsForCountByCondition(Map<String, Object> map);

    public int queryContactsForId();

    public Staff queryContactsById(String id);
    /**
     * 根据ids批量删除联系人
     */
    public int deleteContactsByIds(String[] ids);

    public List<Group> queryGroupList();

    public List<Group> queryGroupListById(String id);

    public int SaveEditContacts(Staff staff);

    public int SaveCreateStaffGroupRelation(StaffGroupRelation staffGroupRelation);

    public List<Staff> selectStaffListByIds(@Param("set") Set set);

    //public int deleteRelationByIds(@Param("map") Map map);
    public int deleteRelationByIds(Map map);

    public int insertRelationByIds(Map map);

    Staff selectByPrimaryKey(Integer id);

    int saveCreateStaffByList(List<Staff> staffList);
}
