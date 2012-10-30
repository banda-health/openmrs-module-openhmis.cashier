define("lib/i18n",
	['lib/sprintf'],
	function() {
		var __ = function() {
			return sprintf.apply(null, arguments);
		}
		return __;
	}
);