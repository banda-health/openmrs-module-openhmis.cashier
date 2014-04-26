curl(
    { baseUrl: openhmis.url.resources },
    [
        openhmis.url.backboneBase + 'js/lib/jquery',
        openhmis.url.cashierBase + 'js/model/payment',
        openhmis.url.backboneBase + 'js/view/generic',
        openhmis.url.cashierBase + 'js/view/payment',
        openhmis.url.cashierBase + 'js/view/editors',

    ],
    function($, openhmis) {
        $(function() {
            openhmis.startAddEditScreen(openhmis.PaymentMode, {
                addEditViewType: openhmis.PaymentModeAddEditView,
                listFields: ['name', 'description', 'paymentModeRoles']
            });
        });
    }
);