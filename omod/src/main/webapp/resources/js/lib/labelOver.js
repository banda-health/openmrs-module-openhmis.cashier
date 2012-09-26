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
					  label.css({ visibility: "hidden" })
					}
					
					this.show = function() {
					  if (input.val() == '') label.css({ visibility: "visible" })
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