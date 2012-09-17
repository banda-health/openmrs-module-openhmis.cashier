define(
    [
        'lib/jquery',
        'lib/backbone',
        'openhmis'
    ],
    function($, Backbone, openhmis) {
        $.ajax({
            url: openhmis.baseUrl + '/lib/backbone-forms-src.js',
            dataType: 'script',
            async: false
        });
    }
);