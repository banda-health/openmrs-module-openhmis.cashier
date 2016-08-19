package org.openmrs.module.openhmis.cashier.uiframework;

import org.openmrs.module.openhmis.commons.uiframework.UiConfigurationFactory;

/**
 * The OpenMRS UI Framework configuration settings.
 */
public class UiConfigurationCashier extends UiConfigurationFactory {

	@Override
	public String getModuleId() {
		return "openhmis.cashier";
	}
}
