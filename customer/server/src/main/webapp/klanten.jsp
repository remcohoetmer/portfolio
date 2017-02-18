
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="css/jquery-ui.css" />
		<link rel="stylesheet" href="css/style.css" />
  		<link rel="shortcut icon" href="favicon.ico" />

		<title>Gebruikersgroepen Beheer</title>
		<style type="text/css" title="currentStyle">
			@import "css/demo_page.css";
			@import "css/demo_table.css";
		</style>
		<script type="text/javascript" language="javascript" src="js/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="js/jquery.dataTables.js"></script>
		<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
		<script type="text/javascript" language="javascript" src="includes/filter.js"></script>
		<script type="text/javascript" language="javascript" src="includes/klanten.js"></script>
		<script type="text/javascript" language="javascript">
		$(document).ready(function() {
			$('#closeGebruikersBeheer').click( function() {
				window.location.href="index.jsp";
			} );
	
			searchGebruikers();
		});
		
		</script>
	</head>
	<body id="dt_example">
		<a id="closeGebruikersBeheer"><img class="close_button" src="images/close_button.png"/></a>
		<div id="container">
			<h1>
				Klanten Overzicht
			</h1>
			<%@include file="includes/klanten.jspx" %>
		</div>
	</body>
</html>