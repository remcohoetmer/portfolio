
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="css/jquery-ui.css" />
		<link rel="stylesheet" href="css/style.css" />
  		<link rel="shortcut icon" href="favicon.ico" />
  
		<title>Groepen Beheer</title>
		<style type="text/css" title="currentStyle">
			@import "css/demo_page.css";
			@import "css/demo_table.css";
		</style>
		<script type="text/javascript" language="javascript" src="js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
		<script type="text/javascript" language="javascript" src="js/ui/1.10.2/jquery-ui.js"></script>
		<script type="text/javascript" language="javascript" src="includes/windows.js"></script>
		<script type="text/javascript" language="javascript" src="includes/filter.js"></script>
		<script type="text/javascript" language="javascript" src="includes/klanten.js"></script>
		<script type="text/javascript" language="javascript" src="includes/groepOverview.js"></script>
		<script type="text/javascript" language="javascript" src="includes/groepCreate.js"></script>
		<script type="text/javascript" language="javascript" src="includes/groepAddLeden.js"></script>
		<script type="text/javascript" language="javascript" src="includes/groepEdit.js"></script>
		<script type="text/javascript" charset="utf-8">

var service_path= "rest/groep";
$(document).ready(function() {
	
	$('#closePopupboxEdit').click( function() {
		searchGroepen();
		closeWindow( "popupboxEdit");
	} );
	
	$('#closePopupboxCreate').click( function() {
		searchGroepen(); 
		closeWindow( "popupboxCreate");
	} );

	$('#closePopupboxAddLeden').click( function() {
		jump2EditGroep( ); 
		closeWindow( "popupboxAddLeden");
	} );
	
	$('#closeGebruikersgroepenBeheer').click( function() {
		window.location.href="index.jsp";
	} );
	searchGroepen();
});

		</script>
	</head>
	<body id="dt_example">
		<a id="closeGebruikersgroepenBeheer"><img class="close_button" src="images/close_button.png"/></a>
		<div id="container">
			<h1>Groepen Overzicht</h1>
			<%@include file="includes/groepOverview.jspx" %>
		</div>
		
		<div id="popupboxEdit">
			<a id="closePopupboxEdit"><img class="close_button" src="images/close_button.png"/></a>
			<h1>Edit Groep</h1>
			<%@include file="includes/groepEdit.jspx" %>
		</div>
		
		<div id="popupboxCreate">
			<a id="closePopupboxCreate"><img class="close_button" src="images/close_button.png"/></a>
			<h1>Nieuwe Groep</h1>
			<%@include file="includes/groepCreate.jspx" %>
		</div>

		<div id="popupboxAddLeden">
			<a id="closePopupboxAddLeden"><img class="close_button" src="images/close_button.png"/></a>
			<h1>Groepsleden toevoegen</h1>
			<%@include file="includes/klanten.jspx" %>
			<%@include file="includes/groepAddLeden.jspx" %>
		</div>
	</body>
</html>