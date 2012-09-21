curl(
	{ baseUrl: curl.getRootUrl() },
	[
		'lib/jquery',
		'view/patient',
		'view/bill',
		'model/lineItem'
	],
	function($, openhmis) {
		var patientView = new openhmis.PatientView();
		openhmis.doSelectionHandler = patientView.takeRawPatient;
		$(document).ready(function() {
			patientView.setElement($('#patient-view'));
			patientView.render();
		
			var billView = new openhmis.BillView({ model: new openhmis.GenericCollection(
				[
					new openhmis.LineItem({ uuid: 'asd', item: new openhmis.Item({ uuid: 'asd' }) }),
					new openhmis.LineItem({ uuid: 'fgh', item: new openhmis.Item({ uuid: 'fgh' }) }),
					new openhmis.LineItem({ uuid: 'jkl', item: new openhmis.Item({ uuid: 'jkl' }) }),
					new openhmis.LineItem({ uuid: 'qwe', item: new openhmis.Item({ uuid: 'qwe' }) }),
					new openhmis.LineItem()
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
