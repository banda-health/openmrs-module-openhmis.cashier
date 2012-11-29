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
				'js!' + testBaseUrl + '/openhmis.js',
				'js!' + testBaseUrl + '/model/generic.js',
				'js!' + testBaseUrl + '/model/item.js',
				'js!' + testBaseUrl + '/view/generic.js',
				'js!' + testBaseUrl + '/view/paginate.js',
				'js!' + testBaseUrl + '/view/editors.js',
				'js!' + testBaseUrl + '/view/bill.js',
				'js!' + testBaseUrl + '/view/item.js',
				'js!' + testBaseUrl + '/lib/i18n.js'
			],
			function() {
				$(function() {
					window.reporter = new jasmine.HtmlReporter(); jasmine.getEnv().addReporter(reporter);
					jasmine.getEnv().execute();
				});
			}
		);
	}
);
})();