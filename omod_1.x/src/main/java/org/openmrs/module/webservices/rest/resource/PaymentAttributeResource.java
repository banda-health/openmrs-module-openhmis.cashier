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

import org.apache.log4j.Logger;
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
	private static final Logger log = Logger.getLogger(PaymentAttributeResource.class);

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
			try {
				Integer userId = Integer.valueOf(instance.getValue());
				names = (userService.getUser(userId).getDisplayString());
			} catch (NumberFormatException ex) {
				log.error("The user Id should be a number  " + ex);
			}

		} else if (instanceFormat.contains("Location")) {
			LocationService locationService = Context.getLocationService();
			try {
				Integer locationId = Integer.valueOf(instance.getValue());
				names= locationService.getLocation(locationId).getDisplayString();
			} catch (NumberFormatException ex) {
				log.error("The location Id should be a number " + ex);
			}
		} else if (instanceFormat.contains("Provider")) {
			ProviderService providerService = Context.getProviderService();
			try {
				Integer providerId = Integer.valueOf(instance.getValue());
				names = providerService.getProvider(providerId).getName();
			} catch (NumberFormatException ex) {
				log.error("The Provider Id should be a number " + ex);
			}
		} else if (instanceFormat.contains("Concept")) {
			ConceptService conceptService = Context.getConceptService();
			try {
				Integer conceptId = Integer.valueOf(instance.getValue());
				names = conceptService.getConcept(conceptId).getDisplayString();
			} catch (NumberFormatException ex) {
				log.error("The Concept Id should be a number " + ex);
			}
		} else if (instanceFormat.contains("Patient")) {
			PatientService patientService = Context.getPatientService();
			try {
				Integer patientId = Integer.valueOf(instance.getValue());
				names = patientService.getPatient(patientId).getPersonName().getFullName();
			} catch (NumberFormatException ex) {
				log.error("The Patient Id should be a number " +ex);
			}
		} else if (instanceFormat.contains("Encounter")) {
			EncounterService encounterService = Context.getEncounterService();
			try {
				Integer encounterId = Integer.valueOf(instance.getValue());
				names = encounterService.getEncounter(encounterId).toString();
			} catch (NumberFormatException ex) {
				log.error("Encounter Id Should be a number " +ex);
			}
		} else if (instanceFormat.contains("ProgramWorkflow")) {
			ProgramWorkflowService programWorkflowService  = Context.getProgramWorkflowService();
			try {
				Integer programWorkflowId = Integer.valueOf(instance.getValue());
				names = programWorkflowService.getProgram(programWorkflowId).getName();
			} catch (NumberFormatException ex) {
				log.error("The Program WorkFlow should be a number " + ex);
			}
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
