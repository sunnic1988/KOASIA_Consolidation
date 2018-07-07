$(document).ready(function(){
   $("body").on("keydown","#filterformPeriod", function(e){
		var curKey = e.which;
		if(curKey == 13){

			$("#tab1").remove();
			
			$("#filterformPeriod").after("<div id=\"tab1\" style=\"overflow: hidden; border: 1px solid #A3C0E8;\"> " + 
    		"<div id=\"item1\" tabid=\"item1\" title=\"Profit and Loss\">" +
            /* Profit and Loss 报表*/
    		"<div id=\"plperiodgrid\"  style=\"margin-top: 0px\"></div>"+
    		"</div>"+	
    		"<div id=\"item2\" tabid=\"item2\"  title=\"Operating Costs\">" +
            /* Operating Costs 报表*/
    		"<div id=\"opperiodgrid\" style=\"margin-top: 0px\"></div>"+
    		"</div>"+
    		"<div id=\"item3\" tabid=\"item3\"  title=\"Balance Sheet\">" +
            /* Balance Sheet 报表*/
    		"<div id=\"bsperiodgrid\" style=\"margin-top: 0px\"></div>"+
    		"</div>"+
    		"</div>");
    		
	 //添加完元素之后重新刷新	
      liger.inject.init();	  
/*        $("#filterformPeriod").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformPeriodHide\" onclick = \"filterformPeriodHide()\">Close Filterform</button>")*/
         var navtab = null;
         $(function ()
         {
             $("#tab1").ligerTab({     
             contextmenu: false,
             onAfterSelectTabItem: function(targettabid){
             	switch(targettabid)
					{
					case "item1":
					  plperiod.reRender(e) 
					  break;
					case "item2":
					  opperiod.reRender(e) 
					  break;
					case "item3":
					  bsperiod.reRender(e) 
					  break;
					}         	
             }
             });	
             //navtab = $("#tab1").ligerGetTabManager();
         });	
        createplGridbyPeriod();
        createopGridbyPeriod();
        createbsGridbyPeriod();
		}
	});
});

/*function filterformPeriodHide(){
	$("#filterformPeriod").hide()
	$("#filterformPeriodHide").remove();
	$("#filterformPeriod").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformPeriodDisplay\" onclick = \"filterformPeriodDisplay()\">Open Filterform</button>")
	 navtab = $("#tab1").ligerGetTabManager();
	 var targettabid = navtab.selectedTabId
      	switch(targettabid)
		{
		case "item1":
		         createplGridbyPeriod();
		  break;
		case "item2":
		         createopGridbyPeriod();
		  break;
		case "item3":
		         createbsGridbyPeriod();
		  break;
		}    
}

function filterformPeriodDisplay(){
	$("#filterformPeriod").show()
	$("#filterformPeriodDisplay").remove();
	$("#filterformPeriod").before("<button type=\"button\" class=\"l-menubar\" id = \"filterformPeriodHide\" onclick = \"filterformPeriodHide()\">Close Filterform</button>")
	 navtab = $("#tab1").ligerGetTabManager();
	 var targettabid = navtab.selectedTabId
      	switch(targettabid)
		{
		case "item1":
		         createplGridbyPeriod();
		  break;
		case "item2":
		         createopGridbyPeriod();
		  break;
		case "item3":
		         createbsGridbyPeriod();
		  break;
		}    
}*/
    
