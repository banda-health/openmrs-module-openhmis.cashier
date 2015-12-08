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
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * REST resource representing a {@link PaymentAttribute}.
 */
@Resource(name = RestConstants.VERSION_2 + "/cashier/paymentAttribute", supportedClass = PaymentAttribute.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
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

	@Override
	public String getDisplayString(PaymentAttribute instance) {
		String instanceFormat = instance.getAttributeType().getFormat();
		String names = null;
		Integer instanceId = NumberUtils.toInt(instance.getValue());
		if (StringUtils.isEmpty(instanceFormat)) {
			if (instanceId > 0) {
				if (instanceFormat.contains("User")) {
					UserService userService = Context.getUserService();
					if (userService.getUser(instanceId).getId() != null) {
						names = (userService.getUser(instanceId).getDisplayString());
					} else {
						LOG.error("The user with Id " + userService.getUser(instanceId).getId() + " could not be found");
					}
				} else if (instanceFormat.contains("Location")) {
					LocationService locationService = Context.getLocationService();
					if (locationService.getLocation(instanceId).getId() != null) {
						names = locationService.getLocation(instanceId).getDisplayString();
					} else {
						LOG.error("The location with Id " + locationService.getLocation(instanceId).getId()
						        + " could not be found");
					}
				} else if (instanceFormat.contains("Provider")) {
					ProviderService providerService = Context.getProviderService();
					if (providerService.getProvider(instanceId).getId() != null) {
						names = providerService.getProvider(instanceId).getName();
					} else {
						LOG.error("The Provider with Id " + providerService.getProvider(instanceId).getId()
						        + " could not be found");
					}
				} else if (instanceFormat.contains("Concept")) {
					ConceptService conceptService = Context.getConceptService();
					if (conceptService.getConcept(instanceId) != null) {
						names = conceptService.getConcept(instanceId).getDisplayString();
					} else {
						LOG.error(
						        "The Concept with Id " + conceptService.getConcept(instanceId).getId()
						                + " could not be found");
					}
				} else if (instanceFormat.contains("Patient")) {
					PatientService patientService = Context.getPatientService();
					if (patientService.getPatient(instanceId) != null) {
						names = patientService.getPatient(instanceId).getPersonName().getFullName();
					} else {
						LOG.error(
						        "The Patient with Id " + patientService.getPatient(instanceId).getId()
						                + " could not be found");
					}
				} else if (instanceFormat.contains("Encounter")) {
					EncounterService encounterService = Context.getEncounterService();
					if (encounterService.getEncounter(instanceId) != null) {
						names = encounterService.getEncounter(instanceId).toString();
					} else {
						LOG.error(
						        "The Encounter with Id " + encounterService.getEncounter(instanceId).getId()
						                + " could not be "
						                + "found");
					}
				} else if (instanceFormat.contains("ProgramWorkflow")) {
					ProgramWorkflowService programWorkflowService = Context.getProgramWorkflowService();
					if (programWorkflowService.getProgram(instanceId).getId() != null) {
						names = programWorkflowService.getProgram(instanceId).getName();
					} else {
						LOG.error("The Program with Id " + programWorkflowService.getProgram(instanceId).getId()
						        + " could not be found");
					}
				} else {
					names = instance.getValue();
				}
			} else {
				LOG.error("The instance cannot be null or empty");
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

	@PropertySetter("attributeType")
	public void setAttributeType(PaymentAttribute instance, PaymentModeAttributeType attributeType) {
		instance.setAttributeType(attributeType);
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
