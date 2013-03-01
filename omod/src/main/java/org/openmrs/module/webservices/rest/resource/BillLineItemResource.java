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
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

@Handler(supports = { BillLineItem.class })
public class BillLineItemResource extends BaseRestDataResource<BillLineItem> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("item");
			description.addProperty("quantity");
			description.addProperty("price");
			description.addProperty("lineItemOrder");
		}
		return description;
	}
	
	@PropertySetter(value = "price")
	public void setPrice(BillLineItem instance, Object price) throws ConversionException {
		instance.setPrice(ItemPriceResource.objectToBigDecimal(price));
	}

	@Override
	public BillLineItem newDelegate() {
		return new BillLineItem();
	}

	@Override
	public Class<IEntityDataService<BillLineItem>> getServiceClass() { return null; }
}
