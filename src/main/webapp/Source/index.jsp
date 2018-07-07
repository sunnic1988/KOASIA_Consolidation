<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>KOASIA Consolidation</title>
    <link rel="stylesheet" type="text/css" id="mylink"/>
    <link href="lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />  
    <script src="lib/jquery/jquery-1.9.0.min.js" type="text/javascript"></script>    
    <script src="lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>  
    <script src="lib/jquery.cookie.js"></script>
    <script src="lib/json2.js"></script>
    <script src="lib/main.js"></script>
    <link href="lib/main.css" rel="stylesheet" type="text/css" />  
    <script src="indexdata.js" type="text/javascript"></script>
        
</head>

<body style="padding:0px;background:#EAEEF5;">  
<div id="pageloading"></div>  
	<div id="topmenu" class="l-topmenu">
	    <div class="l-topmenu-logo">KOSTAL</div>
	    <div class="l-topmenu-welcome">
	        <span class="space">|</span>
	        <a href="" class="l-link2" target="_blank">Download</a> 
	        <span class="space">|</span>
	        <a href="" class="l-link2" target="_blank">Support</a> 
	    </div> 
	</div>
	<div id="layout1"
		style="width: 99.2%; margin: 0 auto; margin-top: 4px;">
		<div position="left" title="Main Menu" id="accordion1">
			<!--  树形结构暂时不用
                    <div title="功能列表" class="l-scroll">
                         <ul id="tree1" style="margin-top:3px;">
                    </div>
                     -->
			<div title="Prepare">
				<div style="height: 7px;"></div>
				<a class="l-link"
					href="javascript:f_addTab('Tab_Upload','Upload','Pages/Prepare/upload.jsp')"
					target="_blank">Daily IRT</a>
			</div>
			<!--                      <div title="Posting">
                    <div style=" height:7px;"></div>
                          <a class="l-link" href="javascript:f_addTab('Tab_Consolidation','Consolidation','Pages/Posting/Consolidation.jsp')"  target="_blank">Consolidation</a> 
                    </div> 
                      <div title="Reports">
                    <div style=" height:7px;"></div>
                          <a class="l-link" href="javascript:f_addTab('Tab_ReportPL','ReportPL','Pages/Reports/pl.jsp')"  target="_blank">Profit and Loss</a> 
                          <a class="l-link" href="javascript:f_addTab('Tab_ReportOC','ReportOC','Pages/Reports/oc.jsp')" target="_blank">Operating Costs</a> 
                          <a class="l-link" href="javascript:f_addTab('Tab_ReportBS','ReportBS','Pages/Reports/bs.jsp')" target="_blank">Balance Sheets</a> 
                    </div> -->
			<div title="Configuration">
				<div style="height: 7px;"></div>
				<a class="l-link"
					href="javascript:f_addTab('Tab_Configuration','Configuration','Pages/Configuration/Configuration.jsp')"
					target="_blank">Configuration</a>
			</div>


		</div>
		<div position="center" id="framecenter">
			<div tabid="home" title="Home Page" style="height: 300px">
				<iframe frameborder="0" name="home" id="home" src="welcome.htm"></iframe>
			</div>
		</div>
	</div>
	<div  style="height:32px; line-height:32px; text-align:center;">
            © 2018 KOSTAL China HC
    </div>
</body>
</html>
