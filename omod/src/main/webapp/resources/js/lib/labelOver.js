define(
	[
		'lib/jquery'
	],
	function($) {
		$.fn.labelOver = function(overClass) {
			return this.each(function(){
				var label = $(this);
				var f = label.attr('for');
				if (f) {
					var input = label.siblings('#' + f);
					
					this.hide = function() {
					  label.css({ textIndent: -10000 })
					}
					
					this.show = function() {
					  if (input.val() == '') label.css({ textIndent: 0 })
					}
		
					// handlers
					input.keydown(this.hide);
					input.blur(this.show);
					label.addClass(overClass).click(function(){ input.focus() });
					
					if (input.val() != '') this.hide(); 
				}
			})
		}
	}
)