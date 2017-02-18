
function popupWindow( popupid)
{
	$('#' + popupid).fadeIn();
	var popuptopmargin = ($('#' + popupid).height() + 10) / 2;
	var popupleftmargin = ($('#' + popupid).width() + 10) / 2;
	// Then using .css function style our popup box for center alignment
	$('#' + popupid).css({
		'margin-top' : -popuptopmargin,
		'margin-left' : -popupleftmargin
	});
}
function closeWindow( popupid)
{
	$('#' + popupid).fadeOut();
}
		
$(document).ready(function() {
						   
	// Here we will write a function when link click under class popup				   
	$('a.popup').click(function() {										
		var popupid = $(this).attr('rel');
		popupWindow( popupid);
		
				   
	});
	// Here we will write a function when link click under class close				   
	$('a.close').click(function() {										
		var popupid = $(this).attr('rel');
		closeWindow( popupid);
	});
});



