
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="css/jquery-ui.css" />
		<link rel="stylesheet" href="css/style.css" />
  		<link rel="shortcut icon" href="favicon.ico" />

		<title>Organisatie Beheer</title>
		<style type="text/css" title="currentStyle">
			@import "css/demo_page.css";
			@import "css/demo_table.css";
		</style>
		<script type="text/javascript" language="javascript" src="js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
		<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
		<script type="text/javascript" language="javascript" src="includes/filter.js"></script>
		<script type="text/javascript" language="javascript" src="includes/windows.js"></script>
		<script type="text/javascript" language="javascript" src="includes/organisatie.js"></script>
		<script type="text/javascript" language="javascript" src="includes/organisatieCreate.js"></script>
		<script type="text/javascript" language="javascript" src="includes/organisatieEdit.js"></script>
		<script type="text/javascript" language="javascript">
		var service_path= "rest/organisatie";
	
		$(document).ready(function() {
			$('#closePopupboxEdit').click( function() {
				searchOrganisaties();
				closeWindow( "popupboxEdit");
			} );
		
			$('#closePopupboxCreate').click( function() {
				searchOrganisaties(); 
				closeWindow( "popupboxCreate");
			} );

			$('#closeMain').click( function() {
				window.location.href="index.jsp";
			} );
	
			searchOrganisaties();
		});
		
		</script>
	</head>
	<body id="dt_example">
		<a id="closeMain"><img class="close_button" src="images/close_button.png"/></a>
		<div id="container">
			<h1>Organisatie Beheer</h1>
			<%@include file="includes/organisatie.jspx" %>
		</div>
		<div id="popupboxEdit">
			<a id="closePopupboxEdit"><img class="close_button" src="images/close_button.png"/></a>
			<h1>Edit Organisatie</h1>
			<%@include file="includes/organisatieEdit.jspx" %>
		</div>
		
		<div id="popupboxCreate">
			<a id="closePopupboxCreate"><img class="close_button" src="images/close_button.png"/></a>
			<h1>Nieuwe Organisatie</h1>
			<%@include file="includes/organisatieCreate.jspx" %>
		</div>
	</body>
</html>
