curl(
    { baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
    [
        'lib/jquery',
        'openhmis',
        'lib/backbone-forms',
        'model/paymentMode',
        'view/generic',
        'view/list',
        'view/editors'
    ],
    function($, openhmis) {
        $(function() {
            openhmis.startAddEditScreen(openhmis.PaymentMode, {
                listFields: ['name', 'description']
            });
        });
    }
);