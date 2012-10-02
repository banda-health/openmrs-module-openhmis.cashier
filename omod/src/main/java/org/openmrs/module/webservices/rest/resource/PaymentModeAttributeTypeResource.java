package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Handler(supports = { PaymentModeAttributeType.class }, order = 0)
public class PaymentModeAttributeTypeResource extends BaseRestMetadataResource<PaymentModeAttributeType> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("required");
		}
		return description;
	}
	
	@Override
	public PaymentModeAttributeType newDelegate() {
		return new PaymentModeAttributeType();
	}

	@Override
	public Class<IMetadataService<PaymentModeAttributeType>> getServiceClass() { return null; }
}
