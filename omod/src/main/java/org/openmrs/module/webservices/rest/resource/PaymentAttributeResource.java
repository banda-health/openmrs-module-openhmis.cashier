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
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeAttributeTypeService;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ObjectNotFoundException;

@Resource(name = RestConstants.VERSION_2 + "/cashier/paymentAttribute", supportedClass = PaymentAttribute.class,
        supportedOpenmrsVersions = { "1.9" })
public class PaymentAttributeResource
        extends BaseRestInstanceAttributeDataResource<PaymentAttribute, Payment, PaymentMode, PaymentModeAttributeType> {
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
		return instance.getAttributeType().getName() + ": " + instance.getValue();
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
	
	// Work around TypeVariable issue on base generic property (BaseCustomizableInstanceData.getInstanceType)
	@PropertySetter("attributeType")
	public void setAttributeType(PaymentAttribute instance, String uuid) {
		IPaymentModeAttributeTypeService service = Context.getService(IPaymentModeAttributeTypeService.class);
		
		PaymentModeAttributeType type = service.getByUuid(uuid);
		if (type == null) {
			throw new ObjectNotFoundException();
		}
		
		instance.setAttributeType(type);
	}
	
	@Override
	public PaymentAttribute newDelegate() {
		return new PaymentAttribute();
	}
	
	@Override
	public Class<? extends IEntityDataService<PaymentAttribute>> getServiceClass() {
		throw new RuntimeException("No service class implemented for " + getClass().getSimpleName());
	}
}
