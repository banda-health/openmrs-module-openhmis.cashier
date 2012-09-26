define(
	[
		'lib/underscore',
		'lib/backbone',
		'openhmis'
	],
	function(_, Backbone, openhmis) {
		openhmis.GenericModel = Backbone.Model.extend({
			initialize: function(attributes, options) {
				if (options !== undefined) {
					this.urlRoot = options.urlRoot;
				}
				//_.bindAll(this, 'saveSubResource');
			},
			
		    url: function() {
				var url;
				try {
					url = Backbone.Model.prototype.url.call(this);
				} catch (e) {
					if (this.meta && this.meta.restUrl) {
						this.urlRoot = openhmis.config.restUrlRoot + this.meta.restUrl;
						url = Backbone.Model.prototype.url.call(this);
					}
					else
						throw e;
				}
				return url;
			},
			
			toJSON: function(options) {
				if (this.schema === undefined) return Backbone.Model.prototype.toJSON.call(this, options);
				var attributes = {};
				for (var attr in this.attributes) {
					// This gets added to representations but cannot be set
					if (attr === 'resourceVersion') continue;
					
					if (this.schema[attr] !== undefined
						&& (this.schema[attr].readOnly === undefined
							|| this.schema[attr].readOnly === false))
						attributes[attr] = this.attributes[attr];
				}
				return _.clone(attributes);
			},
			
			listToString: function(field) {
				var collection = new openhmis.GenericCollection(this.get(field), {
					model: this.schema[field].model
				});
				var str = "";
				collection.each(function(model) {
					str += str === "" ? model.toString() : ", " + model.toString();
				});
				return str;
			}
		});
		
		openhmis.GenericCollection = Backbone.Collection.extend({
			baseUrl: openhmis.config.restUrlRoot,
			
			initialize: function(models, options) {
				if (this.model && this.model.prototype.meta && this.model.prototype.meta.restUrl)
					this.url = this.baseUrl + this.model.prototype.meta.restUrl;
				else
					this.url = this.baseUrl + options.url;
			},
			
			fetch: function(options) {
				options = options ? options : {};
				var error = options.error;
				options.error = function(model, data) {
					openhmis.error(data);
					if (error !== undefined)
						error(model, data);
				}
				Backbone.Collection.prototype.fetch.call(this, options)
			},
			
			parse: function(response) {
				var results = response.results;
				for (var result in results) {
					if (this.model.prototype.parse !== undefined)
						results[result] = this.model.prototype.parse.call(this, results[result]);
				}
				return results;
			}
		});
		
		return openhmis;
	}
);