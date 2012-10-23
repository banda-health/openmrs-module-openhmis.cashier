define("openhmis",
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'lib/i18n',
		'lib/backbone-forms'
	],
	function($, _, Backbone, __) {

		// Use <? ?> for template tags.
		_.templateSettings = {
			evaluate:  /<\?(.+?)\?>/g,
			interpolate: /<\?=(.+?)\?>/g
		};
		
		var openhmis = window.openhmis || {};
		openhmis.templates = {};
				
		openhmis.error = function(model, resp) {
			if (!(model instanceof Backbone.Model)) {
				var o = $.parseJSON(model.responseText).error;
				if (o.detail.indexOf("ContextAuthenticationException") !== -1) {
					alert(__("Your session has timed out.  You will be redirected to the login page."));
					window.location.reload();
				}
				else {
					console.log("Message: " + o.message + "\n" + "Code: " + o.code + "\n" + "Detail: " + o.detail);
					var firstLfPos = o.detail.indexOf('\n');
					if (firstLfPos !== -1)
						o.detail = o.detail.substring(0, firstLfPos);
					alert('An error occurred during the request.\n\n' + o.message + '\n\nCode: ' + o.code + '\n\n' + o.detail);
				}
			}
			else if (resp !== undefined) {
				var str = "";
				for (var i in resp) {
					if (str.length > 0) str += ",\n";
					str += i + ": " + resp[i];
				}
				alert(str);
			}
		}
		
		openhmis.getQueryStringParameter = function(name)
		{
			name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
			var regexS = "[\\?&]" + name + "=([^&#]*)";
			var regex = new RegExp(regexS);
			var results = regex.exec(window.location.search);
			if(results == null)
				  return "";
			else
				return decodeURIComponent(results[1].replace(/\+/g, " "));
		},
		
		openhmis.dateFormat = function(date) {
			var day = date.getDate();
			var month = date.getMonth();
			var year = date.getFullYear();
			day = day < 10 ? "0" + day : day.toString();
			month = month < 10 ? "0" + month: month.toString();
			return day + '-' + month + '-' + year;
		},
		
		openhmis.validationMessage = function(parentEl, message, inputEl) {
			if ($(parentEl).length > 1) parentEl = $(parentEl)[0];
			if ($(parentEl).find('.validation').length > 0) return;
			var prevPosition = $(parentEl).css("position");
			$(parentEl).css("position", "relative");
			var el = $('<div class="validation"></div>');
			el.text(message);
			$(parentEl).append(el);
			if (inputEl !== undefined) $(inputEl).focus();
			setTimeout(function() {
				$(el).remove();
				$(parentEl).css("position", prevPosition);
			}, 5000);
		},
		
		// Use uuid for id
		Backbone.Model.prototype.idAttribute = 'uuid';
		
		/**
		 * Template helper function
		 *
		 * Fetches a template from a remote URI unless it has been previously fetched
		 * and cached.
		 */
		Backbone.View.prototype.tmplFileRoot = openhmis.config.wwwUrlRoot + 'template/';
		Backbone.View.prototype.getTemplate = function(tmplFile, tmplSelector) {
			tmplFile = tmplFile ? tmplFile : this.tmplFile;
			tmplSelector = tmplSelector ? tmplSelector : this.tmplSelector;
			var view = this;
			if (openhmis.templates[tmplFile] === undefined) {
				var uri = view.tmplFileRoot === undefined ? tmplFile : view.tmplFileRoot + tmplFile;
				$.ajax({
					url: uri,
					async: false,
					dataType: "html",
					success: function(data, status, jq) {
						openhmis.templates[tmplFile] = $("<div/>").html(data);
					}
				});
			}
			var template = _.template($(openhmis.templates[tmplFile]).find(tmplSelector).html());
			var augmentedTemplate = function(context) {
				if (context !== undefined) {
					context.__ = context.__ ? context.__ : __;
					context.helpers = context.helpers ? context.helpers : Backbone.Form.helpers
				}
				return template(context);
			}
			return augmentedTemplate;
		}
		
		return openhmis;
	}
)
