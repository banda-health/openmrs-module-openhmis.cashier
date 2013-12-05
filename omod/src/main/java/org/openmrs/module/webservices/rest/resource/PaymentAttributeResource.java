/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Resource(name=RestConstants.VERSION_2 + "/cashier/paymentAttribute", supportedClass=PaymentAttribute.class, supportedOpenmrsVersions={"1.9"})
public class PaymentAttributeResource extends BaseRestDataResource<PaymentAttribute> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("paymentModeAttributeType", Representation.REF);
			description.addProperty("value");
			description.addProperty("valueName", findMethod("getValueName"));
			description.addProperty("order", findMethod("getAttributeOrder"));
		}
		return description;
	}
	
	@Override
	public String getDisplayString(PaymentAttribute instance) {
		return instance.getPaymentModeAttributeType().getName() + ": " + instance.getValue();
	}
	
	public String getValueName(PaymentAttribute instance) {
		if (instance.getPaymentModeAttributeType().getFormat().indexOf("Concept") != -1) {
			ConceptService service = Context.getService(ConceptService.class);
			Concept concept = service.getConcept(instance.getValue());
			return concept.getDisplayString();
		}		
		
		else return instance.getValue(); 	
	}

	public Integer getAttributeOrder(PaymentAttribute instance) {
		return instance.getPaymentModeAttributeType().getAttributeOrder();
	}

	@Override
	public PaymentAttribute newDelegate() {
		return new PaymentAttribute();
	}

	@Override
	public Class<IEntityDataService<PaymentAttribute>> getServiceClass() {
		throw new RuntimeException("No service class implemented for " + getClass().getSimpleName());
	}
}
