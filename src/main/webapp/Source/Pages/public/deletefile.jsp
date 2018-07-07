<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>export</title>
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
    <!-- page CSS -->
    <link href="../../../plugins/bower_components/bootstrap-datepicker/bootstrap-datepicker.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../plugins/bower_components/custom-select/custom-select.css" rel="stylesheet" type="text/css" />
    <link href="../../../plugins/bower_components/switchery/dist/switchery.min.css" rel="stylesheet" />
    <link href="../../../plugins/bower_components/bootstrap-select/bootstrap-select.min.css" rel="stylesheet" />
    <link href="../../../plugins/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.css" rel="stylesheet" />
    <link href="../../../plugins/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.css" rel="stylesheet" />
    <link href="../../../plugins/bower_components/multiselect/css/multi-select.css" rel="stylesheet" type="text/css" />    
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
    <script src="../../../plugins/bower_components/switchery/dist/switchery.min.js"></script>
    <script src="../../../plugins/bower_components/custom-select/custom-select.min.js" type="text/javascript"></script>
    <script src="../../../plugins/bower_components/bootstrap-select/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="../../../plugins/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.min.js"></script>
    <script src="../../../plugins/bower_components/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../plugins/bower_components/multiselect/js/jquery.multi-select.js"></script>    
    <!--Style Switcher -->
    <script src="../../../plugins/bower_components/styleswitcher/jQuery.style.switcher.js"></script>

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
        
     <script type="text/javascript">
        
     function deleteFile(){ 
    	 
     	var str= $("#sheetsSelection").val();
    	$("#DelSelection").val(str);
        $("#deleteFileForm1").submit()
        }	
     
       </script>
        
</head>
<body>
	<form action="../../../SmartDeleteServlet" method="get" id="deleteFileForm1" enctype="application/x-www-form-urlencoded" name="deleteFileForm1">
		<!-- .row -->
		<div class="row">
			<div class="white-box">
				<div class="form-group">
				  <label class="col-md-12">Delete Files</label>
					<div class="col-sm-12">
						<select class="select2 m-b-10 select2-multiple" multiple="multiple" data-placeholder="Choose" id="sheetsSelection">
							<optgroup label="Configuration Sheets">
								<option value="sys_gl_masterdata">GL Master Data</option>
								<option value="sys_exchage_rate">Exchange Rate</option>
								<option value="sys_conso_tp">Trading Partner</option>
								<option value="t0015_pl_cc_structure_op">Mapping for Cost Center</option>								
								<option value="t0016_pl_oc_structure">Mapping for Cost Elements</option>								
							</optgroup>
							<optgroup label="Transfer IRT">
								<option value="report_irt_structure">F.01</option>
								<option value="report_irt_gl_balance">Accounts Balance</option>
								<option value="report_irt_investment">Investment</option>
								<option value="report_irt_plstructure">PL_Structure</option>
								<option value="report_irt_basic_info_plstructure">PL_Structure_after_mapping</option>
								<option value="report_irt_basic_info">Transfer IRT Details</option>							
							</optgroup>
						</select>
					</div>
				</div>
				<div class="col-sm-12">
					<button class="btn btn-block btn-danger"
						style="text-align: center; width: 100%;" onclick="deleteFile()">Delete</button>
				</div>
			</div>
		</div>
		<!--传输复选框内容  -->
		<input type="text" id = "DelSelection" name = "DelSelection" value ="" style ="display: none"></input> 
		<!--传输复选框内容  -->
	</form>
</body>
</html>