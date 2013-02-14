curl(
    { baseUrl: openhmis.url.resources },
    [
        openhmis.url.backboneBase + 'js/lib/jquery',
        openhmis.url.backboneBase + 'js/openhmis',
        openhmis.url.backboneBase + 'js/lib/backbone-forms',
        openhmis.url.cashierBase + 'js/model/cashPoint',
        openhmis.url.backboneBase + 'js/view/generic'
    ],
    function($, openhmis) {
        $(function() {
            openhmis.startAddEditScreen(openhmis.CashPoint, {
                listFields: ['name', 'description']
            });
        });
    }
);