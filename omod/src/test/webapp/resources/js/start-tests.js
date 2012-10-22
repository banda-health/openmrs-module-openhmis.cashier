(function() {
curl(
	{
		baseUrl: openhmis.config.wwwUrlRoot + "js"
	},
	[
		'lib/jquery',
		'model/item'
	],
	function($, openhmis) {
		var testBaseUrl = '/src/test/webapp/resources/js';
		curl(
			[
				'js!' + testBaseUrl + '/model/generic.js',
				'js!' + testBaseUrl + '/model/item.js',
				'js!' + testBaseUrl + '/view/generic.js',
				'js!' + testBaseUrl + '/view/editors.js'
			],
			function() {
				window.reporter = new jasmine.HtmlReporter(); jasmine.getEnv().addReporter(reporter);
				jasmine.getEnv().execute();
			}
		);
	}
);
})();