curl(
	{ baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
	[
		'lib/jquery',
		'view/patient',
		'lib/i18n',
		'view/bill',
		'view/payment',
		'model/lineItem'
	],
	function($, openhmis, __) {
		var billUuid = openhmis.getQueryStringParameter("billUuid");
		var patientUuid = openhmis.getQueryStringParameter("patientUuid");
		
		var patientView = new openhmis.PatientView();
		// Set up patient search selection handler
		openhmis.doSelectionHandler = patientView.takeRawPatient;
		
		var billView;

		// Callback in case we need to load a bill or patient first		
		var displayBillView = function(billView, patientView) {
			$(document).ready(function() {
				// Easy access to status enumeration
				var BillStatus = billView.bill.BillStatus;

				// Patient View
				if (billView.bill.get("status") !== BillStatus.PENDING)
					patientView.readOnly = true;
				patientView.setElement($('#patient-view'));
				patientView.render();
				
				billView.on("save paid adjusted", function(bill) {
					window.location = openhmis.config.pageUrlRoot + 'bill.form?billUuid=' + bill.id;
				});
				billView.setElement($('#bill'));
				billView.render();
				
				$saveButton = $('#saveBill');
				switch (billView.bill.get("status")) {
					case BillStatus.PENDING:
						$saveButton.val(__("Save Bill"));
						$saveButton.click(function() { billView.saveBill() });
						break;
					case BillStatus.PAID:
						$saveButton.val(__("Adjust Bill"));
						$saveButton.click(function() { billView.adjustBill() });
						break;
					case BillStatus.ADJUSTED:
						$saveButton.remove();
						break;
				}
				
				if (billView.bill.get("status") === BillStatus.PENDING)
					billView.setupNewItem();
				
				patientView.on('selected', billView.patientSelected);
				patientView.on('editing', billView.blur);
				
				// Payment View
				var readOnly = billView.bill.get("status") !== BillStatus.PENDING
					|| billView.bill.get("billAdjusted");
				var paymentView = new openhmis.PaymentView({
					paymentCollection: billView.bill.get("payments"),
					processCallback: billView.processPayment,
					readOnly: readOnly
				});
				// Disable add event when the bill is saving to prevent
				// unsettling page drawing
				billView.on("save", function() { paymentView.model.off("add"); });
				paymentView.setElement($('#payment'));
				paymentView.render();
				
				window.onbeforeunload = function() {
					if (billView.bill.isUnsaved())
						return __("There are unsaved changes.");
					return null;
				}
				
				$('#inputNode').focus();
			});			
		}
		
		// If a bill is specified
		if (billUuid) {
			// Load the bill
			var bill = new openhmis.Bill({ uuid: billUuid });
			bill.fetch({ silent: true, success: function(bill, resp) {
				billView = new openhmis.BillView({ bill: bill });
				patientView.model = new openhmis.Patient(bill.get("patient"));
				displayBillView(billView, patientView);
			}});
		}
		// If a patient is specified
		else if (patientUuid) {
			
		}
		else {
			billView = new openhmis.BillView();
			displayBillView(billView, patientView);
		}
	}
);
