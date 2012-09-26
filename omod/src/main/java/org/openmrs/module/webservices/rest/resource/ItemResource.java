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
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IDepartmentService;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.model.ItemCode;
import org.openmrs.module.openhmis.cashier.api.model.ItemPrice;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Resource("item")
@Handler(supports = { Item.class }, order = 0)
public class ItemResource extends BaseRestMetadataResource<Item> {

	public SimpleObject search(String query, String department_uuid, RequestContext context) throws ResponseException {
		IItemService service = (IItemService) Context.getService(getServiceClass());
		IDepartmentService deptService = (IDepartmentService) Context.getService(IDepartmentService.class);
		Department department = deptService.getByUuid(department_uuid);

		PagingInfo pagingInfo = MetadataSearcher.getPagingInfoFromContext(context);
		List<Item> items = service.findItems(department, query, context.getIncludeAll(), pagingInfo);
		Boolean hasMoreResults = (pagingInfo.getPage() * pagingInfo.getPageSize()) < pagingInfo.getTotalRecordCount();
		AlreadyPaged<Item> results = new AlreadyPaged<Item>(context, items, hasMoreResults);
		return results.toSimpleObject();
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof RefRepresentation) {
			description.addProperty("codes");
			description.addProperty("department", Representation.REF);
		}
		else if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("name");
			description.addProperty("codes", Representation.REF);
			description.addProperty("prices", Representation.REF);
			description.addProperty("department", Representation.REF);
			description.addProperty("defaultPrice", Representation.REF);
		}
		return description;
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = super.getCreatableProperties();
		description.addProperty("name");
		description.addProperty("codes");
		description.addProperty("prices");
		description.addProperty("department");
		description.addProperty("defaultPrice");
		return description;
	}

	@PropertySetter(value="codes")
	public void setItemCodes(Item instance, Set<ItemCode> codes) {
		if (instance.getCodes() == null)
			instance.setCodes(new HashSet<ItemCode>());
		BaseRestDataResource.updateCollection(instance.getCodes(), codes);
		for (ItemCode code : instance.getCodes()) {
			code.setItem(instance);
		}
	}

	@PropertySetter(value="prices")
	public void setItemPrices(Item instance, Set<ItemPrice> prices) {
		if (instance.getPrices() == null)
			instance.setPrices(new HashSet<ItemPrice>());
		BaseRestDataResource.updateCollection(instance.getPrices(), prices);
		for (ItemPrice price : instance.getPrices()) {
			price.setItem(instance);
		}
	}

	@PropertySetter(value="defaultPrice")
	public void setDefaultPrice(Item instance, String price_uuid) {
		for (ItemPrice price : instance.getPrices()) {
			if (price.getUuid().equals(price_uuid)) {
				instance.setDefaultPrice(price);
				break;
			}
		}
	}

	@Override
	public Item newDelegate() {
		return new Item();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<Item>> getServiceClass() {
		return (Class<IMetadataService<Item>>)(Object)IItemService.class;
	}
}
