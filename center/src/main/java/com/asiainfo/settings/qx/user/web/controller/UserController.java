package com.asiainfo.settings.qx.user.web.controller;

import com.asiainfo.settings.qx.user.domain.User;
import com.asiainfo.settings.qx.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController extends HttpServlet{

    @Autowired
    private UserService us;

    @RequestMapping("settings/qx/user/queryUser")
    @ResponseBody
    public Object Login (HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("user >>>>>> " + user);
        return user;
    }

    @RequestMapping("settings/qx/user/modifyPwd")
    @ResponseBody
    public Object logout (HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        Map<String,Object> retMap = new HashMap<String,Object>();
        retMap.put("success", false);

        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String confirmPwd = request.getParameter("confirmPwd");

        if(oldPwd.equals(user.getLoginPwd()) && newPwd.equals(confirmPwd)){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("loginAct", user.getLoginAct());
            map.put("loginPwd", newPwd);
            System.out.println("map >>>>>> " + map);

            int count = us.updateUserByActPwd(map);
            if(count > 0){
                retMap.put("success", true);
            }
        }
        return retMap;
    }

}
