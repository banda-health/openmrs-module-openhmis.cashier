$(function() {
window.openhmis.PatientView = Backbone.View.extend({
	tmplFile: 'patient.html',
	tmplSelector: '#patient-details-template',
	
	initialize: function() {
		_.bindAll(this);
		this.template = this.getTemplate();
	},
	
	setElement: function(el) {
		Backbone.View.prototype.setElement.call(this, el);
		this.detailsEl = this.$('#patient-details');
		this.findEl = this.$('#find-patient');
	},
	
	events: {
		'click .editBillPatient': 'editPatient'
	},
	
	takeRawPatient: function(index, data) {
		this.model = new openhmis.Patient(data);
		this.render();
	},
	
	editPatient: function() {
		this.detailsEl.hide();
		this.findEl.show();
		this.$('#inputNode').focus();
	},
		
	render: function() {
		if (this.model === undefined) {
			this.findEl.show();
			this.detailsEl.hide();
		}
		else {
			this.findEl.hide();
			this.detailsEl.html(this.template({ patient: this.model }));
			this.detailsEl.show();
		}
		return this;
	}
});

});
