curl(
	{ baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
	[
		'lib/jquery',
		'openhmis',
		'lib/backbone-forms',
		'model/department',
		'view/generic'
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Department, {
				listFields: ['name', 'description']
			});
		});
	}
);