(function() {
var testPrefix = '/home/daniel/workspace/openhmis/cashier/omod';
window.OPENHMIS_BASE_URL = '/src/main/webapp/resources/js';
if (document.URL !== "http://localhost:8234/")
	OPENHMIS_BASE_URL = testPrefix + OPENHMIS_BASE_URL;
curl(
	{
		baseUrl: OPENHMIS_BASE_URL
	},
	[
		'lib/jquery',
		'model/item'
	],
	function($, openhmis) {
		var testBaseUrl = '/src/test/webapp/resources/js';
		if (document.URL !== "http://localhost:8234/")
			testBaseUrl = testPrefix + testBaseUrl;
		
		curl(
			[
				'js!' + testBaseUrl + '/model/item.js'
			],
			function() {
				window.reporter = new jasmine.HtmlReporter(); jasmine.getEnv().addReporter(reporter);
				jasmine.getEnv().execute();
			}
		);
	}
);
})();