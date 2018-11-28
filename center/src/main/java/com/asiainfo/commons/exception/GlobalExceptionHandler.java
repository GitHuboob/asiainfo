package com.asiainfo.commons.exception;

import com.asiainfo.commons.constant.CommonConstants;
import com.asiainfo.commons.model.CommonException;
import com.asiainfo.commons.model.ResponseInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    //默认异常处理页面
    //public static final String DEFAUL_ERROR_VIEW = "error";

    /**
     * 默认异常处理方法,返回异常请求路径和异常信息
     */
    /*@ExceptionHandler(value = Exception.class)
    public ModelAndView myErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURI()); //异常请求路径
        modelAndView.addObject("exception", e);                 //异常信息
        modelAndView.setViewName(DEFAUL_ERROR_VIEW);                          //返回异常处理页面
        return modelAndView;
    }*/

    /**
     * 默认全局异常捕捉处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseInfo errorHandler(Exception e) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(CommonConstants.FAIL);
        responseInfo.setMessage(e.getMessage());
        return responseInfo;
    }

    /**
     * @ExceptionHandler 匹配抛出自定义的异常类型 CommonException
     * ResponseInfo<String> 为自定义的一个数据封装类，用于返回的json数据
     * 如果返回的是json格式，需要加上@ResponsBody
     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public ResponseInfo jsonErrorHandler(HttpServletRequest request, CommonException e) throws Exception{
        ResponseInfo<String> responseInfo = new ResponseInfo<>();
        responseInfo.setCode(e.getCode());
        responseInfo.setMessage(e.getMessage());
        return responseInfo; //反馈结果
    }

}
