curl(
	{ baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
	[
		'lib/jquery',
		'openhmis',
		'lib/backbone-forms',
		'model/item',
		'model/department',
		'view/generic',
		'view/list',
		'view/editors',
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Item, {
				listFields: ['name', 'department', 'codes']
			});
		});
	}
);