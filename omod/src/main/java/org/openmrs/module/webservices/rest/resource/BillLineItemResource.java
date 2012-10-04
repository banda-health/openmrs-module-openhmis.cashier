package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IDataService;
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
	public Class<IDataService<BillLineItem>> getServiceClass() { return null; }
}
