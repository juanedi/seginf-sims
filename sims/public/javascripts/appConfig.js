$(document).ready(function() { 
 
	var submitButton = $('#submitUsers');
	
	$('td.enableUser input').bind('change', function(e) {
		var checked = $(e.currentTarget).is(":checked");
		var rolesCheckboxes = $(e.target).closest('tr').find('td.addRole input')
		if (checked) {
			rolesCheckboxes.removeAttr('disabled');
		} else {
			rolesCheckboxes.attr('disabled', 'disabled');
		}
	});

	submitButton.bind('click', function() {
		submitButton.attr('disabled', 'disabled');
		var userConfigurations = [];
		$('#appUserData tr.userRow').each(function(i, e) {
			var element = $(e);
			userConfiguration = {
					enabled: element.find('.enableUser input').is(':checked'),
					username: element.find('.userName').text(),
					roles: [],
			};
			element.find('.addRole input:checked').each(function(j, r) {
				userConfiguration.roles.push($(r).attr('class'));
			});
			userConfigurations.push(userConfiguration);
		});
		
		$.ajax({
		    type: 'PUT',
		    contentType: 'application/json',
		    url: location.href + '?userConfigurations=' + JSON.stringify(userConfigurations),
		    complete: function(xmlHttpRequest) {
		    	var status = xmlHttpRequest.status;
		    	if (status == 204) {
		    		$("#changeSuccess").fadeIn('fast').delay(2000).fadeOut('fast');
		    	} else {
		    		$("#changeError").fadeIn('fast').delay(2000).fadeOut('fast');
		    	}
		    	submitButton.removeAttr('disabled');
		    }
		}); 
		
	});
	
	$('#resetUsers').bind('click', function() {
		var conf = confirm("¿Seguro que desea resetear la información que modificó?");
		if (conf) {
			location.reload();
		}
	});
	
}); 
