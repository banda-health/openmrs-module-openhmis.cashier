package org.openmrs.module.openhmis.cashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeAttributeTypeService;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.openhmis.cashier.api.security.BasicMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.cashier.api.security.IMetadataAuthorizationPrivileges;

public class PaymentModeAttributeTypeServiceImpl
		extends BaseMetadataServiceImpl<PaymentModeAttributeType>
		implements IPaymentModeAttributeTypeService {

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return new BasicMetadataAuthorizationPrivileges();
	}

	@Override
	protected void validate(PaymentModeAttributeType entity) throws APIException { }
}