package com.asiainfo.commons.filter;

import com.asiainfo.settings.qx.user.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 * 定义一个普通的Filter
 */
@Order(1)
@WebFilter(filterName = "CommonFilter", urlPatterns = {"/*"})
@Configuration//@Service也可以
public class CommonFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        // System.out.println("您已进入CommonFilter过滤器...");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getSession().getAttribute("user");

        if(user != null){
            chain.doFilter(req, resp);
        }else{
            //注意：放行下载文件监控数据和上传文件监控数据路径，以及资源文件
            if("/settings/qx/user/login.jsp".equals(request.getServletPath())||"/settings/qx/user/login".equals(request.getServletPath())
                    ||"/index.html".equals(request.getServletPath())||request.getServletPath().contains("/monitor/")
                    ||request.getServletPath().contains("/jquery/")||request.getServletPath().contains("/image/")){
                chain.doFilter(req, resp);
            }else{
                response.sendRedirect(request.getContextPath()+"/index.html");
                System.out.println("您的请求被拦截...");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
