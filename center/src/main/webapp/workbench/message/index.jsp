<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
<base href="<%=basePath %>">
<meta charset="UTF-8">

    <style>
        .W10 .th-inner {
            width:10px !important;
        }
    </style>

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/css/jquery.bs_pagination.css">
<script type="text/javascript" src="jquery/bs_pagination/js/jquery.bs_pagination.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/js/localization/en.js"></script>

<script type="text/javascript">

	$(function(){

        //当页面加载完成后
        $('.mydate').datetimepicker({
            language: 'zh-CN',//显示中文
            format: 'yyyy-mm-dd',//显示格式
            minView: "month",//设置只显示到月份
            initialDate: new Date(),//初始化当前日期
            autoclose: true,//选中自动关闭
            todayBtn: true,//显示今日按钮
            clearBtn:true//显示清除按钮
        });

		
		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });

		//当页面加载完成之后,默认显示所有数据的第一页
		display(1,5);
		
		//给"查询"按钮添加单击事件
		$("#queryContactsForPageByConditionBtn").click(function(){
			display(1,$("#paginationDiv").bs_pagination('getOption', 'rowsPerPage'));
		});

		//给"全选"添加单击事件
		$("#ckd_all").click(function(){
			$("#contactsListTbody input[type='checkbox']").prop("checked",this.checked);
		});
		//给列表中所有复选框添加单击事件
		$("#contactsListTbody").on("click","input[type='checkbox']",function(){
			if($("#contactsListTbody input[type='checkbox']").size()==$("#contactsListTbody input[type='checkbox']:checked").size()){
				$("#ckd_all").prop("checked",true);
			}else{
				$("#ckd_all").prop("checked",false);
			}
		});

		//页面加载后,使"添加字段"下拉列表中所有复选框处于选中状态
		$("#definedColumns input[type='checkbox']").prop("checked",true);
		//给"添加字段"下拉列表中所有复选框添加单击事件
		$("#definedColumns input[type='checkbox']").click(function(){
			if(this.checked){
				$("td[name='"+this.name+"']").show();
			}else{
				$("td[name='"+this.name+"']").hide();
			}
		});

	});
	
	function display(pageNo,pageSize){
        //收集参数
        var tableName = $("#query-tableName").val();
        var createTime = $("#query-createTime").val();

		//发送请求
		$.ajax({
            url:'workbench/messages/queryMessageForPageByCondition',
			data:{
                tableName:tableName,
                createTime:createTime,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			success:function(data){

				var htmlStr = "";
				$.each(data.dataList,function(index,obj){
					htmlStr += "<tr>";
					htmlStr += "<td><input value='"+obj.messageId+"' type='checkbox' /></td>";

					htmlStr += "<td name='messageId'>"+obj.messageId+"</td>";
					htmlStr += "<td name='tableName'><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href=\"workbench/contacts/detailContacts/"+obj.messageId+"\";'>"+obj.tableName+"</a></td>";
					if($("#definedColumns input[name='createTime']").prop("checked")){
						htmlStr += "<td name='createTime'>"+obj.createTime+"</td>";
					}else{
						htmlStr += "<td name='createTime' style='display:none'>"+obj.createTime+"</td>";
					}
					htmlStr += "<td name='repeatCount'>"+obj.repeatCount+"</td>";
					htmlStr += "<td name='remark'>"+obj.remark+"</td>";
                    htmlStr += "<td name='orderName'>"+obj.orderName+"</td>";
					htmlStr += "</tr>";
				});
				$("#contactsListTbody").html(htmlStr);
				
				//隔行换色
				$("#contactsListTbody tr:even").attr("class","active");

				//计算总页数
				var totalPages = 1;
				if(data.count%pageSize==0){
					totalPages = data.count/pageSize;
				}else{
					totalPages = parseInt(data.count/pageSize) + 1;
				}
				$("#paginationDiv").bs_pagination({
					  currentPage: pageNo,
					  rowsPerPage: pageSize,
					  totalPages: totalPages,
					  totalRows: data.count,
					 
					  visiblePageLinks: 5,
					 
					  showGoToPage: false,
					  showRowsPerPage: true,
					  showRowsInfo: true,
					 
					  onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
					  	display(pageObj.currentPage,pageObj.rowsPerPage);
					  }
				});
			}
		});
	}
</script>
</head>
<body>
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>短信息列表</h3>
			</div>
		</div>
	</div>

    <div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

        <div style="width: 100%; position: absolute;top: 5px; left: 10px;">
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">表名</div>
				      <input class="form-control" type="text" id="query-tableName">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">创建时间</div>
				      <input class="form-control mydate" type="text" id="query-createTime">
				    </div>
				  </div>

				  <button id="queryContactsForPageByConditionBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>

			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button disabled type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button disabled type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button disabled type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button disabled type="button" class="btn btn-default" data-toggle="modal" data-target="#importContactsModal"><span class="glyphicon glyphicon-import"></span> 导入</button>
				  <button disabled type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 导出</button>
				</div>
				
				<div class="btn-group" style="position: relative; top: 18%; left: 5px;">
					<button type="button" class="btn btn-default">添加字段</button>
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
						<span class="sr-only">Toggle Dropdown</span>
					</button>
					<ul id="definedColumns" class="dropdown-menu" role="menu"> 
						<li><a href="javascript:void(0);"><input name="messageId" type="checkbox"/> 序号</a></li>
						<li><a href="javascript:void(0);"><input name="tableName" type="checkbox"/> 表名</a></li>
						<li><a href="javascript:void(0);"><input name="createTime" type="checkbox"/> 创建时间</a></li>
						<li><a href="javascript:void(0);"><input name="repeatCount" type="checkbox"/> 重复次数</a></li>
						<li><a href="javascript:void(0);"><input name="remark" type="checkbox"/> 备注</a></li>
					</ul>
				</div>

				<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
					<form class="form-inline" role="form">
					  <div class="form-group has-feedback">
					    <input readonly type="text" class="form-control" style="width: 300px;" placeholder="支持任何字段搜索">
					    <span class="glyphicon glyphicon-search form-control-feedback"></span>
					  </div>
					</form>
				</div>
			</div>
			<div style="position: relative;top: 20px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="ckd_all"/></td>
                            <td name="messageId">序号</td>
							<td name="tableName">表名</td>
							<td name="createTime">创建时间</td>
							<td name="repeatCount">重复次数</td>
                            <td name="remark">订单编码</td>
                            <td name="orderName">订单名称</td>
						</tr>
					</thead>
					<tbody id="contactsListTbody">

					</tbody>
				</table>
				<div id="paginationDiv"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>