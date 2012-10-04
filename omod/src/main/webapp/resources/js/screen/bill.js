curl(
	{ baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
	[
		'lib/jquery',
		'view/patient',
		'view/bill',
		'view/payment',
		'model/lineItem'
	],
	function($, openhmis) {
		var billUuid = openhmis.getQueryStringParameter("billUuid");
		var patientUuid = openhmis.getQueryStringParameter("patientUuid");
		
		var patientView = new openhmis.PatientView();
		// Set up patient search selection handler
		openhmis.doSelectionHandler = patientView.takeRawPatient;
		
		var billView = new openhmis.BillView({
			model: new openhmis.GenericCollection([], { model: openhmis.LineItem })
		});

		// Callback in case we need to load a bill or patient first		
		var displayBillView = function(billView, patientView) {
			$(document).ready(function() {
				// Patient View
				patientView.setElement($('#patient-view'));
				patientView.render();
				
				billView.setElement($('#bill'));
				billView.render();
				$('#saveBill').click(billView.saveBill);
				billView.setupNewItem();
				
				patientView.on('selected', billView.patientSelected);
				patientView.on('editing', billView.blur);
				
				// Payment View
				var paymentView = new openhmis.PaymentView({
					processCallback: billView.processPayment
				});
				paymentView.setElement($('#payment'));
				paymentView.render();
				
				$('#inputNode').focus();
			});			
		}
		
		// If a bill is specified
		if (billUuid) {
			// Load the bill
			var bill = new openhmis.Bill({ uuid: billUuid });
			bill.fetch({ success: function(bill, resp) {
				billView.bill = bill;
				billView.model.add(bill.get("lineItems"));
				patientView.model = new openhmis.Patient(bill.get("patient"));
				displayBillView(billView, patientView);
			}});
		}
		// If a patient is specified
		else if (patientUuid) {
			
		}
		else {
			displayBillView(billView, patientView);
		}
		
		
	}
);
