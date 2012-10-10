package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IPaymentModeAttributeTypeService extends IMetadataService<PaymentModeAttributeType> {
}
