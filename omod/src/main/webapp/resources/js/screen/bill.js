/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
curl(
	{ baseUrl: openhmis.url.resources },
	[
		openhmis.url.backboneBase + 'js/view/patient',
		openhmis.url.backboneBase + 'js/lib/i18n',
		openhmis.url.cashierBase + 'js/view/bill',
		openhmis.url.cashierBase + 'js/view/payment',
		openhmis.url.cashierBase + 'js/model/lineItem'
	],
	function(openhmis, __) {
		var Screen = function() {
			this.billUuid = openhmis.getQueryStringParameter("billUuid");
			this.patientUuid = openhmis.getQueryStringParameter("patientUuid");
			
			this.patientView = new openhmis.PatientView();
			// Set up patient search selection handler
			openhmis.doSelectionHandler = this.patientView.takeRawPatient;
			
			var options = new openhmis.GenericModel([], {
				urlRoot: openhmis.url.page + openhmis.url.cashierBase + "options.json"
			});
		
			var self = this;
			options.fetch({ success: function(options, resp) {
				if (resp.exception) {
					openhmis.error({
						message: resp.exception.message,
						code: resp.exception.cause,
						detail: resp.exception.stackTrace
					});
				} else {
					self.createBillView.call(self, options, resp);
				}
			}});
		}

		/**
		 * createBillView
		 *
		 * Create the bill view, using specified options, then fetch data needed
		 * to populate the view.
		 *
		 * @param {Model} options A populated options model
		 */
		Screen.prototype.createBillView = function(options) {
			this.billView = new openhmis.BillView({
				roundToNearest: options.get("roundToNearest"),
				roundingMode: options.get("roundingMode")
			});
			var self = this;
			// If a bill is specified
			if (this.billUuid) {
				// Load the bill
				var bill = new openhmis.Bill({ uuid: this.billUuid });
				bill.fetch({
					silent: true,
					success: function(bill, resp) {
						self.setupBillViewWithBill.call(self, bill, resp);
					}
				});
			}
			// If a patient is specified
			else if (this.patientUuid) {
				var patient = new openhmis.Patient({ uuid: this.patientUuid });
				patient.fetch({
					silent: true,
					success: function(patient, resp) {
						self.setupBillViewWithPatient.call(self, patient, resp);
					}
				});
			} else {
				this.displayBillView.call(this);
			}
		};
		
		/*
		 * setupBillViewWithBill
		 *
		 * For setting up the BillView with an existing bill
		 *
		 * @param {Bill} bill An initialized openhmis.Bill model
		 */
		Screen.prototype.setupBillViewWithBill = function(bill) {
			this.billView.setBill(bill);
			this.patientView.model = new openhmis.Patient(bill.get("patient"));
			if (bill.get("billAdjusted")) {
				var self = this;
				bill.get("billAdjusted").fetch({
					success: function(billAdjusted, resp) {
						self.displayBillView();
					}
				});
			} else {
				this.displayBillView();
			}
			this.patientView.selectPatient(this.patientView.model, {silent:true});			
		}
		
		/*
		 * setupBillViewWithPatient
		 *
		 * For setting up the BillView to be associated with a patient
		 *
		 * @param {Patient} patient An initialized openhmis.Patient model
		 */
		Screen.prototype.setupBillViewWithPatient = function(patient) {
			this.billView.bill.set("patient", patient);
			this.patientView.model = patient;
			this.displayBillView();
		}

		/*
		 * displayBillView
		 *
		 * Take care of putting together and rendering all the elements of the
		 * BillView.  Relies on initialized billView and patientView
		 */
		Screen.prototype.displayBillView = function() {
			var self = this;
			// Easy access to status enumeration
			var BillStatus = this.billView.bill.BillStatus;
			
			// Automatic receipt printing
			if (openhmis.getQueryStringParameter("print") === "true") {
				this.billView.printReceipt();
				$("#printReceipt").attr("disabled", "disabled");
			}
			
			// Patient View
			if (this.billView.bill.get("status") !== BillStatus.PENDING) {
				this.patientView.readOnly = true;
			}
			this.patientView.setElement($('#patient-view'));
			this.patientView.render();
			
			this.billView.on("save paid adjusted", function(bill) {
				window.location = openhmis.url.getPage("cashierBase") + 'bill.form?billUuid=' + bill.id;
			});
			this.billView.on("saveAndPrint", function(bill) {
				var url = openhmis.url.getPage("cashierBase") + 'bill.form?billUuid=' + bill.id;
				url = openhmis.addQueryStringParameter(url, "print=true");
				window.location = url;
			});

			this.billView.setElement($('#bill'));
			$saveButton = $('#saveBill');
			$postButton = $('#postBill');
			$printButton = $("#printReceipt");
			switch (this.billView.bill.get("status")) {
				case BillStatus.PENDING:
					$saveButton.val(__(openhmis.getMessage('openhmis.cashier.bill.saveBill')));

					var inst = this;
					$saveButton.click(function() {
						inst.billView.saveBill();
					});


					var confirmMsg = __(openhmis.getMessage('openhmis.cashier.bill.postMessage'));
					var confirmPostPrintMsg = __(openhmis.getMessage('openhmis.cashier.bill.postAndPrintMessage'));
					$postButton.click(function() { if (confirm(confirmMsg)) { self.billView.postBill() }});
					$postButton.show();
					$printButton.val(__(openhmis.getMessage('openhmis.cashier.bill.postAndPrint')));
					$printButton.click(function() { if (confirm(confirmPostPrintMsg)) { self.billView.postBill({ print: true }) }});
					$printButton.show();
					
					if (this.billView.bill.get("billAdjusted")) {
						adjustedBillView = new openhmis.BillAndPaymentsView({
							model: this.billView.bill.get("billAdjusted")
						});
						$("#patient-view").after(adjustedBillView.el).addClass("combineBoxes");
						adjustedBillView.render();
						$("#bill").appendTo(adjustedBillView.$el);
						this.billView.options.listTitle = __("Adjustments");
					}
					
					// Provide cash point select, if this option is enabled
					var $cashPointLi = $("li.cashPoint");
					if (!$cashPointLi.hasClass("timesheet") && !this.billView.bill.get("billAdjusted")) {
						this.billView.setupCashPointForm($("li.cashPoint"));
					}
					break;
				case BillStatus.POSTED:
				case BillStatus.PAID:
					var $allowBillAdjustment = $('#allowBillAdjustment');
					if ($allowBillAdjustment.val() == 'true'){
						$saveButton.val(__(openhmis.getMessage('openhmis.cashier.bill.adjustBill')));
						$saveButton.click(this.billView.handleAdjustBill);
					} else {
						$saveButton.hide();
					}
					$printButton.val(__(openhmis.getMessage('openhmis.cashier.bill.printReceipt')));
					$printButton.click(function (event) {
						self.billView.printReceipt(event);
						$(this).attr("disabled", "disabled");
					});
					$printButton.show();
					break;
				case BillStatus.ADJUSTED:
					$saveButton.remove();
					break;
			}

			this.billView.render();
			
			if (this.billView.bill.get("status") === BillStatus.PENDING) {
				this.billView.setupNewItem();
			}
			
			this.patientView.on('selected', this.billView.patientSelected);
			this.patientView.on('editing', this.billView.blur);
			
			// Payment View
			var readOnly = !(this.billView.bill.get("status") == BillStatus.PENDING
							 || this.billView.bill.get("status") == BillStatus.POSTED);
			var paymentView = new openhmis.PaymentView({
				paymentCollection: this.billView.bill.get("payments"),
				processCallback: this.billView.processPayment,
				readOnly: readOnly
			});
			// Disable add event when the bill is saving to prevent
			// unsettling page drawing
			this.billView.on("save", function() { paymentView.model.off("add"); });
			paymentView.paymentCollection.on("remove", this.billView.updateTotals);
			paymentView.setElement($('#payment'));
			paymentView.render();
			this.billView.updateTotals();
			this.billView.on("focusNext", paymentView.focus);
			
			window.onbeforeunload = function() {
				if (self.billView.bill.isUnsaved()) {
					return __(openhmis.getMessage('openhmis.cashier.bill.unsavedChanges'));
				}
				return null;
			}
			
			if (this.billView.bill.get("patient")) {
				this.billView.focus();
			} else {
				$('#inputNode').focus();
			}
		}
		
		$(document).ready(function() {
			if ($("#bill").length > 0) {
				var screen = new Screen();
			}
		});
		
		return Screen;
	}
);
