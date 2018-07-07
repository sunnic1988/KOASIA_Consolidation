$(document).ready(function(){
   $("body").on("keydown","#filterform", function(e){
		var curKey = e.which;
		if(curKey == 13){
        filterData()
		}
   });
});
			
    //Gird 表格，生成数据表格 
    var manager, g;
     
    //主数据的表格
    function masterdataGrid(){
    
    $("#filterform").remove();
    //创建筛选内容区域	
    $("#chkbefore").after("<div id=\"filterform\" class=\"liger-form\">" +
    		"<div class=\"fields\">" +
    		"<input data-type = \"text\" data-label = \"dbName\" data-name = \"dbName\" style = \"display: none\"/>" +
    		"<input data-type = \"text\" data-label = \"G/L Account\" data-name = \"G/L Account\" />" +
    		"<input data-type = \"text\" data-label = \"FS Item\" data-name = \"FS Item\" />" +
    		"<input data-type = \"text\" data-label = \"GL Desp.\" data-name = \"GL Description\" />" +
    		"</div>" +
    		"</div>");  
    
     //添加完元素之后重新刷新
      liger.inject.init();
    	   
      var form = liger.get("filterform");
      
      //维护对应sheet的数据库表名
      form.setData({ 
    	  dbName: 'sys_gl_masterdata',
    	  "G/L Account": '%',
    	  "FS Item": '%',
    	  "GL Description": '%'
      });
      
      var data = form.getData();	
      
       $(f_initGrid);
        function f_initGrid()
        { 
            g = manager = $("#maingrid").ligerGrid({
                toolbar: {items: [
                	{ text: 'Add Line', click: addNewRow, icon:'add' },
                    { line:true },
                    { text: 'Del Line', click: deleteRow, icon:'delete' },
                    { line:true },
                    { text: 'Save Line', click: saveRow, icon:'save' },
                    { line:true },
                    { text: 'Search', click: filterData, icon:'search2' },
                    { line:true },
                    { text: 'Refresh', click: filterData, icon:'refresh' }
                	]},
                title : 'Maintain',
                pageSize:20,
                contentType: 'application/json',
                parms: data,
              //字段配置
                columns: [
                { display: '*G/L Account', name: 'G/L Account', width: 150, type: 'text', editor: { type: 'text'} },
                { display: '*FS Item', name: 'FS Item', type: 'text', width: 150, editor: { type: 'text'} },
                { display: '*IRT Structure', name: 'IRT Structure', width: 300,type: 'text', editor: { type: 'text'} },
                { display: 'BS Indicate', name: 'BS Indicate',width: 100,  type: 'text', editor: { type: 'text'} },
                { display: 'Asset Indicate', name: 'Asset Indicate', width: 100, type: 'text', editor: { type: 'text'} },
                { display: 'GL Description', name: 'GL Description',width: 400, type: 'text', editor: { type: 'text'} },
                { display: 'Method', isSort: false, width: 100, render: function (rowdata, rowindex, value)
                    {
                        var h = "";
                        if (!rowdata._editing)	
                        {
                            h += "<a href='javascript:beginEdit(" + rowindex + ")'>Change</a> ";
                        }
                        else
                        {
                            h += "<a href='javascript:endEdit(" + rowindex + ")'>Done</a> ";
                            h += "<a href='javascript:cancelEdit(" + rowindex + ")'>Back</a> "; 
                        }
                        return h;
                    }
                    }
                ],
              //值为local，数据在客户端进行分页
                dataAction:"local",
              //数据请求地址
                url:"../../../SelectConfigurationServlet",
                onSelectRow: function (rowdata, rowindex)
                {
                    $("#txtrowindex").val(rowindex);
                },
                enabledEdit: true, 
                isScroll: true, 
                checkbox:true,
                rownumbers:false,
                clickToEdit: false,
                hideLoadButton : true,
                width: '99%'
            });   
        }
       }        
      
    //汇率的表格
    function exchagerateGrid(){
    	
        $("#filterform").remove();
    	//创建筛选内容区域	
        $("#chkbefore").after("<div id=\"filterform\" class=\"liger-form\">" +
        		"<div class=\"fields\">" +
        		"<input data-type = \"text\" data-label = \"dbName\" data-name = \"dbName\" style = \"display: none\"/>" +
        		"<input data-type = \"text\" data-label = \"ExRType\" data-name = \"ExRType\" />" +
        		"<input data-type = \"text\" data-label = \"Year\" data-name = \"Year\" />" +
        		"<input data-type = \"text\" data-label = \"Period\" data-name = \"Period\" />" +
        		"<input data-type = \"text\" data-label = \"FromCurrency\" data-name = \"FromCurrency\" />" +
        		"<input data-type = \"text\" data-label = \"ToCurrency\" data-name = \"ToCurrency\" />" +
        		"</div>" +
        		"</div>");  
        
         //添加完元素之后重新刷新
          liger.inject.init();
        	   
          var form = liger.get("filterform");

		// 维护对应sheet的数据库表名
		form.setData({
					dbName : 'sys_exchage_rate',
					"ExRType" : '%',
					"Year" : '%',
					"Period" : '%',
					"FromCurrency" : '%',
					"ToCurrency" : '%'
				});
          
          var data = form.getData();	
          
           $(f_initGrid);
            function f_initGrid()
            { 
                g = manager = $("#maingrid").ligerGrid({
                    toolbar: {items: [
                    	{ text: 'Add Line', click: addNewRow, icon:'add' },
                        { line:true },
                        { text: 'Del Line', click: deleteRow, icon:'delete' },
                        { line:true },
                        { text: 'Save Line', click: saveRow, icon:'save' },
                        { line:true },
                        { text: 'Search', click: filterData, icon:'search2' },
                        { line:true },
                        { text: 'Refresh', click: filterData, icon:'refresh' }
                    	]},	
                    title : 'Maintain',
                    pageSize:20,
                    contentType: 'application/json',
                    parms: data,
                    //字段配置
                    columns: [
                    { display: '*ExRType', name: 'ExRType', type: 'text', editor: { type: 'text'},width:100 },
                    { display: '*Year', name: 'Year', type: 'text', editor: { type: 'text'},width:100  },
                    { display: '*Period', name: 'Period', type: 'text', editor: { type: 'text'},width:100  },
                    { display: 'RatioFrom', name: 'RatioFrom', type: 'int', editor: { type: 'text'},width:100  },
                    { display: '*FromCurrency', name: 'FromCurrency', type: 'text', editor: { type: 'text'} ,width:100 },
                    { display: 'DirQuot', name: 'DirQuot', type: 'float', editor: { type: 'text'},width:100  },
                    { display: '*ToCurrency', name: 'ToCurrency', type: 'text', editor: { type: 'text'} ,width:100 },
                    { display: 'Method', isSort: false, width: 100, render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            if (!rowdata._editing)	
                            {
                                h += "<a href='javascript:beginEdit(" + rowindex + ")'>Change</a> ";
                            }
                            else
                            {
                                h += "<a href='javascript:endEdit(" + rowindex + ")'>Done</a> ";
                                h += "<a href='javascript:cancelEdit(" + rowindex + ")'>Back</a> "; 
                            }
                            return h;
                        }
                        }
                       
                    ],
                  //值为local，数据在客户端进行分页
                    dataAction:"local",
                  //数据请求地址
                    url:"../../../SelectConfigurationServlet",
                    onSelectRow: function (rowdata, rowindex)
                    {
                        $("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, 
                    isScroll: true, 
                    checkbox:true,
                    rownumbers:false,
                    clickToEdit: false,
                    hideLoadButton : true,
                    width: '99%'
                });   
            }
           }        
 
           
           
    //periodGrid的表格
    function periodGrid(){
    	
        $("#filterform").remove();
    	//创建筛选内容区域	
        $("#chkbefore").after("<div id=\"filterform\" class=\"liger-form\">" +
        		"<div class=\"fields\">" +
        		"<input data-type = \"text\" data-label = \"dbName\" data-name = \"dbName\" style = \"display: none\"/>" +
        		"<input data-type = \"text\" data-label = \"Period\" data-name = \"Period\" />" +
        		"</div>" +
        		"</div>");  
        
         //添加完元素之后重新刷新
          liger.inject.init();
        	   
          var form = liger.get("filterform");
          
          //维护对应sheet的数据库表名
          form.setData({ 
        	  dbName: 'sys_period',
        	  "Period": '%'
          });
          
          var data = form.getData();	
          
           $(f_initGrid);
            function f_initGrid()
            { 
                g = manager = $("#maingrid").ligerGrid({
                    toolbar: {items: [
                    	{ text: 'Add Line', click: addNewRow, icon:'add' },
                        { line:true },
                        { text: 'Del Line', click: deleteRow, icon:'delete' },
                        { line:true },
                        { text: 'Save Line', click: saveRow, icon:'save' },
                        { line:true },
                        { text: 'Search', click: filterData, icon:'search2' },
                        { line:true },
                        { text: 'Refresh', click: filterData, icon:'refresh' }
                    	]},	
                    title : 'Maintain',
                    pageSize:20,
                    contentType: 'application/json',
                    parms: data,
                    //字段配置
                    columns: [
                    { display: 'Period', name: 'Period', type: 'text', editor: { type: 'text'},width:200 },
                    { display: 'Open/Close', name: 'OpenClose', type: 'text', editor: { type: 'text'},width:200  }
//                    { display: 'Method', isSort: false, width: 100, render: function (rowdata, rowindex, value)
//                        {
//                            var h = "";
//                            if (!rowdata._editing)	
//                            {
//                                h += "<a href='javascript:beginEdit(" + rowindex + ")'>Change</a> ";
//                            }
//                            else
//                            {
//                                h += "<a href='javascript:endEdit(" + rowindex + ")'>Done</a> ";
//                                h += "<a href='javascript:cancelEdit(" + rowindex + ")'>Back</a> "; 
//                            }
//                            return h;
//                        }
//                        }
                       
                    ],
                  //值为local，数据在客户端进行分页
                    dataAction:"local",
                  //数据请求地址
                    url:"../../../SelectConfigurationServlet",
                    onSelectRow: function (rowdata, rowindex)
                    {
                        $("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, 
                    isScroll: true, 
                    checkbox:true,
                    rownumbers:false,
                    clickToEdit: false,
                    hideLoadButton : true,
                    width: '99%'
                });   
            }
           }                
           
           
           
      //python的表格
    function pythonGrid(){
    	
        $("#filterform").remove();
    	//创建筛选内容区域	
        $("#chkbefore").after("<div id=\"filterform\" class=\"liger-form\">" +
        		"<div class=\"fields\">" +
        		"<input data-type = \"text\" data-label = \"dbName\" data-name = \"dbName\" style = \"display: none\"/>" +
        		"<input data-type = \"text\" data-label = \"Item\" data-name = \"Item\" />" +
        		"</div>" +
        		"</div>");  
        
         //添加完元素之后重新刷新
          liger.inject.init();
        	   
          var form = liger.get("filterform");
          
          //维护对应sheet的数据库表名
          form.setData({ 
        	  dbName: 'sys_configuration_to_python',
        	  "Item": '%'
          });
          
          var data = form.getData();	
          
           $(f_initGrid);
            function f_initGrid()
            { 
                g = manager = $("#maingrid").ligerGrid({
                    toolbar: {items: [
                    	{ text: 'Add Line', click: addNewRow, icon:'add' },
                        { line:true },
                        { text: 'Del Line', click: deleteRow, icon:'delete' },
                        { line:true },
                        { text: 'Save Line', click: saveRow, icon:'save' },
                        { line:true },
                        { text: 'Search', click: filterData, icon:'search2' },
                        { line:true },
                        { text: 'Refresh', click: filterData, icon:'refresh' }
                    	]},	
                    title : 'Maintain',
                    pageSize:20,
                    contentType: 'application/json',
                    parms: data,
                    //字段配置
                    columns: [
                    { display: 'Item', name: 'Item', type: 'text', editor: { type: 'text'},width:200 },
                    { display: 'Configuration', name: 'Configuration', type: 'text', editor: { type: 'text'},width:400  },
                    { display: 'Method', isSort: false, width: 100, render: function (rowdata, rowindex, value)
                        {
                            var h = "";
                            if (!rowdata._editing)	
                            {
                                h += "<a href='javascript:beginEdit(" + rowindex + ")'>Change</a> ";
                            }
                            else
                            {
                                h += "<a href='javascript:endEdit(" + rowindex + ")'>Done</a> ";
                                h += "<a href='javascript:cancelEdit(" + rowindex + ")'>Back</a> "; 
                            }
                            return h;
                        }
                        }
                       
                    ],
                  //值为local，数据在客户端进行分页
                    dataAction:"local",
                  //数据请求地址
                    url:"../../../SelectConfigurationServlet",
                    onSelectRow: function (rowdata, rowindex)
                    {
                        $("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, 
                    isScroll: true, 
                    checkbox:true,
                    rownumbers:false,
                    clickToEdit: false,
                    hideLoadButton : true,
                    width: '99%'
                });   
            }
           }              
           
           
           
  //合并的公司代码表格
    function companyGrid(){
    	
        $("#filterform").remove();
    	//创建筛选内容区域	
        $("#chkbefore").after("<div id=\"filterform\" class=\"liger-form\">" +
        		"<div class=\"fields\">" +
        		"<input data-type = \"text\" data-label = \"dbName\" data-name = \"dbName\" style = \"display: none\"/>" +
        		"<input data-type = \"text\" data-label = \"ConsoLevel\" data-name = \"ConsoLevel\"/>" +
        		"<input data-type = \"text\" data-label = \"Company\" data-name = \"Company Code\" />" +	
        		"</div>" +
        		"</div>");  
        
         //添加完元素之后重新刷新
          liger.inject.init();
        	   
          var form = liger.get("filterform");
          
          //维护对应sheet的数据库表名
          form.setData({ 
        	  dbName: 'sys_conso_tp',
        	  "ConsoLevel": '%',
        	  "Company Code": '%'	
          });
          
          var data = form.getData();	
          
           $(f_initGrid);
            function f_initGrid()
            { 
                g = manager = $("#maingrid").ligerGrid({
                    toolbar: {items: [
                    	{ text: 'Add Line', click: addNewRow, icon:'add' },
                        { line:true },
                        { text: 'Del Line', click: deleteRow, icon:'delete' },
                        { line:true },
                        { text: 'Save Line', click: saveRow, icon:'save' },
                        { line:true },
                        { text: 'Search', click: filterData, icon:'search2' },
                        { line:true },
                        { text: 'Refresh', click: filterData, icon:'refresh' }
                    	]},	
                    title : 'Maintain',
                    pageSize:20,
                    contentType: 'application/json',
                    parms: data,
                    //字段配置
                    columns: [
                    { display: '*ConsoLevel', name: 'ConsoLevel', type: 'text', editor: { type: 'text'}, width: 150 },
                    { display: '*Company Code', name: 'Company Code', type: 'text', editor: { type: 'text'}, width: 150 },
                    { display: '*Trading Partner IRT', name: 'Trading Partner IRT', type: 'text', editor: { type: 'text'}, width: 150},
                    { display: 'Trading Partner SAP', name: 'Trading Partner SAP', type: 'text', editor: { type: 'text'},width: 150 }
//                    { display: 'Method', isSort: false, width: 100, render: function (rowdata, rowindex, value)
//                        {
//                            var h = "";
//                            if (!rowdata._editing)	
//                            {
//                                h += "<a href='javascript:beginEdit(" + rowindex + ")'>Change</a> ";
//                            }
//                            else
//                            {
//                                h += "<a href='javascript:endEdit(" + rowindex + ")'>Done</a> ";
//                                h += "<a href='javascript:cancelEdit(" + rowindex + ")'>Back</a> "; 
//                            }
//                            return h;
//                        }
//                        }  
                    ],
                  //值为local，数据在客户端进行分页
                    dataAction:"local",
                  //数据请求地址
                    url:"../../../SelectConfigurationServlet",
                    onSelectRow: function (rowdata, rowindex)
                    {
                        $("#txtrowindex").val(rowindex);
                    },
                    enabledEdit: true, 
                    isScroll: true, 
                    checkbox:true,
                    rownumbers:false,
                    clickToEdit: false,
                    hideLoadButton : true,
                    width: '99%'
                });   
            }
           }        
    
    
	      //以下是通用部分，一般不用更改代码
	//表格上保存所选row按钮
	function saveRow() {
		var form = liger.get("filterform");
		var data = form.getData();
		var row = manager.getSelectedRows();
		if (!row.length) {
			alert('Please choose the rows!')
		} else {
			manager.set({
						//值为local，数据在客户端进行分页
						dataAction : "local",
						//数据请求地址	
						url : "../../../SmartSaveRowServlet",
						contentType : 'application/json',
						parms : {
							Rows : row,
							Data : data
						}
					});
			alert("Save or updated successful");
		}
	}
	
	//表格上删除所有row按钮
	function deleteRow() {
		var form = liger.get("filterform");
		var data = form.getData();
		manager.deleteSelectedRow();
		var row = manager.getSelectedRows();
		if (!row.length) {
			alert('Please choose the rows!')
		} else {
			manager.set({
						//值为local，数据在客户端进行分页
						dataAction : "local",
						//数据请求地址	
						url : "../../../SmartDeleteRowServlet",
						contentType : 'application/json',
						parms : {
							Rows : row,
							Data : data
						}
					});
			alert("Delete Successful");
			filterData();
		}
	}
	
	//表格上添加行按钮按钮
	var newrowid = 100;
	function addNewRow() {
		var row = manager.getSelectedRow();
		//参数1:rowdata(非必填)
		//参数2:插入的位置 Row Data 
		//参数3:之前或者之后(非必填)
		manager.addEditRow({}, row, document.getElementById("chkbefore").checked);
	}
	
	//表格上搜索按钮按钮
	function filterData() {
		var form = liger.get("filterform");
		var data = form.getData();
		manager.set({
					//值为local，数据在客户端进行分页
					dataAction : "local",
					//数据请求地址	
					url : "../../../SelectConfigurationServlet",
					contentType : 'application/json',
					parms : data
				});
	}
	
	function beginEdit(rowid) {
		manager.beginEdit(rowid);
	}
	
	function cancelEdit(rowid) {
		manager.cancelEdit(rowid);
	}
	
	function endEdit(rowid) {
		manager.endEdit(rowid);
		var form = liger.get("filterform");
		var data = form.getData();
		var row = manager.getRow(rowid)
		manager.set({
					//值为local，数据在客户端进行分页
					dataAction : "local",
					//数据请求地址	
					url : "../../../SmartSaveRowServlet",
					contentType : 'application/json',
					parms : {
						Rows : [row],
						Data : data
					}
				});
		alert("Save Successful!");
	}
	
	        
        
        