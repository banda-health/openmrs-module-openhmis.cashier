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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.model.ItemPrice;
import org.openmrs.module.webservices.rest.helper.Converter;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * REST resource representing a {@link BillLineItem}.
 */
@Resource(name = RestConstants.VERSION_2 + "/cashier/billLineItem", supportedClass = BillLineItem.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class BillLineItemResource extends BaseRestDataResource<BillLineItem> {

	private static final Log LOG = LogFactory.getLog(BillLineItemResource.class);

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("item");
			description.addProperty("quantity");
			description.addProperty("price");
			description.addProperty("priceName");
			description.addProperty("priceUuid");
			description.addProperty("lineItemOrder");
		}
		return description;
	}

	@PropertySetter(value = "price")
	public void setPriceValue(BillLineItem instance, Object price) {
		instance.setPrice(Converter.objectToBigDecimal(price));
	}

	@PropertySetter(value = "priceName")
	public void setPriceName(BillLineItem instance, String name) {
		//name is set in setItemPriceMethod as not set in js
	}

	@PropertyGetter(value = "priceName")
	public String getPriceName(BillLineItem instance) {
		String itemName = instance.getPriceName();
		return StringUtils.isNotBlank(itemName) ? itemName : "";
	}

	@PropertySetter(value = "priceUuid")
	public void setItemPrice(BillLineItem instance, String uuid) {
		IItemDataService itemDataService = Context.getService(IItemDataService.class);
		ItemPrice itemPrice = itemDataService.getItemPriceByUuid(uuid);
		if (itemPrice != null) {
			instance.setItemPrice(itemPrice);
			instance.setPriceName(itemPrice.getName());
		}
	}

	@PropertyGetter(value = "priceUuid")
	public String getItemPriceUuid(BillLineItem instance) {
		try {
			ItemPrice itemPrice = instance.getItemPrice();
			return itemPrice != null ? itemPrice.getUuid() : "";
		} catch (Exception e) {
			LOG.warn("Price probably was deleted", e);
			return "";
		}
	}

	@Override
	public BillLineItem getByUniqueId(String uuid) {
		return null;
	}

	@Override
	public BillLineItem newDelegate() {
		return new BillLineItem();
	}

	@Override
	public Class<IEntityDataService<BillLineItem>> getServiceClass() {
		return null;
	}
}
