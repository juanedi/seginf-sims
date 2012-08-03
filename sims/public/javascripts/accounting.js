$(document).ready(function() {

	$('.datePicker').datepicker({
	    format: 'dd-mm-yyyy',
	    autoclose: true,
	    language: 'es'
	});
	
	new SearchController($("form")).init();
	new ResultsController($('#searchResults')).init();
});

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

SearchController = function($form) {
	this.$form = $form;
	this.$errorAlert = $("#errorAlert", $form);
	this.$infoAlert = $("#infoAlert", $form);
	this.$eventTypeInput = $('#eventType', $form);
	this.$dateSinceInput = $('#dateSince', $form);
	this.$dateToInput = $('#dateTo', $form);
	this.$userInput = $('#user', $form);
	this.$relatedUsers = $("#relatedUsers", $form);
	this.$relatedApps = $("#relatedApps", $form);
}

SearchController.prototype.init = function() {
	$(document).bind('newSearch', $.proxy(this._onNewSearch, this));
	$('#submitBtn').bind('click', $.proxy(this._submit, this));
	new ListSelectController($('#modalUsers'), this.$relatedUsers).init();
	new ListSelectController($('#modalApps'), this.$relatedApps).init();
}

SearchController.prototype._onNewSearch = function() {
	this.$form.show();
}

SearchController.prototype._submit = function(e) {
	e.preventDefault();
	
	this.$errorAlert.hide();
	this.$infoAlert.hide();
	
	var eventType = this.$eventTypeInput.val();
	var dateSince = this.$dateSinceInput.val();
	var dateTo = this.$dateToInput.val();
	var user = this.$userInput.val();

	var relatedUsers = [];
	$('ul li', this.$relatedUsers).each(function(i, e) {
		relatedUsers.push($(e).text());
	});
	
	var relatedApps = [];
	$('ul li', this.$relatedApps).each(function(i, e) {
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
	
  $.get(doSearch(searchQuery), $.proxy(this._processSearchResults, this));
	
};

SearchController.prototype._processSearchResults = function(result) {
	if (result.errors && result.errors.length > 0) {
		this.$errorAlert.empty();
		for(var i = 0; i < result.errors.length; i++) {
			var delim = i == 0 ? "" : "<br>";
			this.$errorAlert.html(this.$errorAlert.html() + delim + result.errors[i]);
		}
		this.$errorAlert.fadeIn("fast");
	} else {
		if (result.events && result.events.length > 0) {
			$(document).trigger("newSearchReady", result);
			this.$form.hide();
		} else {
			this.$infoAlert.empty();
			this.$infoAlert.text("No se encontró ningún evento para su búsqueda");
			this.$infoAlert.fadeIn("fast");
		}
	}
}

ResultsController = function($panel) {
	this.$panel = $panel;
	this.$table = $("table", $panel);
}

ResultsController.prototype.init = function() {
	$(document).bind('newSearch', $.proxy(this._onNewSearch, this));
	$(document).bind('newSearchReady', $.proxy(this._showResults, this));
	
	$('#searchAgain', this.$panel).bind('click', function(e) {
		e.preventDefault();
		$(document).trigger("newSearch");
	});
}

ResultsController.prototype._showResults = function(e, result) {
	for(var i = 0; i < result.events.length; i++) {
		var event = result.events[i];
		this.$table.append('<tr><td>' + event.date + '</td><td>' + event.responsible + '</td><td>' + event.description + '</td></tr>')
	}
	this.$panel.show();
}

ResultsController.prototype._onNewSearch = function() {
	$("tr", this.$table).remove();
	this.$panel.hide();
}