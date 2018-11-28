package com.asiainfo.workbench.contacts.web.controller;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.commons.util.CellUtil;
import com.asiainfo.commons.util.DateUtil;
import com.asiainfo.workbench.contacts.domain.Group;
import com.asiainfo.workbench.contacts.domain.Staff;
import com.asiainfo.workbench.contacts.service.ContactsService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class ContactsController {

    @Autowired
    private ContactsService cs;

    private org.slf4j.Logger logger = LoggerFactory.getLogger(ContactsController.class);

    @RequestMapping("/workbench/contacts/queryStaffByGroupCode")
    @ResponseBody
    public Object getStaffByGroupCodeController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fullName", request.getParameter("fullName"));
        map.put("groupCode", request.getParameter("groupCode"));

        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        long pageNo = CommonConstants.PAGE_NUMBER;
        if (pageNoStr != null && pageNoStr.length() > 0) {
            pageNo = Long.parseLong(pageNoStr.trim());
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr.trim());
        }

        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        PaginationVO<Staff> vo = cs.queryContactsForPageByCondition(map);
        return vo;
    }

    @RequestMapping("/workbench/contacts/queryStaffInfoByAutoComplate")
    @ResponseBody //注意@Controller需要注解@ResponseBody，否则ajax请求失效！
    public Object QueryStaffByAutoComplateController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("fullName", request.getParameter("name"));

        PaginationVO<Staff> vo = cs.queryStaffByAutoComplate(map);
        return vo;
    }

    @RequestMapping("/workbench/contacts/queryGroupCode")
    @ResponseBody
    public Object getGroupCodeListController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Group> groupList = cs.queryGroupList();
        return groupList;
    }

    @RequestMapping("/workbench/contacts/saveContacts")
    @ResponseBody
    public Object saveEditContactsInfoController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String description = request.getParameter("description");

        int staffId = cs.queryContactsForId();

        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setStaffName(fullName);
        staff.setStaffEmail(email);
        staff.setStaffPhone(mphone);
        staff.setState("1");
        staff.setRemark(description);
        staff.setCreateTime(DateUtil.parseStringTime(new Date()));

        System.out.println("staff >>>>>> " + staff);

        int ret = cs.SaveCreateContacts(staff);

        Map<String, Object> map = new HashMap<String, Object>();
        if (ret > 0) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping("/workbench/contacts/editContacts")
    @ResponseBody
    public Object getEditContactsInfoController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        System.out.println("id >>>>>> " + id);

        Staff staff = cs.queryContactsById(id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activity",staff);
        System.out.println("map >>>>>>> " + map);

        return map;
    }

    @RequestMapping("/workbench/contacts/saveEditContacts")
    @ResponseBody
    public Object SaveEditContactsController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String description = request.getParameter("description");

        Staff staff = new Staff();
        staff.setStaffId(Integer.valueOf(id));
        staff.setStaffName(name);
        staff.setStaffEmail(email);
        staff.setStaffPhone(phone);
        staff.setRemark(description);
        System.out.println("staff >>>>>> " + staff.toString());

        int ret = cs.SaveEditContacts(staff);

        Map<String,Object> map = new HashMap<String,Object>();
        if(ret > 0){
            map.put("success", true);
        }else{
            map.put("success", false);
        }
        return map;
    }

    // 删除联系人
    @RequestMapping("/workbench/contacts/deleteContacts")
    @ResponseBody
    public Object deleteContactsController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("id");
        int ret = cs.deleteContactsByIds(ids);

        Map<String,Object> map = new HashMap<String,Object>();
        if(ret > 0){
            map.put("success", true);
        }else{
            map.put("success", false);
        }
        return map;
    }

    // 根据id查询员工信息，返回页面！
    @RequestMapping(value="/workbench/contacts/detailContacts/{id}")
    public String getStaffDetailByIdController (Model model, @PathVariable("id") Integer staffId) {
        Staff staff = cs.getStaffById(staffId);
        List<Group> groupList = cs.queryGroupListById(String.valueOf(staffId));

        System.out.println("staff >>>>>> " + staff);
        System.out.println("groupList >>>>>> " + groupList);

        model.addAttribute("staff", staff);
        model.addAttribute("groupList", groupList);

        return "workbench/contacts/detail";
    }

    // 根据id查询员工信息，返回json
    @RequestMapping("/workbench/contacts/editContactsDetail")
    @ResponseBody
    public Object EditContactsController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Staff staff = cs.getStaffById(Integer.valueOf(id));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("staff", staff);
        System.out.println("map >>>>>> " + map);

        return map;
    }

    // 解除关联关系
    @RequestMapping("/workbench/contacts/unbundRelation")
    @ResponseBody
    public Object unbundRelationController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String staffId = request.getParameter("staffId");
        String groupId = request.getParameter("groupId");
        System.out.println("staffId >>>>>> " + staffId);
        System.out.println("groupId >>>>>> " + groupId);

        Map map = new HashMap();
        map.put("staffId", staffId);
        map.put("groupId", groupId);
        int ret = cs.deleteRelationByIds(map);

        Map<String,Object> retMap = new HashMap<String,Object>();
        if(ret > 0){
            retMap.put("success", true);
        }else{
            retMap.put("success", false);
        }
        return retMap;
    }

    // 添加关联关系
    @RequestMapping("/workbench/contacts/bundRelation")
    @ResponseBody
    public Object bundRelationController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String staffId = request.getParameter("staffId");
        String groupIds = request.getParameter("ids");

        System.out.println("staffId >>>>>> " + staffId);
        System.out.println("groupIds >>>>>> " + groupIds);

        String[] splitGroupIds = groupIds.replaceAll("id=", "").split("&");
        System.out.println("splitGroupIds >>>>>> " + Arrays.toString(splitGroupIds));

        Map map = new HashMap();
        map.put("staffId", staffId);
        map.put("groupIds", splitGroupIds);
        int ret = cs.insertRelationByIds(map);

        Map<String,Object> retMap = new HashMap<String,Object>();
        if(ret > 0){
            retMap.put("success", true);
        }else{
            retMap.put("success", false);
        }
        return retMap;
    }

    @RequestMapping(value="/workbench/contacts/exportExcel", params={"name"})
    @ResponseBody
    public void ExportContactsController (Model model, HttpServletRequest request, HttpServletResponse response,
                                                @RequestParam("name") String name) throws IOException {

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", name);
        map.put("beginNo", 0);
        map.put("pageSize", 1000);

        PaginationVO<Staff> vo = cs.queryContactsForPageByCondition(map);
        List<Staff> staffList = vo.getDataList();

        //根据查询结果,使用poi生成excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("联系人列表");
        sheet.setDefaultColumnWidth(18);
        //生成样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //对应一行
        HSSFRow row = sheet.createRow(0);
        //对应一列
        String[] arr = {"ID","姓名","邮箱","手机","创建时间","描述"};
        for(int i = 0; i < arr.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(arr[i]);
        }
        //创建数据行
        if(staffList!=null&&staffList.size()>0){
            Staff st=null;
            for(int i=0;i<staffList.size();i++){
                row=sheet.createRow(i+1);
                st=staffList.get(i);

                HSSFCell cell=row.createCell(0);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getStaffId());
                cell=row.createCell(1);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getStaffName());
                cell=row.createCell(2);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getStaffEmail());
                cell=row.createCell(3);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getStaffPhone());
                cell=row.createCell(4);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getCreateTime());
                cell=row.createCell(5);
                cell.setCellStyle(style2);
                cell.setCellValue(st.getRemark());
            }
        }

        String fileName = URLEncoder.encode("联系人表","UTF-8");
        if(request.getHeader("User-Agent").toLowerCase().contains("firefox")){
            fileName = new String("联系人表".getBytes("UTF-8"),"ISO8859-1");
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
    }

    @RequestMapping("/workbench/contacts/importExcel")
    public Object ImportMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,Object> map=new HashMap<String,Object>();
        int count = 0;
        try {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> iterator = req.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = req.getFile(iterator.next());

                String fileNames = file.getOriginalFilename();
                int split = fileNames.lastIndexOf(".");
                System.out.println("fileNames >>>>>> "+fileNames.substring(0,split));
                System.out.println("fileTypes >>>>>> "+fileNames.substring(split+1,fileNames.length()));

                HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
                //获取页信息
                HSSFSheet sheel = wb.getSheetAt(0);
                //获取数据行的信息
                List<Staff> staffList=new ArrayList<Staff>();
                for (int i = 1; i <= sheel.getLastRowNum(); i++) {
                    HSSFRow row = sheel.getRow(i);

                    Staff st = new Staff();
                    st.setStaffName(CellUtil.getValueFromCell(row.getCell(1)));
                    st.setStaffEmail(CellUtil.getValueFromCell(row.getCell(2)));
                    st.setStaffPhone(CellUtil.getValueFromCell(row.getCell(3)));
                    st.setState("1");
                    st.setRemark("批量导入" + new Date());
                    staffList.add(st);

                    //批量提交
                    if(staffList.size()%3 == 0){
                        count += cs.saveCreateStaffByList(staffList);
                        staffList.clear();
                    }
                }
                if(staffList.size() > 0){
                    count += cs.saveCreateStaffByList(staffList);
                }

                //根据处理结果,返回响应信息(json)
                if(count>0){
                    map.put("success", true);
                    map.put("count", count);
                }else{
                    map.put("success", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("count", count);
        }
        return map;
    }

}
