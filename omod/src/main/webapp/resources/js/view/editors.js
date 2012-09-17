define(
	[
		'lib/backbone',
		'lib/backbone-forms'
	],
	function(Backbone) {
		var editors = Backbone.Form.editors;
		editors.BasicNumber = editors.Number.extend({
			initialize: function(options) {
				editors.Text.prototype.initialize.call(this, options);
			},
			
				/**
			* Check value is numeric
			*/
			onKeyPress: function(event) {
				var self = this,
					delayedDetermineChange = function() {
					  setTimeout(function() {
						self.determineChange();
					  }, 0);
					}
					
				//Allow backspace && enter
				if (event.charCode == 0 || event.charCode == 13) {
				  delayedDetermineChange();
				  return;
				}
				
				//Get the whole new value so that we can prevent things like double decimals points etc.
				var newVal = this.$el.val() + String.fromCharCode(event.charCode);
		  
				var numeric = /^[0-9]*\.?[0-9]*?$/.test(newVal);
		  
				if (numeric) {
				  delayedDetermineChange();
				}
				else {
				  event.preventDefault();
				}
		   },
			
		});		
	}
)
