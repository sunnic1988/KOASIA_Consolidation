<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Configuration</title>
    <script src="../../lib/main.js"></script>
    <link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script> 
    <script src="../../lib/json2.js" type="text/javascript"></script>    
    <script src="../../lib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
    <script src="grid.js" type="text/javascript"></script>      
    <script type="text/javascript">

       //生成top部分两组按钮
        $(function ()
        {
            $("#topmenu").ligerMenuBar({ items: [
            	 { text: 'Configuration Sheets', menu: menu1 }
            ]
            });

            $("#toptoolbar").ligerToolBar({ items: [
                { text: 'Upload', click: uploadForm1, icon:'up'},
                { line:true },
                { text: 'Export', click: exportForm1, icon:'down' },
                { line:true },
                { text: 'Delete', click: deleteForm1, icon:'delete' },
                { line:true },
                
            ],
            });
        });

        //下拉菜单，主要用于切换内容
          var menu1 = { width: 240, items:
              [

              { text: 'Maintain GL MasterData', click: masterdataGrid },
              { text: 'Maintain Exchage Rate', click: exchagerateGrid },
              { text: 'Maintain Company MasterData', click: companyGrid },
              { text: 'Maintain Conditon for Python', click: pythonGrid },
              { text: 'Open and Close Period', click: periodGrid },
              { line: true },
              
              ]
          };        
                 
        //测试事件
         function itemclick(item)
         {	
             alert(item.text);
         }
         
         //上传按钮
         function uploadForm1()
         {
             $.ligerDialog.open({ 
             	url: '../public/uploadfile.jsp', 
             	height: 400, 
             	width: 600, 
             	isResize: true,
             	title:"Info"
             });
         }
         
       //导出按钮
         function exportForm1()
         {
             $.ligerDialog.open({
                url: '../public/exportfile.jsp', 
             	height: 400, 
             	width: 600, 
             	isResize: true,
             	title:"Info"
             });
         }
       
         //删除按钮 
         function deleteForm1()
         {
             $.ligerDialog.open({
                url: '../public/deletefile.jsp', 
             	height: 400, 
             	width: 600, 
             	isResize: true,
             	title:"Info"
             });
         }

    </script>

    
    <style type="text/css">
    #menu1,.l-menu-shadow{top:30px; left:50px;}
    #menu1{width:200px;}
    #maingrid{margin-left: 8px}
    </style>
    
</head>
<body> 

       <!-- 菜单栏按钮 -->
      <div id="toptoolbar"></div>
      
      <!-- 下拉菜单 -->
      <div id="topmenu"></div> 
      
      <!-- 检查是否被选中 -->
      <div><input type="checkbox" id="chkbefore" style = "display: none"/></div>
      
      <!-- 筛选条件内容,改成动态添加 -->

       <!-- 表格内容-->
	  <div class="l-clear"></div>
	  <div id="maingrid" style="margin-top: 5px"></div>

</body>
</html>