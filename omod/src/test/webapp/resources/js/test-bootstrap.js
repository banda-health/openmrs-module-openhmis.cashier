window.openhmis = {};
openhmis.config = {
	wwwUrlRoot: "/src/main/webapp/resources/",
	restUrlRoot: "/rest/"
}
var jQuery, _, Backbone = {}, i18n, openhmis;
Backbone.Form = undefined;

// htmlunit seems to need this... no clue why
openhmis.ItemCode = function() {}

// Testing define() that undoes all the good normally done by curl and adds all
// libraries to the global namespace, making it easier to run Jasmine test
// suites
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
	for (var i in includes)
		argv[i] = includesMap[includes[i]] ? includesMap[includes[i]] : openhmis;

	// Call JS modules with the libraries they need
	var result = callback.apply(window, argv);

	// Add certain special cases to window
	switch (name) {
		case 'lib/backbone':
			window.Backbone = result;
			break;
		case 'lib/i18n':
			window.i18n = result;
			break;
	}
}