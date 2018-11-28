<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=basePath %>">

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

            $(".remarkDiv").mouseover(function(){
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function(){
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function(){
                $(this).children("span").css("color","red");
            });

            $(".myHref").mouseout(function(){
                $(this).children("span").css("color","#E6E6E6");
            });

            //给联系人明细"删除"按钮添加单击事件
            $("#deleteContactsDetailBtn").click(function(){
                var id = '${staff.staffId}';
                $.ajax({
                    url:'workbench/contacts/deleteContacts',
                    data:{
                        id:id
                    },
                    type:'post',
                    success:function(data){
                        if(data.success){
                            document.location.href = "javascript:history.go(-1);"
                        }else{
                            alert("删除失败");
                        }
                    }
                });
            });

            //给联系人明细"编辑"按钮添加单击事件
            $("#editContactsDetailBtn").click(function(){
                var id = '${staff.staffId}';
                $.ajax({
                    url:'workbench/contacts/editContactsDetail',
                    data:{
                        id:id
                    },
                    type:'post',
                    success:function(data){

                        $("#edit-surname").val(data.staff.staffName);
                        $("#edit-email").val(data.staff.staffEmail);
                        $("#edit-mphone").val(data.staff.staffPhone);
                        $("#edit-describe").val(data.staff.remark);

                        $("#edit-id").val(id);
                        $("#editContactsModal").modal("show");
                    }
                });
            });

            //给"更新"按钮添加单击事件
            $("#saveCreateContactsBtn").click(function(){
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
                            //后退到上一页
                            document.location.href = "javascript:history.go(-1);"
                        }else{
                            alert("修改失败!");
                        }
                    }
                });
            });

            //给"解除关联"按钮添加单击事件
            $("#groupListDiv").on("click","a[name='edit_A']",function(){
                //收集参数
                var id = $(this).attr("group-id");
                //将参数显示到模态窗口中
                $("#unbund-group-id").val(id);
                $("#unbundActivityModal").modal("show");
            });

            //给"解除"按钮添加单击事件
            $("#unbundRelationBtn").click(function(){
                var staffId = '${staff.staffId}';
                var groupId = $("#unbund-group-id").val();

                $.ajax({
                    url:'workbench/contacts/unbundRelation',
                    data:{
                        staffId:staffId,
                        groupId:groupId
                    },
                    type:'post',
                    success:function(data){
                        if(data.success){
                            //$("#tr_"+groupId).remove();
                            document.location.href = "workbench/contacts/detailContacts/"+staffId;
                        }else{
                            alert("删除失败");
                        }
                    }
                })
            });


            $("#createUnbundContactsBtn").click(function(){
                //收集参数
                var staffId = '${staff.staffId}';
                var ckdIds = $("#groupListTbody input[type='checkbox']:checked");
                if(ckdIds.size()==0){
                    alert("请选择要绑定的组信息");
                    return;
                }
                if(window.confirm("你确定绑定吗")){
                    var ids = "";
                    $.each(ckdIds,function(index,obj){
                        ids += "id="+obj.value+"&";
                    })
                    ids = ids.substr(0,ids.length-1);
                    //发送请求
                    $.ajax({
                        url:'workbench/contacts/bundRelation',
                        data: {
                            staffId: staffId,
                            ids: ids
                        },
                        type:'post',
                        success:function(data){
                            if(data.success){
                                alert("成功")
                                $("#bundActivityModal").modal("hide");
                                document.location.href = "workbench/contacts/detailContacts/"+staffId;
                            }else{
                                alert("绑定失败");
                            }
                        }
                    })
                }
            });
        });

        function bund_method(){
            $.ajax({
                url:'workbench/contacts/queryGroupCode',
                type:'post',
                success:function(data){
                    var htmlStr = "";
                    $.each(data,function(index,obj){
                        htmlStr += "<tr>";
                        htmlStr += "<td><input value="+obj.groupId+" type='checkbox' /></td>";
                        htmlStr += "<td>"+obj.groupCode+"</td>";
                        htmlStr += "<td>"+obj.groupName+"</td>";
                        htmlStr += "<td>"+obj.remark+"</td>";
                        htmlStr += "</tr>";
                    });
                    $("#groupListTbody").html(htmlStr);

                    $("#bundActivityModal").modal("show");
                }
            });
        }

    </script>

</head>
<body>

<!-- 解除联系人和市场活动关联的模态窗口 -->
<div class="modal fade" id="unbundActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 30%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">解除关联</h4>
            </div>
            <div class="modal-body">
                <p>您确定要解除该关联关系吗？</p>
            </div>

            <!-- 添加一个隐藏input来保存当前记录的id -->
            <input type="hidden" id="unbund-group-id">

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button id="unbundRelationBtn" type="button" class="btn btn-danger" data-dismiss="modal">解除</button>
            </div>
        </div>
    </div>
</div>

<!-- 联系人和市场活动关联的模态窗口 -->
<div class="modal fade" id="bundActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">关联组信息</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入组信息名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox"/></td>
                        <td>组编码</td>
                        <td>组名称</td>
                        <td>备注</td>
                        <td></td>
                    </tr>
                    </thead>
                    <tbody id="groupListTbody">

                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <%--<button type="button" class="btn btn-primary" data-dismiss="modal">关联</button>--%>
                <button id="createUnbundContactsBtn" type="button" class="btn btn-primary">关联</button>
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
                    <!-- 添加一个隐藏input来保存当前记录的id -->
                    <input type="hidden" id="edit-id">

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
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                    <%--<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>--%>


                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveCreateContactsBtn" type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
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
        <h3>${staff.staffName } <small> - asiainfo</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
        <button id="editContactsDetailBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
        <button id="deleteContactsDetailBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <!-- 添加一个隐藏input来保存当前记录的id -->
    <input type="hidden" id="detail-id" value="${staff.staffId }">

    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">邮箱</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${staff.staffEmail }</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${staff.staffPhone }</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${staff.remark }
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>

</div>

<!-- 市场活动 -->
<div>
    <div style="position: relative; top: 60px; left: 40px;">
        <div class="page-header">
            <h4>组信息</h4>
        </div>

        <div id="groupListDiv" style="position: relative;top: 0px;">
            <table id="groupListTable" class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>序号</td>
                    <td>组编码</td>
                    <td>组名称</td>
                    <td>备注</td>
                    <td></td>
                </tr>
                </thead>
                <tbody>

                <c:if test="${not empty groupList }">
                    <c:forEach var="group" items="${groupList }" varStatus="status">
                            <tr id="tr_${group.groupId }">
                                <td><a href="activity/detail.html" style="text-decoration: none;">${status.index + 1}</a></td>
                                <td>${group.groupCode }</td>
                                <td>${group.groupName }</td>
                                <td>${group.remark }</td>
                                <td><a group-id="${group.groupId }" name="edit_A" href="javascript:void(0);" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>
                            </tr>
                    </c:forEach>
                </c:if>

                </tbody>
            </table>
        </div>

        <div>
            <a href="javascript:void(0);" onclick="bund_method()" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联组信息</a>
        </div>
    </div>
</div>


<div style="height: 200px;"></div>
</body>
</html>