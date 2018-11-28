package com.asiainfo.settings.qx.user.web.controller;

import com.asiainfo.settings.qx.user.domain.User;
import com.asiainfo.settings.qx.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController extends HttpServlet{

    @Autowired
    private UserService us;

    // 根据id和名字更新信息
    @RequestMapping("settings/qx/user/login")
    @ResponseBody
    public Object Login (HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        String isRemPwd = request.getParameter("isRemPwd");

        //封装参数
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        System.out.println("map >>>>>> " + map);

        //调用service方法
        User user = us.queryUserByLoginActPwd(map);
        System.out.println("user >>>>>> " + user);

        //根据查询结果返回响应信息
        Map<String,Object> retMap = new HashMap<String,Object>();
        if(user == null){
            retMap.put("success", false);
            retMap.put("msg", "用户名或密码错误");
        }else{
            if(!"1".equals(user.getLockStatus())){
                retMap.put("success", false);
                retMap.put("msg", "用户已失效");
            }else{
                //登陆成功
                retMap.put("success", true);

                //把user保存到session中
                request.getSession().setAttribute("user", user);

                //处理是否记住密码
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct",loginAct);
                    Cookie c2 = new Cookie("loginPwd",loginPwd);
                    c1.setMaxAge(10*24*60*60);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }else{
                    Cookie c1 = new Cookie("loginAct",loginAct);
                    Cookie c2 = new Cookie("loginPwd",loginPwd);
                    c1.setMaxAge(0);
                    c2.setMaxAge(0);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }
            }
        }

        return retMap;
    }

    @RequestMapping("settings/qx/user/logout")
    public String logout (HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }

        //销毁cookie
        Cookie c1 = new Cookie("loginAct","");
        Cookie c2 = new Cookie("loginPwd","");
        c1.setMaxAge(0);
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);

        return "settings/qx/user/login";
    }

}
