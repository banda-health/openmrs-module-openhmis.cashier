package org.openmrs.module.openhmis.cashier.uiframework;

import org.openmrs.util.OpenmrsClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The OpenMRS UI Framework configuration settings.
 */
public class UiConfigurationCashier implements BeanFactoryPostProcessor {

	private static Log log = LogFactory.getLog(UiConfigurationCashier.class);

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		log.info("Register openhmis.cashier module");
		try {
			// load UiFramework's StandardModuleUiConfiguration class
			Class cls = OpenmrsClassLoader.getInstance()
			        .loadClass("org.openmrs.ui.framework.StandardModuleUiConfiguration");

			// get spring's bean definition builder
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(cls);

			// set the module Id
			builder.addPropertyValue("moduleId", "openhmis.cashier");

			// register bean
			((DefaultListableBeanFactory)beanFactory).registerBeanDefinition(
			    "openhmisCashierStandardModuleUiConfiguration", builder.getBeanDefinition());
		} catch (ClassNotFoundException ex) {
			// StandardModuleUiConfiguration class not found!
			log.info("Unable to register openhmis.cashier UI 2.x module");
		}
	}
}
