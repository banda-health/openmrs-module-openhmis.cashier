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
		'view/search',
		'view/item'
	],
	function($, openhmis) {
		$(function() {
			openhmis.startAddEditScreen(openhmis.Item, {
				listView: openhmis.GenericSearchableListView,
				searchView: openhmis.NameSearchView,
				addEditViewType: openhmis.ItemAddEditView,
				listFields: ['name', 'department', 'codes']
			});
		});
	}
);