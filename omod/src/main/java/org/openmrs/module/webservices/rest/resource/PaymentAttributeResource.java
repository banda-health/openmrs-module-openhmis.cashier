package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;

@Handler(supports = {PaymentAttribute.class}, order = 0)
public class PaymentAttributeResource extends BaseRestDataResource<PaymentAttribute> {

	@Override
	public PaymentAttribute newDelegate() {
		return new PaymentAttribute();
	}

	@Override
	public Class<IDataService<PaymentAttribute>> getServiceClass() {
		throw new RuntimeException("No service class implemented for " + getClass().getSimpleName());
	}
}
