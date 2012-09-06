// Use <? ?> for template tags.
var $ = jQuery.noConflict();
_.templateSettings = {
	evaluate:  /<\?(.+?)\?>/g,
	interpolate: /<\?=(.+?)\?>/g
};
// Set up openhmis "namespace"
if (window.openhmis === undefined) window.openhmis = {};
if (window.openhmis.template === undefined) window.openhmis.templates = {};

// Use uuid for id
Backbone.Model.prototype.idAttribute = 'uuid';

/**
 * Template helper function
 *
 * Fetches a template from a remote URI unless it has been previously fetched
 * and cached.
 */
Backbone.View.prototype.tmplFileRoot = '/openmrs/moduleResources/openhmis/cashier/template/';
Backbone.View.prototype.getTemplate = function(context) {
	var view = this;
	if (window.openhmis.templates[view.tmplFile] === undefined) {
		var uri = view.tmplFileRoot === undefined ? view.tmplFile : view.tmplFileRoot + view.tmplFile;
		$.ajax({
			url: uri,
			async: false,
			dataType: "html",
			success: function(data, status, jq) {
				openhmis.templates[view.tmplFile] = $("<div/>").html(data);
			}
		});
	}
	return _.template($(openhmis.templates[view.tmplFile]).find(view.tmplSelector).html());
}

// Capitalize first letter
capitalize = function(string) {
	return string.charAt(0).toUpperCase() + string.substring(1);
}