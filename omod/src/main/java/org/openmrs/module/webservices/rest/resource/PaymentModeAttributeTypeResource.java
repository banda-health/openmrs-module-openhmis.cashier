package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;

@Handler(supports = { PaymentModeAttributeType.class }, order = 0)
public class PaymentModeAttributeTypeResource extends BaseRestMetadataResource<PaymentModeAttributeType> {

	@Override
	public Class<IMetadataService<PaymentModeAttributeType>> getServiceClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentModeAttributeType newDelegate() {
		return new PaymentModeAttributeType();
	}

}
