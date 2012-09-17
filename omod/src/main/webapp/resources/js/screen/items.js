curl(
	{ baseUrl: '/openmrs/moduleResources/openhmis/cashier/js' },
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
			openhmis.startAddEditScreen(openhmis.Item, "/item", {
				listFields: ['name', 'codes']
			});
		});
	}
);