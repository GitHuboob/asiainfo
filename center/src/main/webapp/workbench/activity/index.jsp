<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
<!-- 加入basePath标签后所有路径都改为WebContent下文件夹开始-->
<base href="<%=basePath %>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="styleSheet" type="text/css" href="jquery/bs_pagination/css/jquery.bs_pagination.css">
<script type="text/javascript" src="jquery/bs_pagination/js/jquery.bs_pagination.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/js/localization/en.js"></script>

<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.js"></script>

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

        $('.mydate2').datetimepicker({
            language: 'zh-CN',//显示中文
            format: 'yyyy-mm-dd hh:mm:ss',//显示格式
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
		
		//为"创建"按钮添加点击事件
		$("#createActivityBtn").click(function(){
            //重置表单
		    $("#createActivityForm")[0].reset();

		    $("#createActivityModal").modal("show");

		});
		
		//给"保存"按钮添加点击事件
		$("#saveCreateActivityBtn").click(function(){
			//收集页面参数
			//var owner = $("#create-marketActivityOwner").val();
			var name=$.trim($("#create-marketActivityName").val());
			var state=$("#create-marketActivityState").val();
            var time =$("#create-marketActivityTime").val();
			var description=$.trim($("#create-description").val());
			
			//表单验证
			if(name==null||name.length==0){
				alert("名称不能为空");
				return;
			}
			
			//发送请求
			$.ajax({
				url:'workbench/activity/saveCreateChannel',
				data:{
					//owner:owner,
					name:name,
					state:state,
                    time:time,
					description:description
				},
				type:'post',
				success:function(data){
					if(data.success){
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						
						//刷新活动列表(保留),显示第一页数据,保持每页显示条数不变
						display(1,$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert("创建失败");
						$("#createActivityModal").modal("show");
					}
				}
			})
		});
		
		//当页面加载完成之后，默认显示所有数据的第一页
		display(1,5);
		
		//给"查询"按钮添加单击事件
		$("#queryActivityBtn").click(function(){
			display(1,$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
		});
		
		//当页面加载完成之后，所有definedColumns下的复选框处于选中状态
		$("#definedColumns input[type='checkbox']").prop("checked",true);
		
		//给definedColumns下复选框添加单击事件
		$("#definedColumns input[type='checkbox']").click(function(){
			if(this.checked){
				$("td[name='"+this.name+"']").show();
			}else{
				$("td[name='"+this.name+"']").hide();
			}
		})
		
		//给"删除"按钮添加单击事件
		$("#deleteActivityBtn").click(function(){
			//表单验证
			var ckIds = $("#activityListTBody input[type='checkbox']:checked");
			if(ckIds.size()==0){
				alert("请选择要删除的记录");
				return;
			}
			if(window.confirm("你确定删除吗")){
				var ids="";
				$.each(ckIds,function(index,obj){
					ids += "id="+obj.value+"&";
				});
				ids = ids.substr(0,ids.length-1);
				//发送请求
				$.ajax({
					url:'workbench/activity/deleteActivity',
					data:ids,
					type:'post',
					success:function(data){
						if(data.success){
							display(1,$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
						}else{
							alert("删除失败");
						}
					}
				});
			}
		});

        //给"邮件"按钮添加单击事件
        $("#sendEmailBtn").click(function(){
            //表单验证
            var ckIds = $("#activityListTBody input[type='checkbox']:checked");
            if(ckIds.size()==0){
                alert("请选择要发送的记录");
                return;
            }
            if(window.confirm("你确定发送邮件吗")){
                var ids="";
                $.each(ckIds,function(index,obj){
                    ids += "id="+obj.value+"&";
                });
                ids = ids.substr(0,ids.length-1);
                //发送请求
                $.ajax({
                    url:'workbench/activity/sendEmail',
                    data:ids,
                    type:'post',
                    success:function(data){
                        if(data.success){
                            alert("发送成功");
                        }else{
                            alert("发送失败");
                        }
                    }
                });
            }
        });

        //给"短信"按钮添加单击事件
        $("#sendMessageBtn").click(function(){
            //表单验证
            var ckIds = $("#activityListTBody input[type='checkbox']:checked");
            if(ckIds.size()==0){
                alert("请选择要发送的记录");
                return;
            }
            if(window.confirm("你确定发送短信吗")){
                var ids="";
                $.each(ckIds,function(index,obj){
                    ids += "id="+obj.value+"&";
                });
                ids = ids.substr(0,ids.length-1);
                //发送请求
                $.ajax({
                    url:'workbench/activity/sendMessage',
                    data:ids,
                    type:'post',
                    success:function(data){
                        if(data.success){
                            alert("发送成功");
                        }else{
                            alert("发送失败");
                        }
                    }
                });
            }
        });


        //给"全选"按钮添加单击事件
		$("#ckd_All").click(function(){
			$("#activityListTBody input[type='checkbox']").prop("checked",this.checked);
		});
		
		//给列表中的所有复选框添加单击事件
		$("#activityListTBody").on("click","input[type='checkbox']",function(){
			if($("#activityListTBody input[type='checkbox']").size()==$("#activityListTBody input[type='checkbox']:checked").size()){
				$("#ckd_All").prop("checked",true);
			}else{
				$("#ckd_All").prop("checked",false);
			}
		});
		
		//给"修改"按钮添加单击事件
		$("#editActivityBtn").click(function(){
			//收集页面参数，表单验证
			var ckd_Id = $("#activityListTBody input[type='checkbox']:checked");
			if(ckd_Id.size()!=1){
				alert("请选择一条活动记录");
			}
			//发送请求
			$.ajax({
				url:'workbench/activity/editActitity',
				data:{
					id:ckd_Id.val()
				},
				type:'post',
				success:function(data){
					var htmlStr = "";
					$("#edit-owner").val(data.activity.GROUP_CODE);
					$("#edit-type").val(data.activity.WORK_TYPE);
					$("#edit-name").val(data.activity.JOB_NAME);
					$("#edit-state").val(data.activity.STATE);
					$("#edit-description").val(data.activity.REMARK);
					
					$("#edit-id").val(data.activity.ID);
                    $("#edit-time").val(data.activity.SEND_TIME);
					//显示模态窗口
					$("#editActivityModal").modal("show");
				}
			});
		});
		
		//给"更新"按钮添加单击事件
		$("#saveEditActivityBtn").click(function(){
			//收集页面参数
			var id=$("#edit-id").val();
			var owner=$("#edit-owner").val();
			var type=$("#edit-type").val();
			var name=$.trim($("#edit-name").val());
			var state=$("#edit-state").val();
			var description=$.trim($("#edit-description").val());

			//发送请求
			$.ajax({
				url:'workbench/activity/saveEditActitity',
				data:{
					id:id,
					owner:owner,
					type:type,
					name:name,
					state:state,
					description:description
				},
				type:'post',
				success:function(data){
					if(data.success){
						//刷新列表
						display($("#pageNoDiv").bs_pagination('getOption', 'currentPage'),$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
					}else{
						alert("修改失败!");
						$("#editActivityModal").modal("show");
					}
				}
			});
		});
		
		//给"导出"按钮添加单击事件
		$("#exportActivityBtn").click(function(){
			//获取页面参数
			var name=$("#query-name").val();
			var type=$("#query-type").val();
			var state=$("#query-state").val();
			var startDate=$("#query-startDate").val();
			var endDate=$("#query-endDate").val();
			//发送请求
            window.location.href="workbench/activity/exportExcel?name="+name
                +"&type="+type+"&state="+state+"&startDate="+startDate+"&endDate="+endDate;
		});
		
		//给"导入"按钮添加单击事件
		$("#importMarketActivityBtn").click(function(){
			//收集参数
			var fileName = $("#activityFile").val();
			var activityFile = $("#activityFile")[0].files[0];
			//表单验证
			var suffix = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
			if(!(suffix=="xls"||suffix=="xlsx")){
				alert("请检查文件格式");
				return;
			}
			if(activityFile.size>5*1024*1024){//字节
				alert("文件大小不能超过5MB");
				return;
			}
			//发送请求
			var formData = new FormData();
            formData.append("myName",fileName);
			formData.append("myFile",activityFile);
			$.ajax({
				url:'workbench/activity/importExcel',
				data:formData,
				type:'post',
				processData:false,
				contentType:false,
				success:function(data){
					if(data.success){
						//显示提示信息
						alert("成功导入"+data.count+"条数据");
						//隐藏模态窗口
						$("#importActivityModal").modal("hide");
						//刷新列表
						display(1,$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert("成功导入前"+data.count+"条，其余数据导入失败");
						$("#importActivityModal").modal("show");
					}
				}
			});
		});

		// 自动补全，模糊查询
        //var subjects = ['PHP', 'MySQL', 'SQL', 'PostgreSQL', 'HTML', 'CSS', 'HTML5', 'CSS3', 'JSON'];
        //$('#search').typeahead({source: subjects})

        var name2Id = {};//名称对应的id
        $("#autocomplate").typeahead({
            source: function (query, process) {
                return $.ajax({
                    url:'workbench/activity/queryChannelInfoByAutoComplate',
                    data:{
                        name:query//query是输入的值
                    },
                    type:'post',

                    success:function(data){
                        var array = [];
                        $.each(data.dataList, function (index, obj) {
                            name2Id[obj.JOB_NAME] = obj.ID;//键值对保存下来。
                            array.push(obj.JOB_NAME);
                        });
                        return process(array);
                    }
                });
            },
            items: 8,
            afterSelect: function (item) {
                //console.log(name2Id[item]);//打印对应的id
                //alert(name2Id[item]);//打印对应的id
                $("#query-name").val(item);
                display(1,$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
            },
            delay: 300
        });
	});
	
	//显示列表函数
	function display(pageNo,pageSize){
		//获取页面参数
		var name=$("#query-name").val();
		var type=$("#query-type").val();
		var state=$("#query-state").val();
		var startDate=$("#query-startDate").val();
		var endDate=$("#query-endDate").val();
		//var pageNo=1;
		//var pageSize=10;
		//发送请求
		$.ajax({
			url:'workbench/activity/queryChannelInfo',
			data:{
				name:name,
				type:type,
				state:state,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			success:function(data){
				/* //设置总条数
				$("#totalRows").html(data.count); */
				//设置列表
				var htmlStr="";
				$.each(data.dataList,function(index,obj){
					htmlStr+="<tr>";
					htmlStr+="<td><input value='"+obj.ID+"' type='checkbox' /></td>";
					htmlStr+="<td name='name'><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href=\"workbench/activity/detailMarketActivity.do?id="+obj.id+"\";'>"+obj.JOB_NAME+"</a></td>";
					
					if($("#definedColumns input[name='type']").prop("checked")){
						htmlStr+="<td name='type'>"+obj.WORK_TYPE+"</td>";
					}else{
						htmlStr+="<td name='type' style='display:none'>"+obj.WORK_TYPE+"</td>";
					}
					
					htmlStr+="<td name='state'>"+obj.STATE+"</td>";
					htmlStr+="<td name='owner'>"+obj.GROUP_CODE+"</td>";
					htmlStr+="<td name='createTime'>"+obj.NEXT_START_TIME+"</td>";
					htmlStr+="<td name='description'>"+obj.REMARK+"</td>";
					htmlStr+="</tr>";
				});
				$("#activityListTBody").html(htmlStr);
				//隔行换颜色
				$("#activityListTBody tr:even").addClass("active");
				//显示分页信息
				var totalPages = 1;
				if(data.count%pageSize==0){
					totalPages = data.count/pageSize;
				}else{
					totalPages = parseInt(data.count/pageSize) + 1;
				}
				$("#pageNoDiv").bs_pagination({
					currentPage:pageNo,//当前页
					rowsPerPage:pageSize,//每页显示条数
					totalRows:data.count,//总条数
					totalPages:totalPages,//总页数(必须手动计算)
					visiblePageLinks:5,//最多可以显示的卡片数
					showGoToPage:false,//是否显示跳转到第几页
					showRowsPerPage:true,//是否显示每页显示条数
					showRowsInfo:true,//是否显示记录信息
					//当页号改变的时候,执行指定函数
					//event就代表页号改变的事件,pageObj代表页信息
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

	<!-- 创建活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
							<label for="create-marketActivityType" class="col-sm-2 control-label">时间</label>
							<div class="col-sm-10" style="width: 300px;">
                                <input class="form-control mydate2" type="text" id="create-marketActivityTime"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-marketActivityState" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityState">
								  <option></option>
                                  <option value="1">已到</option>
                                  <option value="2">未到 未超时</option>
                                  <option value="3">未到 已超时</option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveCreateActivityBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<!-- 添加一个隐藏input来保存当前记录的id -->
							<input type="hidden" id="edit-id">

							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-owner" readonly>
							</div>
							<label for="edit-marketActivityType" class="col-sm-2 control-label">类型</label>
							<div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-type" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" readonly>
							</div>
							<label for="edit-marketActivityState" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
								    <option></option>
                                    <option value = "1">1</option>
                                    <option value = "2">2</option>
                                    <option value = "3">3</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">活动Marketing，是指品牌主办或参与的展览会议与公关活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveEditActivityBtn" type="button" class="btn btn-primary">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- 导入活动的模态窗口 -->
	<div class="modal fade" id="importActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">导入活动</h4>
				</div>
				<div class="modal-body" style="height: 350px;">
					<div style="position: relative;top: 20px; left: 50px;">
						请选择要上传的文件：<small style="color: gray;">[仅支持.xls或.xlsx格式]</small>
					</div>
					<div style="position: relative;top: 40px; left: 50px;">
						<input type="file" id="activityFile">
					</div>
					<div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
						<h3>重要提示</h3>
						<ul>
							<li>给定文件的第一行将视为字段名。</li>
							<li>请确认您的文件大小不超过5MB。</li>
							<li>从XLS/XLSX文件中导入全部重复记录之前都会被忽略。</li>
							<li>复选框值应该是1或者0。</li>
							<li>日期值必须为MM/dd/yyyy格式。任何其它格式的日期都将被忽略。</li>
							<li>日期时间必须符合MM/dd/yyyy hh:mm:ss的格式，其它格式的日期时间将被忽略。</li>
							<li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
							<li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
						</ul>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<!-- <button type="button" class="btn btn-primary" data-dismiss="modal">导入</button> -->
					<button id="importMarketActivityBtn" type="button" class="btn btn-primary">导入</button>
				</div>
			</div>
		</div>
	</div>
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		<%--<div style="width: 130%; position: absolute;top: 5px; left: 10px;">--%>
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control"  id="query-type">
					  	  <option selected = "selected"></option>
                          <option value = "D">日接口</option>
                          <option value = "M">月接口</option>
					  </select>
				    </div>
				  </div>
				  
				  <%--<br>--%>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">状态</div>
					  <select class="form-control" id="query-state">
                        <%--<option selected = "selected"></option>--%>
						<option selected = "selected"></option>
                        <option value = "1">文件已到</option>
                        <option value = "2">文件未到（未超时）</option>
                        <option value = "3">文件未到（已超时）</option>
					  </select>
				    </div>
				  </div>

                  <br>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control mydate" type="text" id="query-startDate"/>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control mydate" type="text" id="query-endDate">
				    </div>
				  </div>

				  <button id="queryActivityBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createActivityBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editActivityBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteActivityBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

				<div class="btn-group" style="position: relative; top: 18%;">
					<button id="sendEmailBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-envelope"></span> 邮件</button>
					<button id="sendMessageBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-phone"></span> 短信</button>
				</div>

				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal"><span class="glyphicon glyphicon-import"></span> 导入</button>
				  <button id="exportActivityBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 导出</button>
				</div>
				
				<div class="btn-group" style="position: relative; top: 18%; left: 5px;">
					<button type="button" class="btn btn-default">添加字段</button>
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
						<span class="sr-only">Toggle Dropdown</span>
					</button>
					<ul id="definedColumns" class="dropdown-menu" role="menu"> 
						<li><a href="javascript:void(0);"><input name="name" type="checkbox"/> 名称</a></li>
						<li><a href="javascript:void(0);"><input name="type" type="checkbox"/> 类型</a></li>
						<li><a href="javascript:void(0);"><input name="state" type="checkbox"/> 状态</a></li>
						<li><a href="javascript:void(0);"><input name="owner" type="checkbox"/> 所有者</a></li>
						<li><a href="javascript:void(0);"><input name="createTime" type="checkbox"/> 下一次执行时间</a></li>
						<li><a href="javascript:void(0);"><input name="description" type="checkbox"/> 描述</a></li>
					</ul>
				</div>

				<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
					<form class="form-inline" role="form">
					  <div class="form-group has-feedback">
					     <%--<input type="email" class="form-control" style="width: 300px;" placeholder="支持任何字段搜索">--%>
                         <input id="autocomplate" autocomplete="off" data-provide="typeahead" type="text"
                                class="form-control" style="width: 300px;" placeholder="模糊查询接口名称" />
					     <span class="glyphicon glyphicon-search form-control-feedback"></span>
                         <%--<input type="text" class="span3" id="search" data-provide="typeahead" data-items="4" />--%>
					  </div>
					</form>
				</div>
			</div>
			<div style="position: relative;top: 10px;">
            <%--<div style="position: relative;top: 10px; width: 70%">--%>
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="ckd_All" type="checkbox" /></td>
							<td name="name">名称</td>
							<td name="type">类型</td>
							<td name="state">状态</td>
							<td name="owner">组信息</td>
							<td name="createTime">下次执行时间</td>
							<td name="description">描述</td>
						</tr>
					</thead>
					<tbody id="activityListTBody">

					</tbody>
				</table>
				<div id="pageNoDiv"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>