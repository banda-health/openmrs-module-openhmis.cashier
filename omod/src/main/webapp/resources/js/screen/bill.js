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
			billView.setupNewItem();
			$('#inputNode').focus();
			patientView.on('selected', billView.focus);
			patientView.on('editing', billView.blur);
			
			// Payment View
			var paymentView = new openhmis.PaymentView();
			paymentView.setElement($('#payment'));
			paymentView.render();
		});
	}
);
