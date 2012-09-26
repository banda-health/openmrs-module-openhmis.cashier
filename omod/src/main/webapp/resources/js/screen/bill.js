curl(
	{ baseUrl: openhmis.config.wwwUrlRoot + 'js/' },
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
		});
	}
);
