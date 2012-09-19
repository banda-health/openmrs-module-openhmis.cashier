curl(
	{ baseUrl: curl.getRootUrl() },
	[
		'lib/jquery',
		'view/patient',
		'view/bill',
		'model/lineItem',
		'view/lineItem'
	],
	function($, openhmis) {
		var patientView = new openhmis.PatientView();
		openhmis.doSelectionHandler = patientView.takeRawPatient;
		$(document).ready(function() {
			patientView.setElement($('#patient-view'));
			patientView.render();
		
			var billView = new openhmis.BillView({ model: new openhmis.GenericCollection(
				[
					new openhmis.LineItem({
						description: "Consultation Fee",
						quantity: 1,
						price: 50
					}),
					new openhmis.LineItem({
						description: "File Retrieval Fee",
						quantity: 1,
						price: 10
					})					
				],
				{
					model: openhmis.LineItem,
				})
			});
			billView.setElement($('#bill'));
			billView.render();
		});
	}
);
