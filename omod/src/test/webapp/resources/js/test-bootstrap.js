window.openhmis = {};
openhmis.config = {
	wwwUrlRoot: "/src/main/webapp/resources/",
	restUrlRoot: "/rest/"
}

// Global utility function for loading JS modules during a Jasmine spec
var require = function(map, libs) {
	libs = libs ? libs : {};
	for (var lib in libs) { delete libs[lib]; }
	var list = [];
	for (var lib in map) { list.push(lib); }
	curl({ baseUrl: openhmis.config.wwwUrlRoot + "js"}, list, function(something, somethinelse) {
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