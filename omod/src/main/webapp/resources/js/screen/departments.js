curl(
	{ baseUrl: curl.getRootUrl() },
	[
		'lib/jquery',
		'openhmis',
		'lib/backbone-forms',
		'model/department',
		'view/generic'
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Department, "/department", {
				listFields: ['name', 'description']
			});
		});
	}
);