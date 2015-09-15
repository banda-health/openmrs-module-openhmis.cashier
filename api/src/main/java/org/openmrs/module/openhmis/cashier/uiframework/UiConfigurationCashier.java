package org.openmrs.module.openhmis.cashier.uiframework;

import org.openmrs.annotation.OpenmrsProfile;
import org.openmrs.ui.framework.StandardModuleUiConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The OpenMRS UI Framework configuration settings.
 */
@Configuration
@OpenmrsProfile(modules = { "uiframework:*.*" })
public class UiConfigurationCashier {
	
	@Bean
	public StandardModuleUiConfiguration createUiConfigurationBean() {
			StandardModuleUiConfiguration standardModuleUiConfiguration = new StandardModuleUiConfiguration();
			standardModuleUiConfiguration.setModuleId("openhmis.cashier");
			return standardModuleUiConfiguration;
	}
}
