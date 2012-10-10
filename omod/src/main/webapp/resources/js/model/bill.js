define(
	[
		'model/generic',
		'model/payment'
	],
	function(openhmis) {
		openhmis.Bill = openhmis.GenericModel.extend({
			meta: {
				name: "Bill",
				namePlural: "Bills",
				openmrsType: 'data',
				restUrl: "bill"
			},
			
			schema: {
				lineItems: { type: 'Object'},
				patient: { type: 'Object' },
				payments: { type: 'Object'}
			},
			
			addPayment: function(payment) {
				payment.meta.parentRestUrl = this.url() + '/';
				var payments = this.get("payments");
				if (!payments)
					this.set("payments", new openhmis.GenericCollection(payment, { model: openhmis.Payment }));
				else
					payments.add(payment);
			},
			
			validate: function(final) {
				// By default, backbone validates every time we try try to alter
				// the model.  We don't want to be bothered with this until we
				// care.
				if (final !== true) return null;
				
				if (this.get("patient") === undefined)
					return { patient: "A bill needs to be associated with a patient." }
				if (this.get("lineItems") === undefined || this.get("lineItems").length === 0)
					return { lineItems: "A bill should contain at least one item." }
				return null;
			},
			
			toJSON: function() {
				var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
				if (attrs.lineItems) {
					for (var item in attrs.lineItems)
						attrs.lineItems[item] = attrs.lineItems[item].toJSON();
				}
				if (attrs.patient) attrs.patient = attrs.patient.id;
				return attrs;
			},
			
			parse: function(resp) {
				if (resp === null) return resp;
				if (resp.lineItems) {
					resp.lineItems = new openhmis.GenericCollection(resp.lineItems, {
						model: openhmis.LineItem,
						parse: true
					});
				}
				if (resp.payments) {
					var urlRoot = this.url() + '/payment/';
					var paymentCollection = new openhmis.GenericCollection([], { model: openhmis.Payment });
					paymentCollection.add(resp.payments, { parse: true, urlRoot: urlRoot });
					//paymentCollection.reset(paymentCollection.reject(function(payment) { return payment.get("voided"); }));
					resp.payments = paymentCollection;
				}
				return resp;
			}
		});
	}
);