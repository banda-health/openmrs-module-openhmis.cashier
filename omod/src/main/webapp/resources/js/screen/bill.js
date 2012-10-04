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
		var patientView = new openhmis.PatientView();
		openhmis.doSelectionHandler = patientView.takeRawPatient;
		$(document).ready(function() {
			// Patient View
			patientView.setElement($('#patient-view'));
			patientView.render();
		
			// Bill View
			var billView = new openhmis.BillView({ model: new openhmis.GenericCollection([],
				{
					model: openhmis.LineItem,
				})
			});
			billView.setElement($('#bill'));
			billView.render();
			$('#saveBill').click(billView.saveBill);
			billView.setupNewItem();
			
			patientView.on('selected', billView.patientSelected);
			patientView.on('editing', billView.blur);
			
			// Payment View
			var paymentView = new openhmis.PaymentView({
				paymentCallback: billView.processPayment
			});
			paymentView.setElement($('#payment'));
			paymentView.render();

			$('#inputNode').focus();
		});
	}
);