function runReportsbyPeriod(){
	
	$("#filterform").remove();
	$("#filterformPeriod").remove();
	$("#tab1").remove();

    //创建筛选内容区域	
    $("#topmenu").after("<div id=\"filterformPeriod\" class=\"liger-form\"></div>");  
	
    	var groupicon = "../../lib/ligerUI/skins/icons/communication.gif"
    	
   	    //创建表单结构 
	    form = $("#filterformPeriod").ligerForm({
	        inputWidth:200, labelWidth: 90, space: 40,
	        fields: [
	        { display: "Company", name: "Company", newline: true, type: "text", group: 'Filterform', groupicon:groupicon,
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 1050</div>'},
	        { display: "Year", name: "FiscalYear", newline: true, type: "text", 
	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 2018</div>'	},
//	        { display: "Period", name: "Period", newline: true, type: "text", 
//	          afterContent: '<div style="margin-right:10px;margin-left:6px;color:red;float:left;">Example: 1,2...</div>'	},
	        { display: "Currency", name: "Currency", newline: true, type: "select", dictionary: 'CNY|EUR' }
	        ]
	    })
    		
    	
     //添加完元素之后重新刷新	
      liger.inject.init();	
  
      var form = liger.get("filterformPeriod");
     
      //维护对应sheet的数据库表名
      form.setData({ 
    	  "Company": '1050',
    	  "FiscalYear": '2018',
//    	  "Period": '1',
  	  	  "Currency": 'CNY'
      });
}         

         
function createopGridbyPeriod(){
/*     $("#opperiodgrid").remove();
     $("#item2").after("<div id=\"opperiodgrid\" style=\"margin-top: 0px\"></div>")*/

      var form = liger.get("filterformPeriod");
	  var datafilter =  form.getData();
  
       var gridPeriod;    
       var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 200, frozen: true},
                                 { display: 'Jan', name: '1', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},
                                 { display: 'Feb', name: '2', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Mar', name: '3', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}, 
                                 { display: 'Apr', name: '4', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},     
                                 { display: 'May', name: '5', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jun', name: '6', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jul', name: '7', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Aug', name: '8', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Sep', name: '9', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},             
                                 { display: 'Oct', name: '10', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},            
                                 { display: 'Nov', name: '11', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Dec', name: '12', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},              
                                 { display: 'Adj', name: '13', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){
                                 	if (rowdata.Structure == "in % of Total Business Volume"){
                                 	return ""	
                                 	} else {
                                 	return format(value)}
                                 	}
                                 },          
                                 { display: 'Total', name: 'Total', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}        
                                ]
                $(function ()
                {
                  window['opperiod'] = gridPeriod = managerPeriodop = $("#opperiodgrid").ligerGrid({ 
                        columns: columns, 
                        contentType: 'application/json',
                        url:"../../../ReportOpServletPeriod",
                        dataAction: 'local',
                        parms: datafilter,
                        usePager: false,
                        width: '100%', 
                        height: '100%', 
                        enabledSort: false,
/*	                    toolbar: {items: [
	                	{ text: 'AccountDetails', click: AccountDetailsbyPeriod, icon:'view' },
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
	                        var srcString = escapeJquery("#opperiodgrid|1|"+rowsData[i].__id)
	                        $(srcString).find("span").remove();
                           }
                          }
                        },                        
                        rowAttrRender: function (data) {	
                        if (data.Structure == "Total Wages" 
                        || data.Structure == "in % of Total Business Volume" 
                        || data.Structure == "Total Other Costs" 
                        || data.Structure == "Total Operating Costs" 
                        || data.Structure == "Total Salaries" 
                        || data.Structure == "Total Personnel Costs"
                        ){
                           return "style = 'background: #FFFFE0; font-weight:bold;'";
                          	 }
                         if (data.Structure == "Check PL_Structure"){
                           return "style = 'font-weight:bold; color: #F00'";
                        	 }
                         },
                        onDblClickRow : function (data, rowindex, rowobj)
                        {
                        var selectData = managerPeriodop.getSelectedRows();	 		
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
			                url: '../public/accountdetailsPeriod.jsp', 
						    height: managerPeriodop.windowHeight, 
						    width: getWidthforPeriod(), 
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

function createplGridbyPeriod(){
/*	 $("#plperiodgrid").remove();
     $("#item1").after("<div id=\"plperiodgrid\" style=\"margin-top: 0px\"></div>")*/
						     
      var form = liger.get("filterformPeriod");
      var datafilter =  form.getData();
      
       var gridPeriod;    
       var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 200, frozen: true},
                                 { display: 'Jan', name: '1', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},
                                 { display: 'Feb', name: '2', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Mar', name: '3', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}, 
                                 { display: 'Apr', name: '4', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},     
                                 { display: 'May', name: '5', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jun', name: '6', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jul', name: '7', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Aug', name: '8', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Sep', name: '9', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},             
                                 { display: 'Oct', name: '10', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},            
                                 { display: 'Nov', name: '11', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Dec', name: '12', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},              
                                 { display: 'Adj', name: '13', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){
                                 	if (rowdata.Structure == "in % of Total Business Volume"){
                                 	return ""	
                                 	} else {
                                 	return format(value)}}
                                 },          
                                 { display: 'Total', name: 'Total', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}
                                ]
        $(function ()
        {
           window['plperiod'] = gridPeriod = managerPeriodpl = $("#plperiodgrid").ligerGrid({ 
                columns: columns, 
                contentType: 'application/json',
                url:"../../../ReportPlServletPeriod",
                dataAction: 'local',
                parms: datafilter,
                usePager: false,
                width: '100%', 
                height: '100%', 
                enabledSort: false,
/*                toolbar: {items: [
            	{ text: 'AccountDetails', click: AccountDetailsbyPeriod, icon:'view' },
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
                    var srcString = escapeJquery("#plperiodgrid|1|"+rowsData[i].__id)
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
                var selectData = managerPeriodpl.getSelectedRows();	 	
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
	                url: '../public/accountdetailsPeriod.jsp', 
				    height: managerPeriodpl.windowHeight, 
				    width: getWidthforPeriod(), 
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
  
  function createbsGridbyPeriod(){
/*     $("#bsperiodgrid").remove();
     $("#item3").after("<div id=\"bsperiodgrid\" style=\"margin-top: 0px\"></div>")*/

      var form = liger.get("filterformPeriod");
      var datafilter =  form.getData();

       var gridPeriod;    
       var columns =  [{ display: 'Structure', name: 'Structure', align: "left", width: 250, frozen: true},
                                 { display: 'Jan', name: '1', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},
                                 { display: 'Feb', name: '2', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Mar', name: '3', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}, 
                                 { display: 'Apr', name: '4', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},     
                                 { display: 'May', name: '5', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jun', name: '6', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Jul', name: '7', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Aug', name: '8', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Sep', name: '9', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},             
                                 { display: 'Oct', name: '10', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},            
                                 { display: 'Nov', name: '11', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},          
                                 { display: 'Dec', name: '12', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}},              
                                 { display: 'Adj', name: '13', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}          
/*                                 { display: 'Total', name: 'Total', align: "right", width: 90, type:'int', render: function(rowdata, rowindex, value){return format(value)}}         */    
                                ]
                $(function ()
                {
                   window['bsperiod'] = gridPeriod = managerPeriodbs = $("#bsperiodgrid").ligerGrid({ 
                        columns: columns, 
                        contentType: 'application/json',
                        url:"../../../ReportBsServletPeriod",
                        dataAction: 'local',
                        parms: datafilter,
                        usePager: false,
                        width: '100%', 
                        height: '100%',
                        enabledSort: false, 
/*	                    toolbar: {items: [
	                	{ text: 'AccountDetails', click: AccountDetailsbyPeriod, icon:'view' },
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
	                        var srcString = escapeJquery("#bsperiodgrid|1|"+rowsData[i].__id)
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
                         if (data.Structure == "Check Balance" || data.Structure == "Check Investment" ){
                           return "style = 'font-weight:bold; color: #F00'";
                        	 } 
                         },
                        onDblClickRow : function (data, rowindex, rowobj)
                        {
                        var selectData = managerPeriodbs.getSelectedRows();	
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
			                url: '../public/accountdetailsPeriod.jsp', 
						    height: managerPeriodbs.windowHeight, 
						    width: getWidthforPeriod(), 
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
       
       
/*function AccountDetailsbyPeriod(){
	var data = managerPeriod.getSelectedRows();
	var columns =  managerPeriod.columns
    var form = liger.get("filterformPeriod");
    var datafilter =  form.getData();
    if (!data.length) { 	
        alert('Please choose the rows!')	
     } else {	
     	$.ligerDialog.open({
				id: "acountDetailsWindow",
			    url: '../public/accountdetailsPeriod.jsp', 
			    height: managerPeriod.windowHeight, 
			    width: getWidthforPeriod(), 
			    isResize: true,
			    data: data,
			    datafilter: datafilter,     	
			    columnsAccountDetails: columns,
			    title:"Accounts Details"
	    });
     }
}
*/

function getWidthforPeriod(){
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
