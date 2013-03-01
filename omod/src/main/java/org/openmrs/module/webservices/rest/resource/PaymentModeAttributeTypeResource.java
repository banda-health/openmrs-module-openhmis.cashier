/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
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

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeAttributeTypeService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Handler(supports = { PaymentModeAttributeType.class }, order = 0)
public class PaymentModeAttributeTypeResource extends BaseRestMetadataResource<PaymentModeAttributeType> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("format");
			description.addProperty("foreignKey");
			description.addProperty("regExp");
			description.addProperty("required");
		}
		return description;
	}
	
	@Override
	public PaymentModeAttributeType newDelegate() {
		return new PaymentModeAttributeType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataDataService<PaymentModeAttributeType>> getServiceClass() {
		return (Class<IMetadataDataService<PaymentModeAttributeType>>)(Object)IPaymentModeAttributeTypeService.class;
	}
}
