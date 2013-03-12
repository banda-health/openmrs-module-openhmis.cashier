window.openhmis = {};
openhmis.url = {
	//wwwUrlRoot: "/target/classes/web/module/resources/", // Test minified JS
	backboneBase: "/target/jasmine/backboneforms/web/module/resources/",
	cashierBase: "/src/main/webapp/resources/",
	resources: "",
	rest: "/rest/"
}

// Global utility function for loading JS modules during a Jasmine spec
var require = function(toLoad, libs) {
	libs = libs ? libs : {};
	for (var lib in libs) { delete libs[lib]; }
	var list = [];
	if (toLoad instanceof Array) {
		var map = {};
		for (var l in toLoad)
			map[toLoad[l]] = null;
	}
	else {
		var map = toLoad;
	}
	for (var lib in map) { list.push(lib); }
	curl({ baseUrl: openhmis.url.resources }, list, function(something, somethinelse) {
		libList = {};
		for (var lib in arguments) {
			libs[map[list[lib]]] = arguments[lib];
		}
	});
	waitsFor(function() {
		for (var item in map) {
			if (libs[map[item]] !== undefined) continue;
			else return false;
		}
		return true;
	}, "required resources to load", 1000);
};