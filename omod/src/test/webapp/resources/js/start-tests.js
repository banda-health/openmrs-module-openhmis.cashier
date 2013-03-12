(function() {
curl(
	{
		baseUrl: openhmis.url.resources
	},
	[
		openhmis.url.backboneBase + 'js/lib/jquery',
		openhmis.url.cashierBase + 'js/model/item'
	],
	function($, openhmis) {
		var testBaseUrl = '/src/test/webapp/resources/js';
		curl(
			[
				'js!' + testBaseUrl + '/model/item.js',
				'js!' + testBaseUrl + '/view/editors.js',
				'js!' + testBaseUrl + '/view/bill.js',
				'js!' + testBaseUrl + '/view/item.js',
				'js!' + testBaseUrl + '/model/payment.js',
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