define(
    [
        'lib/jquery',
        'lib/underscore',
        'openhmis'
    ], function($, _, openhmis) {
        $.ajax({
            url: openhmis.baseUrl + '/lib/backbone-src.js',
            dataType: 'script',
            async: false
        });
        return Backbone;
    }
);