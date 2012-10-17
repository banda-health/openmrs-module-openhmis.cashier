window.openhmis = {};
openhmis.config = {
	wwwUrlRoot: '/'
}
var jQuery, _, Backbone = {}, i18n, openhmis;
Backbone.Form = undefined;
openhmis.ItemCode = function() {}

var define = function(name, includes, callback) {
	if (name instanceof Array) {
		callback = includes;
		includes = name;
		name = undefined;
	}
	var includesMap = {
		'lib/jquery': jQuery,
		'lib/underscore': _,
		'lib/backbone': Backbone,
		'lib/i18n': i18n,
		'openhmis': openhmis,
		'lib/backbone-forms': Backbone.Form
	}
	argv = [];
	for (var i in includes) {
		argv[i] = includesMap[includes[i]] ? includesMap[includes[i]] : openhmis;
	}
	var result = callback.apply(window, argv);
	switch (name) {
		case 'lib/backbone':
			window.Backbone = result;
			break;
		case 'lib/i18n':
			window.i18n = result;
			break;
	}
}