$(document).ready(function() {

	$('.datePicker').datepicker({
	    format: 'dd-mm-yyyy',
	    autoclose: true,
	    language: 'es'
	});
	
	new ListSelectController($('#modalUsers'), $("#relatedUsers")).init();
	new ListSelectController($('#modalApps'), $("#relatedApps")).init();

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