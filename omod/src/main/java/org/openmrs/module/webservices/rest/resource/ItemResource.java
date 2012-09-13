package org.openmrs.module.webservices.rest.resource;

import java.util.HashSet;
import java.util.Set;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.model.ItemCode;
import org.openmrs.module.openhmis.cashier.api.model.ItemPrice;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
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
