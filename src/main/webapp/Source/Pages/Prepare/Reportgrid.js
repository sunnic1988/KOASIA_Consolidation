$(document).ready(function(){
   $("body").on("keydown","#filterform", function(e){
		var curKey = e.which;
		if(curKey == 13){

			$("#tab1").remove();
			
			$("#filterform").after("<div id=\"tab1\" style=\"overflow: hidden; border: 1px solid #A3C0E8;\"> " + 
    		"<div id=\"item1\" tabid=\"item1\" title=\"Profit and Loss\">" +
            /* Profit and Loss 报表*/
    		"<div id=\"plgrid\"  style=\"margin-top: 0px\"	></div>"+
    		"</div>"+	
    		"<div id=\"item2\" tabid=\"item2\"  title=\"Operating Costs\">" +
            /* Operating Costs 报表*/
    		"<div id=\"opgrid\" style=\"margin-top: 0px\"></div>"+
    		"</div>"+
    		"<div id=\"item3\" tabid=\"item3\"  title=\"Balance Sheet\">" +
            /* Balance Sheet 报表*/
    		"<div id=\"bsgrid\" style=\"margin-top: 0px\"></div>"+
    		"</div>"+
    		"</div>");
    		
	 //添加完元素之后重新刷新	
      liger.inject.init();	  
      
/*        $("#filterform").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformHide\" onclick = \"filterformHide()\">Close Filterform</button>")*/
         var navtab = null;
         $(function ()
         {
             $("#tab1").ligerTab({     
             contextmenu: false,
             onAfterSelectTabItem: function(targettabid){
             	switch(targettabid)
					{
					case "item1":
					  pl.reRender() 
					  break;
					case "item2":
					  op.reRender() 
					  break;
					case "item3":
					  bs.reRender() 
					  break;
					}         	
             }
             });	
             //navtab = $("#tab1").ligerGetTabManager();
         });	
        createplGrid();
		createopGrid();
        createbsGrid();
		}
	});
});

/*function filterformHide(){
	$("#filterform").hide()
	$("#filterformHide").remove();
	$("#filterform").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformDisplay\" onclick = \"filterformDisplay()\">Open Filterform</button>")
	 navtab = $("#tab1").ligerGetTabManager();
	 var targettabid = navtab.selectedTabId
      	switch(targettabid)
		{
		case "item1":
		         createplGrid();
		  break;
		case "item2":
		         createopGrid();
		  break;
		case "item3":
		         createbsGrid();
		  break;
		}    
}

function filterformDisplay(){
	$("#filterform").show()
	$("#filterformDisplay").remove();
	$("#filterform").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformHide\" onclick = \"filterformHide()\">Close Filterform</button>")
	 navtab = $("#tab1").ligerGetTabManager();
	 var targettabid = navtab.selectedTabId
      	switch(targettabid)
		{
		case "item1":
		         createplGrid();
		  break;
		case "item2":
		         createopGrid();
		  break;
		case "item3":
		         createbsGrid();
		  break;
		}    
}*/
    
