curl(
    { baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
    [
        'lib/jquery',
        'openhmis',
        'lib/backbone-forms',
        'model/cashPoint',
        'view/generic'
    ],
    function($, openhmis) {
        $(function() {
            openhmis.startAddEditScreen(openhmis.CashPoint, {
                listFields: ['name', 'description']
            });
        });
    }
);