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
		'view/item',
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Item, {
				addEditViewType: openhmis.ItemAddEditView,
				listFields: ['name', 'department', 'codes']
			});
		});
	}
);