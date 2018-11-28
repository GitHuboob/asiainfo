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

<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.js"></script>

<script type="text/javascript">

	$(function(){
		
		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		
		//给"创建"联系人按钮添加单击事件
		$("#createContactsBtn").click(function(){
            $("#createContactsModal").modal("show");
			/*$.ajax({
                url:'workbench/contacts/queryGroupCode',
				type:'post',
				success:function(data){
					var htmlStr = "";
					htmlStr += "<option></option>";
					$.each(data,function(index,obj){
						htmlStr += "<option value='"+obj.groupId+"'>"+obj.groupCode+"</option>";
					});
					$("#create-source").html(htmlStr);
					
					$("#createContactsModal").modal("show");
				}
			});*/
		});
		
		//给"保存"创建的联系人按钮添加单击事件
		$("#saveCreateContactsBtn").click(function(){
			//收集参数
			//var source=$("#create-source").val();
			var fullName=$("#create-fullName").val();
			var email=$("#create-email").val();
			var mphone=$("#create-mphone").val();
			var description=$("#create-description").val();

			//发送请求
			$.ajax({
				url:'workbench/contacts/saveContacts',
				data:{
					//source:source,
					fullName:fullName,
					email:email,
					mphone:mphone,
					description:description
				},
				type:'post',
				success:function(data){
					if(data.success){
						$("#createContactsModal").modal("hide");
						//刷新列表
						display(1,$("#paginationDiv").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert("保存失败")
						$("#createContactsModal").modal("show");
					}
				}
			});
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

        //给"修改"按钮添加单击事件
        $("#editContactsBtn").click(function(){
            var ckd_Id = $("#contactsListTbody input[type='checkbox']:checked");
            if(ckd_Id.size()!=1){
                alert("请选择一条活动记录");
                return;
            }
            //发送请求
            $.ajax({
                url:'workbench/contacts/editContacts',
                data:{
                    id:ckd_Id.val()
                },
                type:'post',
                success:function(data){
                    var htmlStr = "";
                    /*$.each(data.userList,function(index,obj){
                        if(obj.groupId == data.activity.staffId){
                            htmlStr += "<option value='"+obj.groupId+"' selected>"+obj.groupCode+"</option>";
                        }else{
                            htmlStr += "<option value='"+obj.groupId+"'>"+obj.groupCode+"</option>";
                        }
                    })
                    $("#edit-source").html(htmlStr);*/

                    $("#edit-surname").val(data.activity.staffName);
                    $("#edit-email").val(data.activity.staffEmail);
                    $("#edit-mphone").val(data.activity.staffPhone);
                    $("#edit-describe").val(data.activity.remark);

                    $("#edit-id").val(data.activity.staffId);
                    //显示模态窗口
                    $("#editContactsModal").modal("show");
                }
            });
        });

        //给"更新"按钮添加单击事件
        $("#saveEditContactsBtn").click(function(){
            //收集页面参数
            var id=$("#edit-id").val();
            //var source = $("#edit-source").val();
            var name=$.trim($("#edit-surname").val());
            var email=$("#edit-email").val();
            var phone=$("#edit-mphone").val();
            var description=$.trim($("#edit-describe").val());

            //发送请求
            $.ajax({
                url:'workbench/contacts/saveEditContacts',
                data:{
                    id:id,
                    //source:source,
                    name:name,
                    email:email,
                    phone:phone,
                    description:description
                },
                type:'post',
                success:function(data){
                    if(data.success){
                        //关闭模态窗口
                        $("#editContactsModal").modal("hide");
                        //刷新列表
                        display(1,5);
                        //display($("#pageNoDiv").bs_pagination('getOption', 'currentPage'),$("#pageNoDiv").bs_pagination('getOption', 'rowsPerPage'));
                    }else{
                        alert("修改失败!");
                        $("#editContactsModal").modal("show");
                    }
                }
            });
        });
		
		//给联系人"删除"按钮添加单击事件
		$("#deleteContactsBtn").click(function(){
			//收集参数
			var ckdIds = $("#contactsListTbody input[type='checkbox']:checked");
			if(ckdIds.size()==0){
				alert("请选择要删除的联系人");
				return;
			}
			if(window.confirm("你确定删除吗")){
				var ids = "";
				$.each(ckdIds,function(index,obj){
					ids += "id="+obj.value+"&";
				})
				ids = ids.substr(0,ids.length-1);
				//发送请求
				$.ajax({
					url:'workbench/contacts/deleteContacts',
					data:ids,
					type:'post',
					success:function(data){
						if(data.success){
							display(1,$("#paginationDiv").bs_pagination('getOption','rowsPerPage'));
						}else{
							alert("删除失败");
						}
					}
				})
			}
		});

        //给"导出"按钮添加单击事件
        $("#exportContactsBtn").click(function(){
            //获取页面参数
            var name=$("#query-fullName").val();
            //发送请求
            window.location.href="workbench/contacts/exportExcel?name="+name;
        });

        //给"导入"按钮添加单击事件
        $("#importContactsBtn").click(function(){
            //收集参数
            var fileName = $("#contactsFile").val();
            var contactsFile = $("#contactsFile")[0].files[0];
            //表单验证
            var suffix = fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase();
            if(!(suffix=="xls"||suffix=="xlsx")){
                alert("请检查文件格式");
                return;
            }
            if(contactsFile.size>5*1024*1024){//字节
                alert("文件大小不能超过5MB");
                return;
            }
            //发送请求
            var formData = new FormData();
            formData.append("myName",fileName);
            formData.append("myFile",contactsFile);
            $.ajax({
                url:'workbench/contacts/importExcel',
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
        var name2Id = {};//名称对应的id
        $("#autocomplate").typeahead({
            source: function (query, process) {
                return $.ajax({
                    url:'workbench/contacts/queryStaffInfoByAutoComplate',
                    data:{
                        name:query//query是输入的值
                    },
                    type:'post',
                    success:function(data){
                        var array = [];
                        $.each(data.dataList,function(index,obj){
                            name2Id[obj.staffName] = obj.staffId;//键值对保存下来。
                            array.push(obj.staffName);
                        });
                        return process(array);
                    }
                });
            },
            items: 8,
            afterSelect: function (item) {
                //console.log(name2Id[item]);//打印对应的id
                //alert(name2Id[item]);//打印对应的id
                $("#query-fullName").val(item);
                display(1,$("#paginationDiv").bs_pagination('getOption', 'rowsPerPage'));
            },
            delay: 300
        });
	});

	function display(pageNo,pageSize){
              //收集参数
        var fullName = $("#query-fullName").val();

		//发送请求
		$.ajax({
            url:'workbench/contacts/queryStaffByGroupCode',
			data:{
				fullName:fullName,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			success:function(data){

				var htmlStr = "";
				$.each(data.dataList,function(index,obj){
					htmlStr += "<tr>";
					htmlStr += "<td><input value='"+obj.staffId+"' type='checkbox' /></td>";
					htmlStr += "<td name='fullName'><a style='text-decoration: none; cursor: pointer;' onclick='window.location.href=\"workbench/contacts/detailContacts/"+obj.staffId+"\";'>"+obj.staffName+"</a></td>";
					if($("#definedColumns input[name='email']").prop("checked")){
						htmlStr += "<td name='email'>"+obj.staffEmail+"</td>";
					}else{
						htmlStr += "<td name='email' style='display:none'>"+obj.staffEmail+"</td>";
					}
					htmlStr += "<td name='mphone'>"+obj.staffPhone+"</td>";
					htmlStr += "<td name='createTime'>"+obj.createTime+"</td>";
					htmlStr += "<td name='description'>"+obj.remark+"</td>";
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

	
	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
                            <label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-fullName">
                            </div>
						</div>
						
						<div class="form-group">
                            <label for="create-email" class="col-sm-2 control-label">邮箱<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-email">
                            </div>
                            <label for="create-mphone" class="col-sm-2 control-label">手机<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
						</div>
						
						<div class="form-group" style="position: relative;">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>

					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveCreateContactsBtn" type="button" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						
						<div class="form-group">
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname" value="">
							</div>

						</div>
						
						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
							</div>
						</div>

                        <!-- 添加一个隐藏input来保存当前记录的id -->
                        <input type="hidden" id="edit-id">

					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="saveEditContactsBtn" type="button" class="btn btn-primary">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- 导入联系人的模态窗口 -->
	<div class="modal fade" id="importContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">导入联系人</h4>
				</div>
				<div class="modal-body" style="height: 350px;">
					<div style="position: relative;top: 20px; left: 50px;">
						请选择要上传的文件：<small style="color: gray;">[仅支持.xls或.xlsx格式]</small>
					</div>
					<div style="position: relative;top: 40px; left: 50px;">
						<input type="file" id="contactsFile">
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
					<button  id="importContactsBtn"  type="button" class="btn btn-primary" data-dismiss="modal">导入</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>联系人列表</h3>
			</div>
		</div>
	</div>
	

    <div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

        <div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		<%--<div style="width: 200%; position: absolute;top: 5px; left: 10px;">--%>

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">姓名</div>
				      <input class="form-control" type="text" id="query-fullName">
				    </div>
				  </div>

				  <button id="queryContactsForPageByConditionBtn" type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createContactsBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editContactsBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteContactsBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importContactsModal"><span class="glyphicon glyphicon-import"></span> 导入</button>
				  <button id="exportContactsBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 导出</button>
				</div>
				
				<div class="btn-group" style="position: relative; top: 18%; left: 5px;">
					<button type="button" class="btn btn-default">添加字段</button>
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret"></span>
						<span class="sr-only">Toggle Dropdown</span>
					</button>
					<ul id="definedColumns" class="dropdown-menu" role="menu"> 
						<li><a href="javascript:void(0);"><input name="fullName" type="checkbox"/> 姓名</a></li>
						<li><a href="javascript:void(0);"><input name="email" type="checkbox"/> 邮箱</a></li>
						<li><a href="javascript:void(0);"><input name="mphone" type="checkbox"/> 手机</a></li>
						<li><a href="javascript:void(0);"><input name="createTime" type="checkbox"/> 创建时间</a></li>
						<li><a href="javascript:void(0);"><input name="description" type="checkbox"/> 描述</a></li>
					</ul>
				</div>

				<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
					<form class="form-inline" role="form">
					  <div class="form-group has-feedback">
					    <%--<input type="text" class="form-control" style="width: 300px;" placeholder="支持任何字段搜索">--%>
						<input id="autocomplate" autocomplete="off" data-provide="typeahead" type="text"
							class="form-control" style="width: 300px;" placeholder="模糊查询联系人名称" />
					    <span class="glyphicon glyphicon-search form-control-feedback"></span>
					  </div>
					</form>
				</div>
			</div>
			<div style="position: relative;top: 20px;">
				<%--<div style="position: relative;top: 20px; width: 50%">--%>
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="ckd_all"/></td>
							<td name="fullName">姓名</td>
							<td name="email">邮箱</td>
							<td name="mphone">手机</td>
							<td name="createTime">创建时间</td>
							<td name="description">描述</td>
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