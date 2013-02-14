define(
	[
		openhmis.url.backboneBase + 'js/model/generic',
		openhmis.url.cashierBase + 'js/model/cashPoint',
		openhmis.url.backboneBase + 'js/model/patient',
		openhmis.url.cashierBase + 'js/model/payment',
		openhmis.url.cashierBase + 'js/model/lineItem'
	],
	function(openhmis) {
		openhmis.Bill = openhmis.GenericModel.extend({
			trackUnsaved: true,
			meta: {
				name: "Bill",
				namePlural: "Bills",
				openmrsType: 'data',
				restUrl: "bill"
			},
			
			schema: {
				billAdjusted: { type: 'Object', objRef: true },
				cashPoint: { type: 'Object', objRef: true },
				lineItems: { type: 'Object'},
				patient: { type: 'Object', objRef: true },
				payments: { type: 'Object'},
				status: { type: 'Text' }
			},
						
			BillStatus: {
				PENDING:	"PENDING",
				POSTED:		"POSTED",
				PAID:		"PAID",
				ADJUSTED:	"ADJUSTED"
			},
			
			initialize: function(attrs, options) {
				openhmis.GenericModel.prototype.initialize.call(this, attrs, options);
				if (!this.get("lineItems")) this.set("lineItems",
					new openhmis.GenericCollection([], { model: openhmis.LineItem }), { silent: true });
				if (!this.get("payments")) this.set("payments",
					new openhmis.GenericCollection([], { model: openhmis.Payment }), { silent: true });
				if (!this.get("status")) this.set("status",
					this.BillStatus.PENDING, { silent: true });
			},
			
			addPayment: function(payment) {
				payment.meta.parentRestUrl = this.url() + '/';
				this.get("payments").add(payment);
			},
			
			getTotal: function() {
				var total = 0;
				var lineItems = this.get("lineItems");
				if (lineItems && lineItems.length > 0) {
					lineItems.each(function(item) {
						if (item.isClean()) total += item.getTotal();
					});
				}
				return total;
			},
			
			/**
			 * This method takes the adjustment process into account when
			 * calculating the bill total
			 */
			getAdjustedTotal: function() {
				var billAdjusted = this.get("billAdjusted");
				if (billAdjusted !== undefined && this.get("status") == this.BillStatus.PENDING) {
					return this.getTotal() + billAdjusted.getTotal() - billAdjusted.getAmountPaid();
				}
				else
					return this.getTotal();
			},
			
			getTotalPayments: function() {
				var total = 0;
				var payments = this.get("payments");
				if (payments && payments.length > 0) {
					payments.each(function(payment) {
						if (payment.get("voided") !== true)
							total += payment.get("amountTendered");
					});
				}
				return total;
			},
			
			getAmountPaid: function() {
				var total = this.getTotal();
				var totalPayments = this.getTotalPayments();
				
				return Math.min(total, totalPayments);
			},
			
			validate: function(goAhead) {
				// By default, backbone validates every time we try try to alter
				// the model.  We don't want to be bothered with this until we
				// care.
                if (goAhead !== true) return null;
				
				if (this.get("patient") === undefined)
					return { patient: "A bill needs to be associated with a patient." }
				if (this.get("lineItems") === undefined || this.get("lineItems").length === 0)
					return { lineItems: "A bill should contain at least one item." }
				return null;
			},
			
			toJSON: function() {
				var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
				if (attrs.lineItems) {
					attrs.lineItems = attrs.lineItems.toJSON();
					for (var i in attrs.lineItems)
						attrs.lineItems[i].lineItemOrder = i;
				}
				if (attrs.payments) attrs.payments = attrs.payments.toJSON();
				return attrs;
			},
			
			parse: function(resp) {
				if (resp === null) return resp;
				if (resp.patient) resp.patient = new openhmis.Patient(resp.patient);
				if (resp.adjustedBy) resp.adjustedBy = new openhmis.GenericCollection(resp.adjustedBy, { model: openhmis.Bill });
				
				if (resp.billAdjusted) resp.billAdjusted = new openhmis.Bill(resp.billAdjusted);
				else delete resp.billAdjusted;
				
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
				if (resp.cashPoint) resp.cashPoint = new openhmis.CashPoint(resp.cashPoint);
				return resp;
			},
			
			toString: function() {
				var str = this.get("receiptNumber");
				return str ? str : openhmis.GenericModel.prototype.toString.call(this);
			}
		});
	}
);