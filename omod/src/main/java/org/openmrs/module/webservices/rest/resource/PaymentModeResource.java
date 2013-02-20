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
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

import java.util.ArrayList;
import java.util.List;

@Resource("paymentMode")
@Handler(supports = { PaymentMode.class }, order = 0)
public class PaymentModeResource extends BaseRestMetadataResource<PaymentMode> {
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);

		description.addProperty("name");
		description.addProperty("attributeTypes");

		return description;
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = super.getCreatableProperties();

		description.addProperty("name");
		description.addProperty("attributeTypes");

		return description;
	}

	@PropertySetter(value="attributeTypes")
	public void setAttributeTypes(PaymentMode instance, List<PaymentModeAttributeType> attributeTypes) {
		if (instance.getAttributeTypes() == null) {
			instance.setAttributeTypes(new ArrayList<PaymentModeAttributeType>());
		}

		BaseRestDataResource.updateCollection(instance.getAttributeTypes(), attributeTypes);
		for (PaymentModeAttributeType attributeType : instance.getAttributeTypes()) {
			attributeType.setPaymentMode(instance);
		}
	}

	@Override
	public PaymentMode newDelegate() {
		return new PaymentMode();
	}

	@Override
	public Class<? extends IMetadataService<PaymentMode>> getServiceClass() {
		return IPaymentModeService.class;
	}
}
