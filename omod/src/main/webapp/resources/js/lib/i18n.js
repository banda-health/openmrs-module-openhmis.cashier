define("lib/i18n",
	['lib/sprintf'],
	function() {
		var __ = function(	format, argv) {
			argv = argv === undefined ? [] : Array(argv);
			argv.unshift(format);
			return sprintf.apply(null, argv);
		}
		return __;
	}
);