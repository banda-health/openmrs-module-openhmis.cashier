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

@Resource(name = RestConstants.VERSION_2 + "/cashier/paymentAttribute", supportedClass = PaymentAttribute.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*" })
public class PaymentAttributeResource extends BaseRestAttributeDataResource<PaymentAttribute, PaymentModeAttributeType> {
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

		if (instanceFormat.contains("User")) {
			UserService userService = Context.getUserService();
			String userId = instance.getValue();
			names = (userService.getUser(Integer.valueOf(userId)).getDisplayString());
		} else if (instanceFormat.contains("Location")) {
			LocationService locationService = Context.getLocationService();
			String locationId = instance.getValue();
			names= locationService.getLocation(Integer.valueOf(locationId)).getDisplayString();
		} else if (instanceFormat.contains("Provider")) {
			ProviderService providerService = Context.getProviderService();
			String providerId = instance.getValue();
			names = providerService.getProvider(Integer.valueOf(providerId)).getName();
		} else if (instanceFormat.contains("Concept")) {
			ConceptService conceptService = Context.getConceptService();
			String conceptId = instance.getValue();
			names = conceptService.getConcept(Integer.valueOf(conceptId)).getDisplayString();
		} else if (instanceFormat.contains("Patient")) {
			PatientService patientService = Context.getPatientService();
			String patientId = instance.getValue();
			names = patientService.getPatient(Integer.valueOf(patientId)).getPersonName().getFullName();
		} else if (instanceFormat.contains("Encounter")) {
			EncounterService encounterService = Context.getEncounterService();
			String encounterId = instance.getValue();
			names = encounterService.getEncounter(Integer.valueOf(encounterId)).toString();
		} else if (instanceFormat.contains("ProgramWorkflow")) {
			ProgramWorkflowService programWorkflowService  = Context.getProgramWorkflowService();
			String programWorkflowId = instance.getValue();
			names = programWorkflowService.getProgram(Integer.valueOf(programWorkflowId)).getName();
		} else {
			names = instance.getValue();
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