function runReports(){
	
	$("#filterform").remove();
	$("#filterformPeriod").remove();
	$("#tab1").remove();

	var groupicon = "../../lib/ligerUI/skins/icons/communication.gif"
	
    //创建筛选内容区域	
    $("#topmenu").after("<div id=\"filterform\" class=\"liger-form\"></div>");  	
    
   	    //创建表单结构 
	    form = $("#filterform").ligerForm({
	        inputWidth:200, labelWidth: 90, space: 40,
	        fields: [
	        { display: "Company", name: "Consolevel", newline: true, type: "text", group: 'Filterform', groupicon:groupicon,
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: KOCHI-Cons</div>'},
	        { display: "Year", name: "FiscalYear", newline: true, type: "text", 
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 2018</div>'}, 
	        { display: "Period", name: "Postingperiod", newline: true, type: "select", dictionary: '1|2|3|4|5|6|7|8|9|10|11|12|13' },  
	        { display: "Currency", name: "Currency", newline: true, type: "select", dictionary: 'CNY|EUR' }
	        ]
	    })
    		
     //添加完元素之后重新刷新	
      liger.inject.init();	
  
      var form = liger.get("filterform");
     
      //维护对应sheet的数据库表名
      form.setData({ 
    	  "Consolevel": 'KOCHI-Cons',
    	  "FiscalYear": '2018',
    	  "Postingperiod": '1',
    	  "Currency": 'CNY'
      });
}         
      
function createopGrid(){
/*     $("#opgrid").remove();
     $("#item2").after("<div id=\"opgrid\" style=\"margin-top: 0px\"></div>")*/

      var form = liger.get("filterform");
      var datafilter =  form.getData();
      
    	   $.ajax({
           type: "GET",
           url: "../../../AjaxCompanyCodeServlet",
           data: {Consolevel: datafilter.Consolevel},
           dataType: "json",
           contentType: "application/json", //必写
           success:function(jsondata){  

        	   var grid;    
               var companies = jsondata.CompanyCode.split(',');  
               var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 200}]
               //动态控制列 公司代码
               for (var i = 0; i < companies.length; i++) {	   
                   var column =  { display: '', name: '', align: "right", width: 90, type:'int',
                   render: function(rowdata, rowindex, value)
                {
                	 if  (datafilter["Postingperiod"] == 13 && rowdata.Structure == "in % of Total Business Volume" ){
                		return ""
                	 } else {
                		return format(value)	
                	 }
                }}
            	   column.display = companies[i];
            	   column.name = companies[i];
            	   columns[i+1] = column;   
               }
               columns.push({ display: 'Total', name: 'Total', align: "right", width: 90, type:'int',
               render: function(rowdata, rowindex, value)
                {
                	 if  (datafilter["Postingperiod"] == 13 && rowdata.Structure == "in % of Total Business Volume" ){
                		return ""
                	 } else {
                		return format(value)	
                	 }
                }
                });         
               datafilter["CompanyCode"] = jsondata.CompanyCode;
                $(function ()
                {
                    window['op'] = grid = managerop = $("#opgrid").ligerGrid({ 
                        columns: columns, 
                        contentType: 'application/json',
                        url:"../../../ReportOpServlet",
                        dataAction: 'local',
                        parms: datafilter,
                        usePager: false,
                        width: '100%', 
                        height: '100%', 
                        enabledSort: false,
/*	                    toolbar: {items: [
	                	{ text: 'AccountDetails', click: AccountDetails, icon:'view' },
	                    { line:true }
	                    ]},*/
                        checkbox:true,
                        onAfterShowData:function (data){  	
                        var rowsData = data.Rows;//获取data参数 	    
                        for (var i = 0; i < rowsData.length; i++){           	
                        if (rowsData[i].Structure == "Total Wages" 
                        || rowsData[i].Structure == "in % of Total Business Volume" 
                        || rowsData[i].Structure == "Total Other Costs" 
                        || rowsData[i].Structure == "Total Operating Costs" 
                        || rowsData[i].Structure == "Total Salaries" 
                        || rowsData[i].Structure == "Total Personnel Costs"
                        || rowsData[i].Structure == "Check PL_Structure"
	                    ){
	                        var srcString = escapeJquery("#opgrid|1|"+rowsData[i].__id)
	                        $(srcString).find("span").remove();
                           }
                          }
                        },                        
                        rowAttrRender: function (data) {	
                        if (data.Structure == "Total Wages" 
                        || data.Structure == "Total Other Costs" 
                        || data.Structure == "Total Operating Costs" 
                        || data.Structure == "Total Salaries" 
                        || data.Structure == "Total Personnel Costs"
                        ){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                           }
                        if (data.Structure == "in % of Total Business Volume"){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                           }
                        if (data.Structure == "Check PL_Structure"){
                           return "style = 'font-weight:bold; color: #F00'";
                        	 }
                         },
                        onDblClickRow : function (data, rowindex, rowobj)
                        {
                        var selectData = managerop.getSelectedRows();	
                        if (  data.Structure != "Total Wages" 
                        && data.Structure != "in % of Total Business Volume" 	
                        && data.Structure != "Total Other Costs" 
                        && data.Structure != "Total Operating Costs" 
                        && data.Structure != "Total Salaries" 
                        && data.Structure != "Total Personnel Costs" 
                        ){
                         if (!selectData.length) { 	
					        alert('Please choose the rows!')	
					     } else {	
			               $.ligerDialog.open({
			               	id: "acountDetailsWindow",
			                url: '../public/accountdetails.jsp', 
						    height: managerop.windowHeight, 
						    width: getWidth(), 
			             	isResize: true,
			                data: selectData,
			             	datafilter: datafilter,     	
			             	columnsAccountDetails: columns,
			             	title:"Accounts Details"
			             });
                        } 
                        }
                        }
                    });
                });
        	   }
           });
       }   

