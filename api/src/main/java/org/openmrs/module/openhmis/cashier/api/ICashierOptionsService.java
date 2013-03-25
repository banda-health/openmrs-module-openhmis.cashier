package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;

public interface ICashierOptionsService {
	/**
	 * Load cashier options from wherever
	 * @return CashierOptions Loaded options
	 * @should load options
	 * @should throw APIException if a rounding item ID is specified but the item cannot be retrieved
	 * @should revert to defaults if there are problems loading options
	 */
	public CashierOptions getOptions();
}
