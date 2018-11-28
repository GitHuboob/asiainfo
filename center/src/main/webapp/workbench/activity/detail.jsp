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

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		$("#remarkDivList").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		});
		$("#remarkDivList").on("mouseout",".remarkDiv",function(){
			$(this).children("div").children("div").hide();
		});
		$("#remarkDivList").on("mouseover",".myHref",function(){
			$(this).children("span").css("color","red");
		});
		$("#remarkDivList").on("mouseout",".myHref",function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		//给市场活动备注"保存"按钮绑定单击事件
		$("#saveCreateRemarkBtn").click(function(){
			//收集页面参数
			var noteContent = $.trim($("#remark").val());
			var activityId = '${activity.id}';
			//表单验证
			if(noteContent==null||noteContent.length==0){
				alert("备注内容不能为空!");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/activity/saveCreateMarketActivityRemark.do',
				data:{
					noteContent:noteContent,
					activityId:activityId
				},
				type:'post',
				success:function(data){
					if(data.success){
						//清空输入框
						$("#remark").val('');
						//刷新备注列表
						var htmlStr="";
						htmlStr+="<div id='div_"+data.remark.id+"' class='remarkDiv' style='height: 60px;'>";
						htmlStr+="<img title='${user.name }' src='image/user-thumbnail.png' style='width: 30px; height:30px;'>";
						htmlStr+="<div style='position: relative; top: -40px; left: 40px;' >";
						htmlStr+="<h5>"+data.remark.noteContent+"</h5>";
						htmlStr+="<font color='gray'>市场活动</font> <font color='gray'>-</font> <b>${activity.name }</b> <small style='color: gray;'> "+data.remark.noteTime+" 由${user.name}创建</small>";
						htmlStr+="<div style='position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;'>";
						htmlStr+="<a name='edit_A' remark-id='"+data.remark.id+"' class='myHref' href='javascript:void(0);'><span class='glyphicon glyphicon-edit' style='font-size: 20px; color: #E6E6E6;'></span></a>";
						htmlStr+="&nbsp;&nbsp;&nbsp;&nbsp;";
						htmlStr+="<a name='delete_B' remark-id='"+data.remark.id+"' class='myHref' href='javascript:void(0);'><span class='glyphicon glyphicon-remove' style='font-size: 20px; color: #E6E6E6;'></span></a>";
						htmlStr+="</div>";
						htmlStr+="</div>";
						htmlStr+="</div>";
						$("#remarkDiv").before(htmlStr);
					}else{
						alert("添加失败!");
					}
				}
			});
		});
		
		//给市场活动备注"删除"按钮添加单击事件
		$("#remarkDivList").on("click","a[name='delete_B']",function(){
			//使用attr收集自定义参数 
			var id=$(this).attr("remark-id");
			$.ajax({
				url:'workbench/activity/deleteMarketActivityRemark.do',
				data:{
					id:id
				},
				type:'post',
				success:function(data){
					if(data.success){
						$("#div_"+id).remove();
					}else{
						alert("删除失败");
					}
				}
			})
		});
		
		//给市场活动备注"编辑"按钮添加单击事件
		$("#remarkDivList").on("click","a[name='edit_A']",function(){
			//收集参数
			var id = $(this).attr("remark-id");
			var noteContent = $("#div_"+id+" h5").html();
			//将参数显示到模态窗口中
			$("#edit-id").val(id);
			$("#edit-noteContent").val(noteContent);
			$("#editActivityRemarkModal").modal("show");
		});
		
		//给市场活动备注"更新"按钮添加单击事件
		$("#saveEditRemarkBtn").click(function(){
			//收集参数
			var id = $("#edit-id").val();
			var noteContent = $.trim($("#edit-noteContent").val());
			//表单验证
			if(noteContent==null||noteContent.length==0){
				alert("备注内容不能为空!");
				return;
			}
			//发送请求
			$.ajax({
				url:'workbench/activity/saveEditMarketActivityRemark.do',
				data:{
					id:id,
					noteContent:noteContent
				},
				type:'post',
				success:function(data){
					if(data.success){
						$("#editActivityRemarkModal").modal("hide");
						//刷新备注列表
						$("#div_"+id+" h5").html(noteContent);
						$("#div_"+id+" small").html(" "+data.remark.editTime+" 由${user.name}修改");
					}else{
						alert("更新失败");
					}
				}
			});
		});
		
		//给市场活动明细"编辑"按钮添加单击事件
		$("#editActivityDetailBtn").click(function(){
			var id = '${activity.id}';
			$.ajax({
				url:'workbench/activity/editMarketActivity.do',
				data:{
					id:id					
				},
				type:'post',
				success:function(data){
					var htmlStr = "";
					$.each(data.userList,function(index,obj){
						if(obj.name=="${user.name}"){
							htmlStr += "<option value='"+obj.id+"' selected>"+obj.name+"</option>";
						}else{
							htmlStr += "<option value='"+obj.id+"'>"+obj.name+"</option>";
						}
					});
					$("#edit-marketActivityOwner").html(htmlStr);
					
					$("#edit-marketActivityType").val(data.activity.type);
					$("#edit-marketActivityName").val(data.activity.name);
					$("#edit-marketActivityState").val(data.activity.state);
					$("#edit-startTime").val(data.activity.startDate);
					$("#edit-endTime").val(data.activity.endDate);
					$("#edit-actualCost").val(data.activity.actualCost);
					$("#edit-budgetCost").val(data.activity.budgetCost);
					$("#edit-describe").val(data.activity.description);
					
					$("#edit-id").val(id);
					$("#editActivityModal").modal("show");
				}
			});
		});
		
		//给修改市场活动明细的"更新"按钮添加单击事件
		$("#saveEditActivityDetailBtn").click(function(){
			//收集参数
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var type = $("#edit-marketActivityType").val();
			var name = $("#edit-marketActivityName").val();
			var state = $("#edit-marketActivityState").val();
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var actualCost = $("#edit-actualCost").val();
			var budgetCost = $("#edit-budgetCost").val();
			var description = $("#edit-describe").val();
			//表单验证(未完成)
			
			//发送请求
			$.ajax({
				url:'workbench/activity/saveEditMarketActivity.do',
				data:{
					id:id,
					owner:owner,
					type:type,
					name:name,
					state:state,
					startDate:startDate,
					endDate:endDate,
					budgetCost:budgetCost,
					actualCost:actualCost,
					description:description
				},
				type:'post',
				success:function(data){
					if(data.success){
						//刷新列表(未完成)
						
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
					}else{
						alert("修改失败!");
						$("#editActivityModal").modal("show");
					}
				}
			});
		});
	});

