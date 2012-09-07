package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Resource("billingitem")
@Handler(supports = { Item.class }, order = 0)
public class BillingItemResource extends BaseRestMetadataResource<Item> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = getDefaultRepresentationDescription();
		description.addProperty("name");
		description.addProperty("description");
		return description;
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
