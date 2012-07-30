$(document).ready(function() {

	$('.datePicker').datepicker({
	    format: 'dd-mm-yyyy',
	    autoclose: true,
	    language: 'es'
	});
	
	new ListSelectController($('#modalUsers'), $("#relatedUsers")).init();
	new ListSelectController($('#modalApps'), $("#relatedApps")).init();

	$('#submitBtn').bind('click', submit);
});


var submit = function(e) {
	e.preventDefault();
	
	$("#errorAlert").hide();
	$("#infoAlert").hide();
	
	var eventType = $('#eventType').val();

	var dateSince = $('#dateSince').val();
	
	var dateTo = $('#dateTo').val();
	
	var user = $('#user').val();

	var relatedUsers = [];
	$('#relatedUsers ul li').each(function(i, e) {
		relatedUsers.push($(e).text());
	});
	
	var relatedApps = [];
	$('#relatedApps ul li').each(function(i, e) {
		relatedApps.push($(e).text());
	});
	
	var searchQuery = {
			eventType: eventType != "ALL" ? eventType : null,
			user: user != "ALL" ? user : null,
			relatedUsers: relatedUsers.length > 0 ? relatedUsers.join(',') : null,
			relatedApps: relatedApps.length > 0 ? relatedApps.join(',') : null,
			since: dateSince? dateSince : null,
			to: dateTo? dateTo : null
	}
	
  $.get(doSearch(searchQuery), processSearchResults);
	
};

var processSearchResults = function(result) {
	if (result.errors && result.errors.length > 0) {
		var errorMsg = $("#errorAlert");
		errorMsg.empty();
		for(var i = 0; i < result.errors.length; i++) {
			var delim = i == 0 ? "" : "<br>";
			errorMsg.html(errorMsg.html() + delim + result.errors[i]);
		}
		errorMsg.fadeIn("fast");
	} else {
		if (result.events && result.events.length > 0) {
			infoMsg = $("#infoAlert");
			infoMsg.empty();
			infoMsg.text("Se encontraron " + result.events.length + " resultados. Todavía no los mostramos :P");
			infoMsg.fadeIn("fast");
		} else {
			infoMsg = $("#infoAlert");
			infoMsg.empty();
			infoMsg.text("No se encontró ningún evento para su búsqueda");
			infoMsg.fadeIn("fast");
		}
	}
}


/**
 * Controller para las listas que se editan con ventanas modales.
 */
ListSelectController = function($modalWindow, $input) {
	this.$modalWindow = $modalWindow;
	this.$input = $input;
}

/**
 * Inicializa el componente.
 */
ListSelectController.prototype.init = function() {
	
	this.$modalWindow.hide();

	this.$modalWindow.modal({
		keyboard: true,
		show: false
	});

	$('a', this.$modalWindow).bind('click', $.proxy(this._updateItems, this));
	$('a', this.$input).bind('click', $.proxy(this._showModal, this));
}

/**
 * Muestra la ventana modal.
 */
ListSelectController.prototype._showModal = function(e) {
	e.preventDefault();
	this._loadSelected();
	this.$modalWindow.modal('show');
}

/**
 * Marca los checkbox según los items que ya hayan sido seleccionados.
 */
ListSelectController.prototype._loadSelected = function() {

	$('input:checked', this.$modalWindow).removeAttr('checked');

	$('ul li', this.$input).each(function(i, e){
		var item = $(e).text();
		$('input#' + item).attr('checked', 'checked');
	});
	
}

/**
 * Actualiza los elementos de la lista según lo que haya marcado el usuario.
 */
ListSelectController.prototype._updateItems = function(e) {
	e.preventDefault();
	
	var selectedItems = [];
	$('input:checked', this.$modalWindow).each(function(i, e){
		var item = $(e).parent().text();
		selectedItems.push(item);
	});
	
	var ul =$('ul', this.$input);
	
	ul.empty();
	
	for(var i = 0; i < selectedItems.length; i++) {
		ul.append('<li>' + selectedItems[i] + '</li>');
	}

	if (ul.is(':empty')) {
		ul.hide();
	} else {
		ul.show();
	}
	
	this.$modalWindow.modal('hide');
}