function createplGrid(){
/*	 $("#plgrid").remove();
     $("#item1").after("<div id=\"plgrid\" style=\"margin-top: 0px\"></div>")*/
						     
      var form = liger.get("filterform");
      var datafilter =  form.getData();
      
    	   $.ajax({
           type: "GET",
           url: "../../../AjaxCompanyCodeServlet",
           data: {Consolevel: datafilter.Consolevel},
           dataType: "json",
           contentType: "application/json", //必写
           success:function(jsondata){  
        	   var grid;    
               var companies = jsondata.CompanyCode.split(',');  
               var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 200}]
               //动态控制列 公司代码
               for (var i = 0; i < companies.length; i++) {	   
                   var column =  { display: '', name: '', align: "right", width: 90, type:'int',
                   render: function(rowdata, rowindex, value)
                {
                	 if  (datafilter["Postingperiod"] == 13 && rowdata.Structure == "in % of Total Business Volume" ){
                		return ""
                	 } else {
                		return format(value)	
                	 }
                }}
            	   column.display = companies[i];
            	   column.name = companies[i];
            	   columns[i+1] = column;   
               }
               columns.push({ display: 'Total', name: 'Total', align: "right", width: 90, type:'int',
               render: function(rowdata, rowindex, value)
                {
                	 if  (datafilter["Postingperiod"] == 13 && rowdata.Structure == "in % of Total Business Volume" ){
                		return ""
                	 } else {
                		return format(value)	
                	 }
                }
                });                           
               datafilter["CompanyCode"] = jsondata.CompanyCode;  
                $(function ()
                {
                    window['pl'] = grid = managerpl = $("#plgrid").ligerGrid({ 
                        columns: columns, 
                        contentType: 'application/json',
                        url:"../../../ReportPlServlet",
                        dataAction: 'local',
                        parms: datafilter,
                        usePager: false,
                        width: '100%', 
                        height: '100%', 
                        enabledSort: false,
/*	                    toolbar: {items: [
	                	{ text: 'AccountDetails', click: AccountDetails, icon:'view' },
	                    { line:true }
	                    ]},*/
                        checkbox:true,
                        onAfterShowData:function (data){  	
                        var rowsData = data.Rows;//获取data参数 	          
                        for (var i = 0; i < rowsData.length; i++){           	
                        if (rowsData[i].Structure == "Total Business Volume" 
                        || rowsData[i].Structure == "in % of Total Business Volume" 
                        || rowsData[i].Structure == "Manufacturing Costs" 
                        || rowsData[i].Structure == "Gross Margin"
                        || rowsData[i].Structure == "Overhead Costs"
                        || rowsData[i].Structure == "Operating Income" 
                        || rowsData[i].Structure == "Non Operating Income" 
                        || rowsData[i].Structure == "Income before Taxes" 
	                    ){
	                        var srcString = escapeJquery("#plgrid|1|"+rowsData[i].__id)
	                        $(srcString).find("span").remove();
                           }
                          }
                        },
                        rowAttrRender: function (data) {	
                        if (data.Structure == "Total Business Volume" 
                        || data.Structure == "Manufacturing Costs" 
                        || data.Structure == "Gross Margin"
                        || data.Structure == "Overhead Costs"
                        || data.Structure == "Operating Income" 
                        || data.Structure == "Non Operating Income" 
                        || data.Structure == "Income before Taxes" 
                        ){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                           }
                        if (data.Structure == "in % of Total Business Volume"){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                           }
                         },
                        onDblClickRow : function (data, rowindex, rowobj)
                        {
                        var selectData = managerpl.getSelectedRows();	
                        if(  data.Structure != "Total Business Volume" 
                        && data.Structure != "in % of Total Business Volume" 
                        && data.Structure != "Manufacturing Costs" 
                        && data.Structure != "Gross Margin"
                        && data.Structure != "Overhead Costs"
                        && data.Structure != "Operating Income" 
                        && data.Structure != "Non Operating Income" 
                        && data.Structure != "Income before Taxes" 
                        ){
                         if (!selectData.length) { 	
					        alert('Please choose the rows!')	
					     } else {	
			               $.ligerDialog.open({
			               	id: "acountDetailsWindow",
			                url: '../public/accountdetails.jsp', 
						    height: managerpl.windowHeight, 
						    width: getWidth(), 
			             	isResize: true,
			                data: selectData,
			             	datafilter: datafilter,     	
			             	columnsAccountDetails: columns,
			             	title:"Accounts Details"
			             });
                        }
                        }
                        }
                    });
                });
        	   }
           });
       }          
  
  function createbsGrid(){
/*     $("#bsgrid").remove();
     $("#item3").after("<div id=\"bsgrid\" style=\"margin-top: 0px\"></div>")*/

      var form = liger.get("filterform");
      var datafilter =  form.getData();
      
    	   $.ajax({
           type: "GET",
           url: "../../../AjaxCompanyCodeServlet",
           data: {Consolevel: datafilter.Consolevel},
           dataType: "json",
           contentType: "application/json", //必写
           success:function(jsondata){  

        	   var grid;    
               var companies = jsondata.CompanyCode.split(',');  
               var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 250}]
               //动态控制列 公司代码
               for (var i = 0; i < companies.length; i++) {	   
                   var column =  { display: '', name: '', align: "right", width: 90, type:'int',
                   render: function(rowdata, rowindex, value)
                {
                		return format(value)	
                }}
            	   column.display = companies[i];
            	   column.name = companies[i];
            	   columns[i+1] = column;   
               }
               columns.push({ display: 'Total', name: 'Total', align: "right", width: 90, type:'int',
               render: function(rowdata, rowindex, value)
                {
                		return format(value)	
                }
                });         
               datafilter["CompanyCode"] = jsondata.CompanyCode;  
                $(function ()
                {
                    window['bs'] = grid = managerbs = $("#bsgrid").ligerGrid({ 
                        columns: columns, 
                        contentType: 'application/json',
                        url:"../../../ReportBsServlet",
                        dataAction: 'local',
                        parms: datafilter,
                        usePager: false,
                        width: '100%', 
                        height: '100%',
                        enabledSort: false, 
/*	                    toolbar: {items: [
	                	{ text: 'AccountDetails', click: AccountDetails, icon:'view' },
	                    { line:true }
	                    ]},*/
                        checkbox:true,
                        onAfterShowData:function (data){  	
                        var rowsData = data.Rows;//获取data参数 	          
                        for (var i = 0; i < rowsData.length; i++){           	
                        if (rowsData[i].Structure == "Fixed Assets" 
                        || rowsData[i].Structure == "Inventory" 
                        || rowsData[i].Structure == "Current Assets" 
                        || rowsData[i].Structure == "Total Assets" 
                        || rowsData[i].Structure == "Equity Capital"
                        || rowsData[i].Structure == "Longterm Liabilities" 
                        || rowsData[i].Structure == "Shortterm Liabilities" 
                        || rowsData[i].Structure == "Total Equity and Liabilities"
                        || rowsData[i].Structure == "Check Balance"
                        || rowsData[i].Structure == "CAPITAL EXPENDITURES"
                        || rowsData[i].Structure == "Check Investment"
                        || rowsData[i].Structure == "TOTAL DEPRECIATION"
                        || rowsData[i].Structure == "Invest Land/Building 3rd"
                        || rowsData[i].Structure == "Invest Machinery/Equipment 3rd"
                        || rowsData[i].Structure == "Invest Tools/Devices 3rd"
                        || rowsData[i].Structure == "Invest Engineering Services 3rd"
                        || rowsData[i].Structure == "Invest Other Fixed Assets 3rd"
                        || rowsData[i].Structure == "Retirement Land/Building 3rd"
                        || rowsData[i].Structure == "Retirement Machinery/Equipment 3rd"
                        || rowsData[i].Structure == "Retirement Tools/Devices 3rd"
                        || rowsData[i].Structure == "Retirement Engineering Services 3rd"
                        || rowsData[i].Structure == "Retirement Other Fixed Assets 3rd"
	                    ){
	                        var srcString = escapeJquery("#bsgrid|1|"+rowsData[i].__id)
	                        $(srcString).find("span").remove();
                           }
                          }
                        },
                        rowAttrRender: function (data) {	
                        if (data.Structure == "Fixed Assets" 
                        || data.Structure == "Inventory" 
                        || data.Structure == "Current Assets" 
                        || data.Structure == "Total Assets" 
                        || data.Structure == "Equity Capital" 
                        || data.Structure == "Longterm Liabilities" 
                        || data.Structure == "Shortterm Liabilities" 
                        || data.Structure == "Total Equity and Liabilities"
                        || data.Structure == "CAPITAL EXPENDITURES"
                        ){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                           }
                         if (data.Structure == "Check Balance" || data.Structure == "Check Investment"){
                           return "style = 'font-weight:bold; color: #F00'";
                        	 } 
                         },
                        onDblClickRow : function (data, rowindex, rowobj)
                        {
                        var selectData = managerbs.getSelectedRows();
                        if(   data.Structure != "Fixed Assets" 
                        && data.Structure != "Inventory" 
                        && data.Structure != "Current Assets" 
                        && data.Structure != "Total Assets" 
                        && data.Structure != "Equity Capital" 
                        && data.Structure != "Longterm Liabilities" 
                        && data.Structure != "Shortterm Liabilities" 
                        && data.Structure != "Total Equity and Liabilities"  
                        && data.Structure != "CAPITAL EXPENDITURES"  
                        && data.Structure != "Check Investment"  
                   		&& data.Structure != "Invest Land/Building 3rd"
                        && data.Structure != "Invest Machinery/Equipment 3rd"
                        && data.Structure != "Invest Tools/Devices 3rd"
                        && data.Structure != "Invest Engineering Services 3rd"
                        && data.Structure != "Invest Other Fixed Assets 3rd"
                        && data.Structure != "Retirement Land/Building 3rd"
                        && data.Structure != "Retirement Machinery/Equipment 3rd"
                        && data.Structure != "Retirement Tools/Devices 3rd"
                        && data.Structure != "Retirement Engineering Services 3rd"
                        && data.Structure != "Retirement Other Fixed Assets 3rd"
                        && data.Structure != "Check Balance"
                        ){
                         if (!selectData.length) { 	
					        alert('Please choose the rows!')	
					     } else {	
			               $.ligerDialog.open({
			               	id: "acountDetailsWindow",
			                url: '../public/accountdetails.jsp', 
						    height: managerbs.windowHeight, 
						    width: getWidth(), 
			             	isResize: true,
			                data: selectData,
			             	datafilter: datafilter,     	
			             	columnsAccountDetails: columns,
			             	title:"Accounts Details"
			             });
                        }
                        }
                        }
                    });
                });
        	   }
           });
       } 
       
       
