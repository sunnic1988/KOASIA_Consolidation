<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Consolidation</title>
    <!-- Bootstrap Core CSS -->
    <link href="bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Menu CSS -->
    <link href="../plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../plugins/bower_components/dropify/dist/css/dropify.min.css">
    <!-- animation CSS -->
    <link href="css/animate.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="css/style.css" rel="stylesheet">
    <!-- color CSS -->
    <link href="css/colors/blue.css" id="theme" rel="stylesheet">
    <!--alerts CSS -->
    <link href="../plugins/bower_components/sweetalert/sweetalert.css" rel="stylesheet" type="text/css">
    <!-- page CSS -->
    <link href="../plugins/bower_components/bootstrap-datepicker/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css" />
    <link href="../plugins/bower_components/custom-select/custom-select.css" rel="stylesheet" type="text/css" />
    <link href="../plugins/bower_components/switchery/dist/switchery.min.css" rel="stylesheet" />
    <link href="../plugins/bower_components/bootstrap-select/bootstrap-select.min.css" rel="stylesheet" />
    <link href="../plugins/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.css" rel="stylesheet" />
    <link href="../plugins/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.css" rel="stylesheet" />
    <link href="../plugins/bower_components/multiselect/css/multi-select.css" rel="stylesheet" type="text/css" />
     <!-- Editable CSS -->
    <link type="text/css" rel="stylesheet" href="../plugins/bower_components/jsgrid/dist/jsgrid.min.css" />
    <link type="text/css" rel="stylesheet" href="../plugins/bower_components/jsgrid/dist/jsgrid-theme.min.css" />
</head>

