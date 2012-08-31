// Use <? ?> for template tags.
var $ = jQuery.noConflict();
_.templateSettings = {
		evaluate:  /<\?(.+?)\?>/g,
		interpolate: /<\?=(.+?)\?>/g
};
// Set up openhmis "namespace"
if (window.openhmis === undefined) window.openhmis = {};
