$(document).ready(function() {

	$('#relatedAppAdd').bind('click', function(e) {
		e.preventDefault();
		$('#modalApps').modal('show');
	});

	$('#relatedUsersAdd').bind('click', function(e) {
		e.preventDefault();
		$('#modalUsers').modal('show');
	});
	
	$('.modal').modal({
		keyboard: true,
		show: false
	});
	
	$('.datePicker').datepicker({
	    format: 'dd-mm-yyyy',
	    autoclose: true,
	    language: 'es'
	});
});