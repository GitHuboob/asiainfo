<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	//http://ip:port/app/
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
<base href="<%=basePath %>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function(){
		//把login.jsp在顶层窗口中打开
		if(window.top!=window){
			window.top.location = window.location;
		}
		
		//页面加载完成后，光标出现在用户名输入框
		$("#loginAct").focus();
		
		//给页面添加键盘按下事件
		$(window).keydown(function(e){
			if(e.keyCode==13){
				$("#loginBtn").click();
			}
		});
		
		//给登陆按钮绑定点击事件
		$("#loginBtn").click(

		    function(){

		    var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			var isRemPwd = $("#isRemPwd").prop("checked");

			if(loginAct==null||loginPwd.length==0){
				alert("用户名不能为空");
				return;
			}
			if(loginPwd==null||loginPwd.length==0){
				alert("密码不能为空");
				return;
			}
			
			$.ajax({
				/*url:'settings/qx/user/login.do',*/
                url:'settings/qx/user/login',
				data:{
					loginAct : loginAct,
					loginPwd : loginPwd,
					isRemPwd : isRemPwd
				},
				//在ajax发送请求之前执行本函数，本函数返回true时继续向后台发送请求，返回false时不再发送请求
				beforeSend:function(){
					$("#msgTip").html("<font color='#79FF79'>正在验证...</font>")
					return true;
				},
				type:'post',
				success:function(data){
					if(data.success){
						window.location.href="workbench/index.jsp";
					}else{
						//$("#msgTip").html(data.msg);
						$("#msgTip").html("<font color='#FF9797'>"+data.msg+"</font>");
					}
				}
			})
		})
	})
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; widows: 100%; height: 100%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">AsiaInfo &nbsp;<span style="font-size: 12px;">&copy;2018&nbsp;亚信科技</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" value="${cookie.loginAct.value }" class="form-control" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" value="${cookie.loginPwd.value }" class="form-control" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox" checked>
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox">
							</c:if>
							十天内免登录&nbsp;&nbsp;<span id="msgTip"></span>
							<!-- <font color="#FF9797"><span id="msgTip"></span></font> -->
						</label>
					</div>
					<button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>