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
				if (!this.get("payments"))
					this.set("payments", []);
				payment.meta.parentRestUrl = this.url() + '/';
				this.get("payments").push(payment);
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