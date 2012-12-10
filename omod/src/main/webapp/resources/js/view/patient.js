define(
	[
		'lib/backbone',
		'lib/underscore',
		'lib/i18n',
		'model/patient',
		'model/visit'
	],
	function(Backbone, _, __, openhmis) {
		openhmis.PatientView = Backbone.View.extend({
			tmplFile: 'patient.html',
			tmplSelector: '#patient-details-template',
			
			initialize: function(options) {
				_.bindAll(this);
				this.template = this.getTemplate();
				this.readOnly = (options && options.readOnly) ? options.readOnly : false;
				this.visit = undefined;
			},
			
			setElement: function(el) {
				Backbone.View.prototype.setElement.call(this, el);
				this.detailsEl = this.$('#patient-details');
				this.findEl = this.$('#find-patient');
			},
			
			events: {
				'click .editBillPatient': 'editPatient',
				'click #endVisit': 'endVisit'
			},
			
			takeRawPatient: function(index, data) {
				data.identifiers = [{ display: data.identifier }];
				data.person = { display: data.personName };
				var model = new openhmis.Patient(data);
				this.selectPatient(model);
			},
			
			selectPatient: function(patient) {
				patient = patient ? patient : this.model;
				this.model = patient;
				this.visit = undefined;
				this.getVisit();
				this.render();
				this.trigger('selected', this.model);
			},
			
			getVisit: function() {
				var self = this;
				var success = function(collection, resp) {
					if (collection.length > 0) {
						self.visit = collection.models[0];
						self.render();
					}
				}
				if (this.model.get("uuid")) {
					openhmis.Visit.prototype.fetchVisitsByPatient({
						patient: this.model,
						active: true,
						success: success
					});
				}
			},
			
			endVisit: function() {
				if (confirm(__("Are you sure you want to end this patient's visit now?"))) {
					var $endVisit = this.$("#endVisit");
					var $spinner = this.$(".spinner");
					var self = this;
					$endVisit.attr("disabled", true);
					$spinner.css("visibility", "visible");
					this.visit.end(new Date(), {
						success: function(model, resp) {
							self.$(".visit").remove();
							$removeMsg = self.$(".removeMsg");
							$removeMsg.show(0, function() {
								$(this).fadeOut(2000);
							})
						}
					});
				}
			},
			
			editPatient: function() {
				this.detailsEl.hide();
				this.findEl.show();
				this.$('#inputNode').focus();
				this.trigger('editing');
			},
				
			render: function() {
				if (this.$findPatient === undefined)
					this.$findPatient = this.$('#findPatients');
				if (this.$findPatient.find('.cancel').length === 0 && this.model) {
					var cancelBtn = $('<input type="button" class="cancel" value="Cancel" />');
					this.$findPatient.append(cancelBtn);
					var self = this;
					cancelBtn.click(function() { self.selectPatient(); });
				}
				if (this.model === undefined) {
					this.findEl.show();
					this.detailsEl.hide();
				}
				else {
					this.findEl.hide();
					this.renderPatientDetails();
					this.detailsEl.show();
				}
				return this;
			},
			
			renderPatientDetails: function() {
				this.detailsEl.html(this.template({
					patient: this.model,
					visit: this.visit,
					dateFormat: openhmis.dateFormat,
					dashboardUri: openhmis.config.pageUrlRoot + "patientDashboard.form?patientUuid=" + encodeURIComponent(this.model.id),
					readOnly: this.readOnly
				}));
			}
		});
		
		return openhmis;
	}
);
