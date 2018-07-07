<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Balance Sheets</title>


    <!-- Bootstrap Core CSS -->
    <link href="../../../ampleadmin-minimal/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- animation CSS -->
    <link href="../../../ampleadmin-minimal/css/animate.css" rel="stylesheet">
    <!-- Menu CSS -->
    <link href="../../../plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="../../../ampleadmin-minimal/css/style.css" rel="stylesheet">
    <!-- color CSS -->
    <link href="../../../ampleadmin-minimal/css/colors/default.css" id="theme" rel="stylesheet">
    <!-- jQuery -->
    <script src="../../../plugins/bower_components/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="../../../ampleadmin-minimal/bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- Menu Plugin JavaScript -->
    <script src="../../../plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
    <!--slimscroll JavaScript -->
    <script src="../../../ampleadmin-minimal/js/jquery.slimscroll.js"></script>
    <!--Wave Effects -->
    <script src="../../../ampleadmin-minimal/js/waves.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="../../../ampleadmin-minimal/js/custom.min.js"></script>
    <script src="../../../ampleadmin-minimal/js/jasny-bootstrap.js"></script>
    <!--Style Switcher -->
    <script src="../../../plugins/bower_components/styleswitcher/jQuery.style.switcher.js"></script>

<script type="text/javascript">
function uploadFile()//上传按钮提交
{
    document.forms['uploadFileForm1'].submit();
}

</script>

</head>
<body>
	<form action="../	../../SmartUploadServlet.do" method="post" id="uploadFileForm1" enctype="multipart/form-data">
		<div class="row">
		<div class="white-box p-l-20 p-r-20">
			<div class="form-group">
				<label class="col-md-12">File upload</label>
				<div class="col-sm-12">
					<div class="fileinput fileinput-new input-group" data-provides="fileinput">
						<div class="form-control" data-trigger="fileinput">
							<i class="glyphicon glyphicon-file fileinput-exists"></i> 
							<span class="fileinput-filename"></span>
						</div>
						<span class="input-group-addon btn btn-default btn-file"> 
						<span class="fileinput-new">Select file</span> 
						<span class="fileinput-exists">Change</span>
						<input type="file"name="uploadFile"></span> 
						<a href="#" class="input-group-addon btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
					</div>
				</div>
			</div>
			     <div class="col-sm-12">
                    <button class="btn btn-block btn-success" style ="text-align:center;width:100%;" onlick = "uploadFile()">Upload</button>
                 </div>	
		   </div>
        </div>
	</form>
</body>
</html>