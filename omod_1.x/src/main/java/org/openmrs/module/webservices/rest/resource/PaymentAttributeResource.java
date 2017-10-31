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
package org.openmrs.module.webservices.rest.resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.Patient;
import org.openmrs.Encounter;
import org.openmrs.Program;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.PatientService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * REST resource representing a {@link PaymentAttribute}.
 */
@Resource(name = RestConstants.VERSION_2 + "/cashier/paymentAttribute", supportedClass = PaymentAttribute.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class PaymentAttributeResource extends BaseRestAttributeDataResource<PaymentAttribute, PaymentModeAttributeType> {
	private static final Log LOG = LogFactory.getLog(PaymentAttributeResource.class);

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);

		if (!(rep instanceof RefRepresentation)) {
			description.addProperty("valueName", findMethod("getValueName"));
		}

		return description;
	}

	@PropertyGetter("value")
	public Object getValue(PaymentAttribute attribute) {
		return super.baseGetPropertyValue(attribute);
	}

	@PropertySetter("attributeType")
	public void setAttributeType(PaymentAttribute instance, PaymentModeAttributeType attributeType) {
		super.baseSetAttributeType(instance, attributeType);
	}

	@Override
	public String getDisplayString(PaymentAttribute instance) {
		String instanceFormat = instance.getAttributeType().getFormat();
		String names = null;
		if (StringUtils.isNotEmpty(instanceFormat)) {
			if (NumberUtils.isNumber(instance.getValue())) {
				int instanceId = NumberUtils.toInt(instance.getValue(), -1);
				if (instanceId > -1) {
					if (instanceFormat.contains("User")) {
						UserService userService = Context.getUserService();
						User user = userService.getUser(instanceId);
						if (user != null) {
							names = (user.getDisplayString());
						} else {
							LOG.error("The user could not be found");
						}
					} else if (instanceFormat.contains("Location")) {
						LocationService locationService = Context.getLocationService();
						Location location = locationService.getLocation(instanceId);
						if (location != null) {
							names = location.getDisplayString();
						} else {
							LOG.error("The location could not be found");
						}
					} else if (instanceFormat.contains("Provider")) {
						ProviderService providerService = Context.getProviderService();
						Provider provider = providerService.getProvider(instanceId);
						if (provider != null) {
							names = provider.getName();
						} else {
							LOG.error("The Provider could not be found");
						}
					} else if (instanceFormat.contains("Concept")) {
						ConceptService conceptService = Context.getConceptService();
						Concept concept = conceptService.getConcept(instanceId);
						if (concept != null) {
							names = concept.getDisplayString();
						} else {
							LOG.error("The Concept could not be found");
						}
					} else if (instanceFormat.contains("Patient")) {
						PatientService patientService = Context.getPatientService();
						Patient patient = patientService.getPatient(instanceId);
						if (patient != null) {
							names = patient.getPersonName().getFullName();
						} else {
							LOG.error("The Patient could not be found");
						}
					} else if (instanceFormat.contains("Encounter")) {
						EncounterService encounterService = Context.getEncounterService();
						Encounter encounter = encounterService.getEncounter(instanceId);
						if (encounter != null) {
							names = encounter.toString();
						} else {
							LOG.error("The Encounter could not be found");
						}
					} else if (instanceFormat.contains("ProgramWorkflow")) {
						ProgramWorkflowService programWorkflowService = Context.getProgramWorkflowService();
						Program program = programWorkflowService.getProgram(instanceId);
						if (program != null) {
							names = program.getName();
						} else {
							LOG.error("The Program could not be found");
						}
					} else {
						names = instance.getValue();
					}
				} else {
					LOG.error("The instance cannot be null or empty");
				}
			} else {
				names = instance.getValue();
			}
		} else {
			LOG.error("The Instance Format should not be empty");
		}

		return instance.getAttributeType().getName() + ": " + names;
	}

	public String getValueName(PaymentAttribute instance) {
		if (instance.getAttributeType().getFormat().contains("Concept")) {
			ConceptService service = Context.getService(ConceptService.class);
			Concept concept = service.getConcept(instance.getValue());

			return concept == null ? "" : concept.getDisplayString();
		} else {
			return instance.getValue();
		}
	}

	@Override
	public PaymentAttribute newDelegate() {
		return new PaymentAttribute();
	}

	@Override
	public Class<? extends IEntityDataService<PaymentAttribute>> getServiceClass() {
		return null;
	}
}
