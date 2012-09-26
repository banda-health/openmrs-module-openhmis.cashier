package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.ItemCode;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Handler(supports = ItemCode.class, order = 0)
public class ItemCodeResource extends BaseRestMetadataResource<ItemCode> implements IMetadataServiceResource<ItemCode> {

	@Override
	public ItemCode newDelegate() {
		return new ItemCode();
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.removeProperty("name");
		description.addProperty("code");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("code");
		return description;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<ItemCode>> getServiceClass() {
		return (Class<IMetadataService<ItemCode>>)(Object)IItemService.class;
	}
}