<body class="fix-header">
    <!-- ============================================================== -->
    <!-- Preloader -->
    <!-- ============================================================== -->
    <div class="preloader">
        <svg class="circular" viewBox="25 25 50 50">
            <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" />
        </svg>
    </div>
    <!-- ============================================================== -->
    <!-- Wrapper -->
    <!-- ============================================================== -->
    <div id="wrapper">
        <!-- ============================================================== -->
        <!-- Topbar header - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <nav class="navbar navbar-default navbar-static-top m-b-0">
            <div class="navbar-header">
                <div class="top-left-part">
                    <!-- Logo -->
                    <a class="logo" href="index.html"> <b style="font-size:24px; text-align:center"></>KOSTAL</b> </a>
                </div>
                <!-- /Logo -->
                <!-- Search input and Toggle icon -->
                <ul class="nav navbar-top-links navbar-right pull-right">
                    <li>
                        <form role="search" class="app-search hidden-sm hidden-xs m-r-10">
                            <input type="text" placeholder="Search..." class="form-control"> <a href=""><i class="fa fa-search"></i></a> </form>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-header -->
            <!-- /.navbar-top-links -->
            <!-- /.navbar-static-side -->
        </nav>
        <!-- End Top Navigation -->
        <!-- ============================================================== -->
        <!-- Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav slimscrollsidebar">
                <div class="sidebar-head">
                    <h3><span class="fa-fw open-close"><i class="ti-close ti-menu"></i></span> <span class="hide-menu">Navigation</span></h3> </div>
                <ul class="nav" id="side-menu">
                    <li> <a href="#" class="waves-effect active"><i class="mdi mdi-content-copy fa-fw"></i> <span class="hide-menu">Sample Pages</span></a>
                    </li>
                    <li><a href="inbox.html" class="waves-effect"><i class="mdi mdi-apps fa-fw"></i> <span class="hide-menu">Perpare<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                             <li><a href="upload.jsp"><i class="fa-fw">U</i><span class="hide-menu">Upload</span></a></li>
                        </ul>
                    </li>
                    <li class="devider"></li>
                    <li> <a href="forms.html" class="waves-effect"><i class="mdi mdi-clipboard-text fa-fw"></i> <span class="hide-menu">Posting<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="form-basic.html"><i class="fa-fw">C</i><span class="hide-menu">Consolidation</span></a></li>
                        </ul>
                    </li>
                     <li class="devider"></li>
                    <li> <a href="tables.html" class="waves-effect"><i class="mdi mdi-table fa-fw"></i> <span class="hide-menu">Reports<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="basic-table.html"><i class="fa-fw">P</i><span class="hide-menu">Profit and Loss</span></a></li>
                            <li><a href="basic-table.html"><i class="fa-fw">O</i><span class="hide-menu">Operating Costs</span></a></li>
                            <li><a href="basic-table.html"><i class="fa-fw">B</i><span class="hide-menu">Balance Sheet</span></a></li>
                        </ul>
                    </li>
                    <li class="devider"></li>
                    <li> <a href="#" class="waves-effect"><i class="mdi mdi-settings fa-fw"></i> <span class="hide-menu">Setting<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                            <li> <a href="Configuration.jsp"><i class="fa-fw">C</i><span class="hide-menu">Configuration</span></a> </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <!-- ============================================================== -->
        <!-- End Left Sidebar -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Page Content -->
        <!-- ============================================================== -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">	
                    <div class="col-md-12">
                        <div class="white-box">
                            <h3 class="box-title">Configuration Sheets</h3> 
                            <div class="button-box">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#UploadModal" data-whatever="@mdo" >Upload Excel Files</button>
                                <button type="button" class="btn btn-info" data-toggle="modal" data-target="#ExportModal" data-whatever="@mdo">Export Excel Files</button>
                                <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#DeleteModal" data-whatever="@mdo">Delete Database</button>
                            </div>
                           </div> 
                         </div>             
                            <!-- 上传弹出框-->	
                            <div class="modal fade" id="UploadModal" tabindex="-1" role="dialog" aria-labelledby="UploadModalLabel1">	
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="UploadModalLabel1">Upload Files</h4> </div>
                                        <div class="modal-body">
                                            <form action="../SmartUploadServlet.do" method="post" id="uploadFileForm1" enctype="multipart/form-data">
                                                <div class="form-group">
                                                <div class="row">
													<div class="col-xs-12">
														<div class="white-box">
															<label for="input-file-now">Choose your file upload into database!</label> 
															<input type="file" name="uploadfile1" id="input-file-now" class="dropify" />
														</div>
													</div>
												</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal" >Close</button>
                                            <button type="button" class="btn btn-primary" id="sa-success">Upload</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 上传弹出框 -->
                            
                            <!-- 导出弹出框-->
                            <div class="modal fade" id="ExportModal" tabindex="-1" role="dialog" aria-labelledby="ExportModalLabel1">	
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="ExportModalLabel1">Export Files</h4> </div>
                                        <div class="modal-body">
                                            <form action="../SmartDownloadServlet.do" method="get" id="exportFileForm1" enctype="application/x-www-form-urlencoded">
												<div class="form-group">
													<div class="row">	
														<div class="col-sm-12">
															<div class="white-box">
															<h5 class="m-t-20">Choose your Database and Export</h5>
															
															<!--传输复选框内容  -->
															<input type="text" id = "ExpSelection" name = "ExpSelection" value ="" style ="display: none"></input> 
															<!--传输复选框内容  -->
															
																<select class="select2 m-b-10 select2-multiple" multiple="multiple" data-placeholder="Choose Database" id ="select_Expform">
																<optgroup label="Name of Database">
																	<option value="sys_tp_config">sys_tp_config_db</option>
																	<option value="sys_cons_tp_config">sys_cons_tp_config_db</option>
																	<option value="sys_cons_ap_config">sys_cons_ap_config_db</option>
																	<option value="sys_gl_description">sys_gl_description_db</option>
																	<option value="sys_exchage_rate">sys_exchage_rate_db</option>
																</optgroup>
															</select>
															</div>	
														</div>
													</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                            <button type="button" class="btn btn-primary" id="sa-successExp">Export</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                             <!-- 导出弹出框-->
                            	
                             <!-- 删除弹出框-->
                            <div class="modal fade" id="DeleteModal" tabindex="-1" role="dialog" aria-labelledby="DeleteModalLabel1">	
                                <div class="modal-dialog" role="document">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                            <h4 class="modal-title" id="DeleteModalLabel1">Delete Database</h4> </div>
                                        <div class="modal-body">
                                            <form action="../SmartDeleteServlet" method="get" id="deleteFileForm1" enctype="application/x-www-form-urlencoded">
												<div class="form-group">
													<div class="row">
														<div class="col-sm-12">
															<div class="white-box">
															<h5 class="m-t-20">Choose your Database and Delete</h5>

															<!--传输复选框内容  -->
															<input type="text" id = "DelSelection" name = "DelSelection" value ="" style ="display:none"></input> 
															<!--传输复选框内容  -->															
															
																<select class="select2 m-b-10 select2-multiple" multiple="multiple" data-placeholder="Choose Database" id ="select_Delform">
																<optgroup label="Name of Database">
																	<option value="sys_tp_config">sys_tp_config</option>
																	<option value="sys_cons_tp_config">sys_cons_tp_config</option>
																	<option value="sys_cons_ap_config">sys_cons_ap_config</option>
																	<option value="sys_gl_description">sys_gl_description</option>
																	<option value="sys_exchage_rate">sys_exchage_rate</option>
																</optgroup>
															</select>
															</div>	
														</div>
													</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                            <button type="button" class="btn btn-primary" id="sa-warning">Delete</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                             <!-- 删除弹出框--> 
                        </div>             
                <!--表格编辑框-->
				<div class="row">
					<div class="col-md-12">
						<div class="white-box">
					    <div class="col-md-4 col-md-offset-10">
							<select class="selectpicker m-b-20 m-r-10"data-style="btn-info btn-outline">
								<option data-tokens="ketchup mustard">Exchange Rate</option>
								<option data-tokens="mustard">Trading Partner</option>
								<option data-tokens="frosting">AR/AP Accounts</option>
								<option data-tokens="frosting">GL Description</option>
								<option data-tokens="frosting">TP Description</option>
							</select>
						</div>
							<h3 class="box-title"></h3>
							<div id="configurationgrid"></div>
						</div>
					</div>
				</div>
			</div>
            <!-- /.container-fluid -->
            <footer class="footer text-center"> 2018 KOSTAL China HC Made </footer>
        </div>
        <!-- ============================================================== -->
        <!-- End Page Content -->
        <!-- ============================================================== -->
    </div>
    <!-- /#wrapper -->
    <!-- jQuery -->
    <script src="../plugins/bower_components/jquery/dist/jquery.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- Menu Plugin JavaScript -->
    <script src="../plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
    <!--slimscroll JavaScript -->
    <script src="js/jquery.slimscroll.js"></script>
    <!--Wave Effects -->
    <script src="js/waves.js"></script>
    <!-- Sweet-Alert  -->
    <script src="../plugins/bower_components/sweetalert/sweetalert.min.js"></script>
    <script src="../plugins/bower_components/sweetalert/jquery.sweet-alert.custom.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="js/custom.min.js"></script>
    <script src="../plugins/bower_components/switchery/dist/switchery.min.js"></script>
    <script src="../plugins/bower_components/custom-select/custom-select.min.js" type="text/javascript"></script>
    <script src="../plugins/bower_components/bootstrap-select/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="../plugins/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
    <script src="../plugins/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../plugins/bower_components/multiselect/js/jquery.multi-select.js"></script>
    <!--Style Switcher -->
    <script src="../plugins/bower_components/styleswitcher/jQuery.style.switcher.js"></script>
    <!-- jQuery file upload -->
    <script src="../plugins/bower_components/dropify/dist/js/dropify.min.js"></script>
    <!-- Editable -->
    <script src="../plugins/bower_components/jsgrid/configurationdb.js"></script>
    <script src="../plugins/bower_components/jsgrid/db.js"></script>
    <script type="text/javascript" src="../plugins/bower_components/jsgrid/dist/jsgrid.min.js"></script>
    <script src="js/jsgrid-init.js"></script>
    <script>
    $(document).ready(function() {
        // Basic
        $('.dropify').dropify();
        // Translated
        $('.dropify-fr').dropify({
            messages: {
                default: 'Glissez-déposez un fichier ici ou cliquez',
                replace: 'Glissez-déposez un fichier ou cliquez pour remplacer',
                remove: 'Supprimer',
                error: 'Désolé, le fichier trop volumineux'
            }
        });
        // Used events
        var drEvent = $('#input-file-events').dropify();
        drEvent.on('dropify.beforeClear', function(event, element) {
            return confirm("Do you really want to delete \"" + element.file.name + "\" ?");
        });
        drEvent.on('dropify.afterClear', function(event, element) {
            alert('File deleted');
        });
        drEvent.on('dropify.errors', function(event, element) {
            console.log('Has Errors');
        });
        var drDestroy = $('#input-file-to-destroy').dropify();
        drDestroy = drDestroy.data('dropify')
        $('#toggleDropify').on('click', function(e) {
            e.preventDefault();
            if (drDestroy.isDropified()) {
                drDestroy.destroy();
            } else {
                drDestroy.init();
            }
        })
    });
    </script>
       <script>
        jQuery(document).ready(function() {
            // Switchery
            var elems = Array.prototype.slice.call(document.querySelectorAll('.js-switch'));
            $('.js-switch').each(function() {
                new Switchery($(this)[0], $(this).data());
            });
            // For select 2
            $(".select2").select2();
            $('.selectpicker').selectpicker();
            //Bootstrap-TouchSpin
            $(".vertical-spin").TouchSpin({
                verticalbuttons: true,
                verticalupclass: 'ti-plus',
                verticaldownclass: 'ti-minus'
            });
            var vspinTrue = $(".vertical-spin").TouchSpin({
                verticalbuttons: true
            });
            if (vspinTrue) {
                $('.vertical-spin').prev('.bootstrap-touchspin-prefix').remove();
            }
            $("input[name='tch1']").TouchSpin({
                min: 0,
                max: 100,
                step: 0.1,
                decimals: 2,
                boostat: 5,
                maxboostedstep: 10,
                postfix: '%'
            });
            $("input[name='tch2']").TouchSpin({
                min: -1000000000,
                max: 1000000000,
                stepinterval: 50,
                maxboostedstep: 10000000,
                prefix: '$'
            });
            $("input[name='tch3']").TouchSpin();
            $("input[name='tch3_22']").TouchSpin({
                initval: 40
            });
            $("input[name='tch5']").TouchSpin({
                prefix: "pre",
                postfix: "post"
            });
            // For multiselect
            $('#pre-selected-options').multiSelect();
            $('#optgroup').multiSelect({
                selectableOptgroup: true
            });
            $('#public-methods').multiSelect();
            $('#select-all').click(function() {
                $('#public-methods').multiSelect('select_all');
                return false;
            });
            $('#deselect-all').click(function() {
                $('#public-methods').multiSelect('deselect_all');
                return false;
            });
            $('#refresh').on('click', function() {
                $('#public-methods').multiSelect('refresh');
                return false;
            });
            $('#add-option').on('click', function() {
                $('#public-methods').multiSelect('addOption', {
                    value: 42,
                    text: 'test 42',
                    index: 0
                });
                return false;
            });
        });
        </script>
 
    
</body>

</html>
