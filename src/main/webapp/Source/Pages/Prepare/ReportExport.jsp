<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <link href="../../lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
    <link href="../../lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" /> 
    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>
    <script src="../../lib/ligerUI/js/ligerui.all.js"></script>
    
    <style type="text/css">
    .l-button{margin-left: 35px}
    .l-grid-loading { display: none; }
    </style>
    
	<script type="text/javascript">
	var form = null;
	$(function ()
	{ 
	    //创建表单结构 
	    form = $("#Chooseform").ligerForm({
	        inputWidth:200, labelWidth: 90, space: 40,
	        fields: [
	        { display: "Company", name: "Company", newline: true, type: "text", 
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 1050,1051...</div>'},
	        { display: "Year", name: "Year", newline: true, type: "text", 
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 2018</div>'	},
	        { display: "Period", name: "Period", newline: true, type: "select", dictionary: '1|2|3|4|5|6|7|8|9|10|11|12|13', },
	        { display: "Currency", name: "Currency", newline: true, type: "select", dictionary: 'CNY|EUR', }
	        ],
 	        buttons: [
                { text: 'Export', click: exportdata},
                { text: 'Cancel', click: cancel},
             ],
	    });
	});
   
     function exportdata(){
    	 
    	 var myform = document.getElementById("Chooseform");  
         myform.action ="../../../AjaxExportIRTServelt";  
         myform.method ="post";  
         myform.submit()
    	
    } 
	
    function cancel()
    {	
    	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
    	dialog.close();//关闭dialog 
    } 
    
	</script>
	
</head>
<body style="padding:10px"> 
      <form id="Chooseform"></form> 
      <div id="loading" class='l-grid-loading'>Loading...</div>
      <div id="progress"></div>
 </body>
</html>