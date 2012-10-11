define(
	[
		'lib/underscore',
		'lib/backbone',
		'openhmis'
	],
	function(_, Backbone, openhmis) {
		openhmis.GenericModel = Backbone.Model.extend({
			initialize: function(attributes, options) {
				_.bindAll(this, 'setUnsaved');
				if (options !== undefined) {
					this.urlRoot = options.urlRoot;
				}
				this.unsaved = this.isNew();
				var self = this;
				if (this.trackUnsaved === true)
					this.on("change", this.setUnsaved);
			},
			
			setUnsaved: function() {
				this.unsaved = true;
			},
			
			isUnsaved: function() {
				return this.unsaved;
			},
			
			save: function(key, value, options) {
				if (value && value.success) {
					var self = this;
					var success = value.success;
					value.success = function(model, resp) {
						self.unsaved = false;
						if (success) success(model, resp);
					}
					return Backbone.Model.prototype.save.call(this, key, value);
				}
				return Backbone.Model.prototype.save.call(this, key, value, options);
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
				if (options && options.baseUrl)
					this.baseUrl = options.baseUrl
				else if (this.model) {
					if (this.model.prototype.urlRoot !== undefined)
						this.url = this.model.prototype.urlRoot;
					else if (this.model.prototype.meta && this.model.prototype.meta.restUrl)
						this.url = this.baseUrl + this.model.prototype.meta.restUrl;
				}
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
				return response.results;
			}
		});
		
		return openhmis;
	}
);