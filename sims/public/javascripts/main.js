$(document).ready(function() 
    { 
        $(".alert-success").delay(3000).fadeOut('fast');
        
        $("select").each(function(i,e) {
        	if ($(e).attr("size") == 1) {
        		$(e).removeAttr("size");
        	}
        });
    } 
); 
