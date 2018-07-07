<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>accountdetails</title>
    <script src="../../lib/main.js"></script>
    <link href="../../lib/ligerUI/skins/Aqua/css/ligerui-allnew.css" rel="stylesheet" type="text/css" />
    <link href="../../lib/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
    <script src="../../lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script> 
    <script src="../../lib/json2.js" type="text/javascript"></script>    
    <script src="../../lib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="../../lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
    <script src="formatMoney.js" type="text/javascript"></script>   
       
    <script type="text/javascript">
    
	$("#accountdetailsgrid").remove();
	$("#acountDetailsWindow").remove();
	
    var grid;   
    var dialog = frameElement.dialog;
    var dialogData = dialog.get('data');//获取data参数
    var dialogDatafilter = dialog.get('datafilter');//获取data参数
    var companies = dialogDatafilter.CompanyCode.split(',');  
    var dialogColumns = dialog.get('columnsAccountDetails');//获取data参数
    
    if (isArray(dialogData)) {
    dialogDatafilter.Structure  = []
    for (var i = 0; i < dialogData.length; i++ ){	
    	dialogDatafilter.Structure.push(dialogData[i].Structure)
    } 
    } else {
    	dialogDatafilter.Structure  = []
    	dialogDatafilter.Structure.push(dialogData.Structure)
    }

	//判断是否存在明细的字段，不然会重复
    if ((dialogColumns[0].name == "Account Number") ||(dialogColumns[1].name == "Account Number") ){
    }else {
	    var Account_Number = { display: 'Account', name: 'Account Number', align: "left", width: 160};
	    var GL_Description = { display: 'Description', name: 'GL_Description', align: "left", width: 350};
	    dialogColumns.splice(1,0,Account_Number);
	    dialogColumns.splice(2,0,GL_Description);
    }
	
	//删除重复的checkbox
	for (var i = 0; i <dialogColumns.length;  i++ ){
     if (dialogColumns[i].ischeckbox == true){ 
    	dialogColumns.splice(i, 1);
      }
	}
	
	//排序IRT Structure
	for (var i = 0; i <dialogColumns.length;  i++ ){
     if (dialogColumns[i].display == "Structure"){ 
		var str = dialogColumns.splice(i,1);
		dialogColumns.unshift(str[0]);
		break
     }
	}
		
     $(function ()
            {
    	      window['g'] =  grid =  $("#accountdetailsgrid").ligerGrid({ 
	                  columns: dialogColumns, 
	                  contentType: 'application/json',
	                  url:"../../../ReportAccountDetailServlet",
	                  dataAction: 'local',
	                  dataType: 'local',
	                  parms: dialogDatafilter,		
	                  usePager: false,
	                  width: '100%', 
	                  height: '100%',
	                  checkbox:true,
	                  toolbar: { items: [{ text: 'Search', click: itemclick, icon: 'search2'}]},
	                  onSelectRow: function(rowdata, rowid, rowobj){
	                	  SumofSelectRows(rowdata);
	                  }, 
	                  onUnSelectRow:function(rowdata, rowid, rowobj){
	                	  SumofSelectRows(rowdata);
	                  },
	                  rowAttrRender: function (data) {	
	                  if (data.GL_Description == "" 
	                  ){
	                     return "style = 'background: #FFFFE0; font-weight:bold;'";
	                     }
	                   }
	             }); 
            }); 
  
     function isArray(arg){
    	  return Object.prototype.toString.call(arg) === '[object Array]';
    }
     
     function itemclick(){
             g.options.data =  g.data;
             g.options.dataType = 'local'
	    	 g.showFilter();  
   	       }
     /**
      * 数字格式转换成千分位
      *@param{Object}num
      */
     function format(num) {
     	if (num != null){
     		if (num.toString().indexOf("%")>0){
                 num = num
     		} else {
    			num = $.formatMoney(Math.round(num))
     		}
     	}
     	return num
     }
      
      function SumofSelectRows(rowdata)	
      {
    	 var sumfields =  [];	  
    	 for (var i = 0; i < companies.length; i++){
    		 sumfields.push({display: companies[i], name: companies[i], newline: false, type: "text"}) 
    	 }
        	 sumfields.push({display: "Total", name: "Total", newline: false, type: "text"})
        	 
    	  form = $("#SumofSelectRows").ligerForm({
  	        inputWidth: 90, labelWidth: 50, space: 40,
  	        fields: sumfields,
  	        readonly: true,
    	  });
    	  var sum  = {}
    	  var data = grid.getSelectedRows();
    	  if (data.length == 0) {
    		  $("#SumofSelectRows").empty();
    	  } else {
	    	  for (var j = 0; j < sumfields.length; j++){
       		      var rs = 0.00
	        	  for (var i = 0; i < data.length; i++){
	    		       if (data[i][sumfields[j].name] != null)
	    		    	   rs = rs + parseFloat(data[i][sumfields[j].name])
	    	      }
       		   sum[sumfields[j].name] = format(rs)	    	  
	    	  }    	  
    	  form.setData(sum);
    	  }
      } 
    </script>
    <style type="text/css">
    .l-text-field{text-align:right; color:#F00;font-weight:bold;}
    </style>
    
    
</head>
<body> 
	  <div id="SumofSelectRows"></div> 
      <div id="accountdetailsgrid"></div>
      
</body>
</html>