/*function AccountDetails(){
	var data = manager.getSelectedRows();
	var columns =  manager.columns
    var form = liger.get("filterform");
    var datafilter =  form.getData();
        $.ajax({
           type: "GET",
           url: "../../../AjaxCompanyCodeServlet",
           data: {Consolevel: datafilter.Consolevel},
           dataType: "json",
           contentType: "application/json", //必写
           success:function(jsondata){  
           datafilter["CompanyCode"] = jsondata.CompanyCode;  
           }
        });
    if (!data.length) { 	
        alert('Please choose the rows!')	
     } else {	
     	$.ligerDialog.open({
				id: "acountDetailsWindow",
			    url: '../public/accountdetails.jsp', 
			    height: manager.windowHeight, 
			    width: getWidth(), 
			    isResize: true,
			    data: data,
			    datafilter: datafilter,     	
			    columnsAccountDetails: columns,
			    title:"Accounts Details"
	    });
     }
}*/

function getWidth(){
	var width = 0
	if (document.getElementById("topmenu") != null){
		width = document.getElementById("topmenu").offsetWidth * 0.95
	} else {
	    width = document.getElementById("topmenu").offsetWidth * 0.95
	}
	return width
}

       
 function escapeJquery(srcString) {
 // 转义之后的结果
 var escapseResult = srcString;
 // javascript正则表达式中的特殊字符
 var jsSpecialChars = ["\\", "^", "$", "*", "?", ".", "+", "(", ")", "[",
   "]", "|", "{", "}"];
 // jquery中的特殊字符,不是正则表达式中的特殊字符
 var jquerySpecialChars = ["~", "`", "@", "%", "&", "=", "'", "\"", ":", ";", "<", ">", ",", "/"];
 for (var i = 0; i < jsSpecialChars.length; i++) {
  escapseResult = escapseResult.replace(new RegExp("\\"
        + jsSpecialChars[i], "g"), "\\"
      + jsSpecialChars[i]);
 }
 for (var i = 0; i < jquerySpecialChars.length; i++) {
  escapseResult = escapseResult.replace(new RegExp(jquerySpecialChars[i],
      "g"), "\\" + jquerySpecialChars[i]);
 }
 return escapseResult;
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
