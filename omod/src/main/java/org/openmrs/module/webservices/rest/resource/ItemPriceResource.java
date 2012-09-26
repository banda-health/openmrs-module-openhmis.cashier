package org.openmrs.module.webservices.rest.resource;

import java.math.BigDecimal;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.ItemPrice;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ConversionException;

@Handler(supports = ItemPrice.class, order = 0)
public class ItemPriceResource extends BaseRestMetadataResource<ItemPrice> implements IMetadataServiceResource<ItemPrice> {

	@PropertySetter(value = "price")
	public void setPrice(ItemPrice instance, Object price) throws ConversionException {
		if (Double.class.isAssignableFrom(price.getClass()))
			instance.setPrice(BigDecimal.valueOf((Double) price));
		else if (Integer.class.isAssignableFrom(price.getClass()))
			instance.setPrice(BigDecimal.valueOf((Integer) price));
		else
			throw new ConversionException("Can't convert given price to " + BigDecimal.class.getSimpleName());
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.removeProperty("name");
		description.addProperty("price");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("price");
		return description;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<ItemPrice>> getServiceClass() {
		return (Class<IMetadataService<ItemPrice>>)(Object)IItemService.class;
	}

	@Override
	public ItemPrice newDelegate() {
		return new ItemPrice();
	}

}
