package com.asiainfo.workbench.search.controller;

import com.asiainfo.workbench.search.domain.SendInfo;
import com.asiainfo.workbench.search.service.SendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/es")
public class SendInfoController {

    @Autowired
    private SendInfoService sendInfoService;

    //http://localhost:8080/es/testSave/sendInfo
    @GetMapping("/testSave/sendInfo")
    public String saveSendInfo(){
        SendInfo sendInfo = new SendInfo(123L,"邮件接收者","邮件内容测试","短信接收者","短信内容测试","这是描述测试" );
        sendInfoService.insert(sendInfo);
        return "success";
    }

    //http://localhost:8080/es/delete/sendInfo?id=123
    @GetMapping("/delete/sendInfo")
    public String deleteSendInfo(long id){
        sendInfoService.delete(id);
        return "success";
    }

    //http://localhost:8080/es/update/sendInfo?id=123&emailContent=修改&messageContent=修改&description=修改
    @GetMapping("update/sendInfo")
    public String updateSendInfo(long id,String emailReceiver,String emailContent,String messageReceiver,String messageContent,String description){
        SendInfo sendInfo = new SendInfo(id,emailReceiver,emailContent,messageReceiver,messageContent,description);
        sendInfoService.insert(sendInfo);
        return "success";
    }

    //http://localhost:8080/es/getOne/sendInfo?id=123
    @GetMapping("getOne/sendInfo")
    public SendInfo getOneSendInfo(long id){
        SendInfo sendInfo = sendInfoService.getOne(id);
        return sendInfo;
    }


    @PostMapping("/sendInfo/create")
    public boolean create(@RequestBody SendInfo sendInfo) {
        return sendInfoService.insert(sendInfo);
    }

    @GetMapping("/sendInfo/searchContent")
    public List<SendInfo> search(@RequestParam(value = "searchContent") String searchContent) {
        return sendInfoService.search(searchContent);
    }

    @GetMapping("/sendInfo/searchContentByPage")
    public List<SendInfo> searchSendInfo(@RequestParam(value = "pageNumber") Integer pageNumber,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @RequestParam(value = "searchContent") String searchContent) {
        return sendInfoService.searchSendInfo(pageNumber, pageSize, searchContent);
    }

    @GetMapping("/sendInfo/searchContentByWeight")
    public List<SendInfo> searchSendInfoByWeight(@RequestParam(value = "searchContent") String searchContent) {
        return sendInfoService.searchSendInfoByWeight(searchContent);
    }
}