</script>

</head>
<body>
	<!-- 修改备注的模态窗口 -->
	<div class="modal fade" id="editActivityRemarkModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveEditRemarkBtn" type="button" class="btn btn-primary">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								</select>
							</div>
							<label for="edit-marketActivityType" class="col-sm-2 control-label">类型</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityType">
								  <option></option>
								  <c:if test="${not empty activityTypeList }">
								  	<c:forEach var="at" items="${activityTypeList }">
								  		<option value="${at.id }">${at.text }</option>
								  	</c:forEach>
								  </c:if>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
							</div>
							<label for="edit-marketActivityState" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityState">
								  <option></option>
								  <c:if test="${not empty activityStateList }">
								  	<c:forEach var="as" items="${activityStateList }">
								  		<option value="${as.id }">${as.text }</option>
								  	</c:forEach>
								  </c:if>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-actualCost" class="col-sm-2 control-label">实际成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-actualCost" value="4,000">
							</div>
							<label for="edit-budgetCost" class="col-sm-2 control-label">预算成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-budgetCost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<!-- <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button> -->
					<button id="saveEditActivityDetailBtn" type="button" class="btn btn-primary">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name } <small>${activity.startDate } ~ ${activity.endDate }</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<!-- <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button> -->
			<button id="editActivityDetailBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner }</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.type }</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.name }</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">状态</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.state }</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate }</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate }</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">实际成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.actualCost }</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预算成本</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.budgetCost }</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy }&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime }</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy }&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime }</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description }
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<c:if test="${not empty remarkList }">
			<c:forEach var="remark" items="${remarkList }">
				<div id="div_${remark.id }" class="remarkDiv" style="height: 60px;">
					<img title="${remark.notePerson }" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
					<div style="position: relative; top: -40px; left: 40px;" >
						<h5>${remark.noteContent }</h5>
						<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name }</b> <small style="color: gray;"> ${remark.editFlag==0?remark.noteTime:remark.editTime } 
						由${remark.editFlag==0?remark.notePerson:remark.editPerson }${remark.editFlag==0?'创建':'编辑' } </small>
						<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
							<a remark-id="${remark.id }" name="edit_A" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a remark-id="${remark.id }" name="delete_B" class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="saveCreateRemarkBtn" type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>