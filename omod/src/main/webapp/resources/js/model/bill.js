define(
	[
		'model/generic'
	],
	function(openhmis) {
		openhmis.Bill = openhmis.GenericModel.extend({
			meta: {
				name: "Bill",
				namePlural: "Bills",
				openmrsType: 'data',
				restUrl: "bill"
			},
			
			addPayment: function(payment) {
				if (!this.get("payments"))
					this.set("payments", []);
				this.get("payments").push(payment);
			},
			
			toJSON: function() {
				var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
				if (attrs.lineItems) {
					for (var item in attrs.lineItems)
						attrs.lineItems[item] = attrs.lineItems[item].toJSON();
				}
				if (attrs.patient) attrs.patient = attrs.patient.id;
				attrs.receiptNumber = "bogus";
				return attrs;
			}
		});
	}
);