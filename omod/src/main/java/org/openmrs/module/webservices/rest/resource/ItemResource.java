package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Resource("item")
@Handler(supports = { Item.class }, order = 0)
public class ItemResource extends BaseRestMetadataResource<Item> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("name");
		description.addProperty("codes");
		description.addProperty("prices");
		description.addProperty("department", Representation.REF);
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = super.getCreatableProperties();
		description.addProperty("name");
		description.addProperty("codes");
		description.addProperty("prices");
		description.addProperty("department");
		return description;
	}	

	@Override
	public Item newDelegate() {
		return new Item();
	}
	
//	@PropertyGetter(value="department")
//	public String getItemDepartment(Item delegate) {
//		return delegate.getDepartment().getUuid();
//	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<Item>> getServiceClass() {
		return (Class<IMetadataService<Item>>)(Object)IItemService.class;
	}
}
