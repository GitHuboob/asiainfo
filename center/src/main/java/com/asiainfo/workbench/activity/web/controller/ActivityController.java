package com.asiainfo.workbench.activity.web.controller;

import com.asiainfo.commons.model.PaginationVO;
import com.asiainfo.commons.util.CellUtil;
import com.asiainfo.commons.util.EmailUtil;
import com.asiainfo.commons.util.MessageUtil;
import com.asiainfo.workbench.activity.domain.Channel;
import com.asiainfo.workbench.activity.service.ActivityService;
import com.asiainfo.workbench.contacts.service.ContactsService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class ActivityController extends HttpServlet {

    @Autowired
    private ActivityService as;

    @Autowired
    private ContactsService cs;

    @RequestMapping("/workbench/activity/sendEmail")
    public Object sendEmailController (Model model, HttpServletRequest request, HttpServletResponse response) {
        //接收参数
        String[] ids = request.getParameterValues("id");

        List<Channel> channelList = as.selectMarketActivityListByIds(ids);
        String flag = EmailUtil.sendEmailByChannelList(channelList);

        //根据处理结果，返回响应信息
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping("/workbench/activity/sendMessage")
    public Object sendMessageController (Model model, HttpServletRequest request, HttpServletResponse response) {
        //接收参数
        String[] ids = request.getParameterValues("id");

        List<Channel> channelList = as.selectMarketActivityListByIds(ids);
        String flag = MessageUtil.sendMessageByChannelList(channelList);

        //根据处理结果，返回响应信息
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);

        return map;
    }

    @RequestMapping("/workbench/activity/deleteActivity")
    public Object DeleteMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数
        String[] ids = request.getParameterValues("id");

        int i = as.deleteMarketActivityByIds(ids);

        //根据处理结果，返回响应信息
        Map<String,Object> map = new HashMap<String,Object>();
        if(i > 0){
            map.put("success", true);
        }else{
            map.put("success", false);
        }

        return map;
    }

    @RequestMapping("/workbench/activity/editActitity")
    public Object EditMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数
        String id = request.getParameter("id");

        //List<User> userList = us.queryAllUsers();
        Channel activity = as.queryMarketActivityById(id);

        //根据查询结果返回响应信息json
        Map<String,Object> map = new HashMap<String,Object>();
        //map.put("userList", userList);
        map.put("activity", activity);

        System.out.println("map >>>>>> " + map);

        return map;
    }

    //restful请求参数为空如何解决
    @RequestMapping(value="/workbench/activity/exportExcel", params={"name","type","state","startDate","endDate"})
    public void ExportMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response,
         @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("state") String state,
         @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException {

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", name);
        map.put("type", type);
        map.put("state", state);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", 0);
        map.put("pageSize", 1000);

        PaginationVO<Channel> vo = as.queryChannelForPageByCondition(map);
        List<Channel> channelList = vo.getDataList();

        //根据查询结果,使用poi生成excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("接口状态列表");
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
        String[] arr = {"ID","接口名称","接口类型","接口状态","所属组","下次执行时间","描述"};
        for(int i = 0; i < arr.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(arr[i]);
        }
        //创建数据行
        if(channelList!=null&&channelList.size()>0){
            Channel ch=null;
            for(int i=0;i<channelList.size();i++){
                row=sheet.createRow(i+1);
                ch=channelList.get(i);

                HSSFCell cell=row.createCell(0);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getId());
                cell=row.createCell(1);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getJobName());
                cell=row.createCell(2);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getWorkType());
                cell=row.createCell(3);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getState());
                cell=row.createCell(4);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getGroupCode());
                cell=row.createCell(5);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getNextStartTime());
                cell=row.createCell(6);
                cell.setCellStyle(style2);
                cell.setCellValue(ch.getRemark());
            }
        }

        String fileName = URLEncoder.encode("接口状态表","UTF-8");
        if(request.getHeader("User-Agent").toLowerCase().contains("firefox")){
            fileName = new String("接口状态表".getBytes("UTF-8"),"ISO8859-1");
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+".xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
    }
    @RequestMapping("/workbench/activity/importExcel")
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
                List<Channel> channelList=new ArrayList<Channel>();
                for (int i = 1; i <= sheel.getLastRowNum(); i++) {
                    HSSFRow row = sheel.getRow(i);
                    Channel ch = new Channel();

                    ch.setJobName(CellUtil.getValueFromCell(row.getCell(1)));
                    ch.setState(CellUtil.getValueFromCell(row.getCell(3)).replace(".0",""));
                    ch.setNextStartTime(CellUtil.getValueFromCell(row.getCell(5)));
                    ch.setRemark("批量导入" + new Date());
                    channelList.add(ch);

                    //批量提交
                    if(channelList.size()%3 == 0){
                        count += as.saveCreateMarketActivityByList(channelList);
                        channelList.clear();
                    }
                }
                if(channelList.size() > 0){
                    count += as.saveCreateMarketActivityByList(channelList);
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

    @RequestMapping("/workbench/activity/queryChannelInfo")
    public Object QueryMarketActivityForPageByConditionController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取参数 
        String name=request.getParameter("name");
        String type=request.getParameter("type");
        String state=request.getParameter("state");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String pageNoStr=request.getParameter("pageNo");
        String pageSizeStr=request.getParameter("pageSize");

        //封装参数
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", name);
        map.put("type", type);
        map.put("state", state);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        long pageNo=1;
        if(pageNoStr!=null&&pageNoStr.trim().length()>0){
            pageNo=Long.parseLong(pageNoStr.trim());
        }
        int pageSize=5;
        if(pageSizeStr!=null&&pageSizeStr.trim().length()>0){
            pageSize=Integer.parseInt(pageSizeStr.trim());
        }
        map.put("beginNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);

        System.out.println("map >>>>>> " + map);

        //调用service方法,查询数据 
        //PaginationVO<Channel> vo=mas.queryMarketActivityForPageByCondition(map);
        PaginationVO<Channel> vo = as.queryChannelForPageByCondition(map);

        System.out.println(vo.getDataList().toString());

        return vo;
    }

    @RequestMapping("/workbench/activity/queryChannelInfoByAutoComplate")
    public Object QueryMarketActivityByAutoComplateController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name=request.getParameter("name");

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name", name);

        PaginationVO<Channel> vo = as.queryChannelByAutoComplate(map);
        return vo;
    }
    @RequestMapping("/workbench/activity/saveCreateChannel")
    public Object SaveCreateMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取参数
        //String owner=request.getParameter("owner");
        //String type=request.getParameter("type");
        String name=request.getParameter("name");
        String state=request.getParameter("state");
        String time=request.getParameter("time");
        String description=request.getParameter("description");

        int id = as.queryActivityForId();

        //封装参数
        Channel channel = new Channel();
        channel.setId(id);
        channel.setJobName(name);
        channel.setState(state);
        channel.setNextStartTime(time);
        channel.setRemark(description);

        System.out.println("channel >>>>>> " + channel.toString());

        //通过代理者调用service方法,保存数据
        int i = as.saveCreateMarketActivity(channel);

        //根据处理结果,生成响应信息(json)
        Map<String,Object> map=new HashMap<String,Object>();
        if(i>0){
            map.put("success", true);
        }else{
            map.put("success", false);
        }

        return map;
    }
    @RequestMapping("/workbench/activity/saveEditActitity")
    public Object SaveEditMarketActivityController (Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        //String owner = request.getParameter("owner");
        //String type = request.getParameter("type");
        //String name = request.getParameter("name");
        String state = request.getParameter("state");
        String description = request.getParameter("description");

        Channel channel = new Channel();
        channel.setId(Integer.valueOf(id));
        channel.setRemark(description);
        channel.setState(state);

        System.out.println("channel >>>>>> " + channel.toString());

        int ret = as.SaveEditMarketActivityById(channel);

        //根据处理结果,返回响应信息(json)
        Map<String,Object> map = new HashMap<String,Object>();
        if(ret > 0){
            map.put("success", true);
        }else{
            map.put("success", false);
        }
        return map;
    }

}
