/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.api;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.reflections.Reflections;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements {@link IReceiptNumberGenerator}
 */
@Transactional
public class ReceiptNumberGeneratorFactory {
	private static final Log LOG = LogFactory.getLog(ReceiptNumberGeneratorFactory.class);
	private static volatile IReceiptNumberGenerator generator;

	protected ReceiptNumberGeneratorFactory() {}

	/**
	 * Returns the currently defined {@link IReceiptNumberGenerator} for the system.
	 * @return The {@link IReceiptNumberGenerator}.
	 * @should Return the currently defined receipt number generator
	 * @should Load the generator if it has not been loaded
	 * @should not load the generator if it has been loaded
	 * @should Return null if no generator has been defined
	 * @should Throw APIException if generator class cannot be found
	 * @should Throw APIException if generator class cannot be instantiated
	 */
	public static IReceiptNumberGenerator getGenerator() {
		if (generator == null) {
			generator = createGeneratorInstance();
			if (generator == null) {
				return null;
			}
		}

		// Ensure that the generator is loaded
		if (!generator.isLoaded()) {
			generator.load();
		}

		return generator;
	}

	/**
	 * Sets the system-wide {@link IReceiptNumberGenerator}.
	 * @param generator The generator.
	 * @throws APIException
	 * @should Set the receipt number generator for the system
	 * @should Remove the current generator if set to null
	 */
	public static void setGenerator(IReceiptNumberGenerator generator) {
		Class<? extends IReceiptNumberGenerator> cls = (generator == null) ? null : generator.getClass();

		FactoryImpl.INSTANCE.setGeneratorClass(cls);

		ReceiptNumberGeneratorFactory.generator = generator;
	}

	private static IReceiptNumberGenerator createGeneratorInstance() {
		Class<? super IReceiptNumberGenerator> cls = null;
		try {
			cls = FactoryImpl.INSTANCE.getGeneratorClass();
			if (cls == null) {
				return null;
			}

			generator = (IReceiptNumberGenerator)cls.newInstance();
			return generator;
		} catch (ClassNotFoundException classEx) {
			LOG.warn("Attempt to load unknown receipt number generator type", classEx);
			throw new APIException("Could not locate receipt number generator class.", classEx);
		} catch (InstantiationException instantiationEx) {
			throw new APIException("Could not instantiate the '" + cls.getClass().getName() + "' class.", instantiationEx);
		} catch (IllegalAccessException accessEx) {
			throw new APIException("Could not access the '" + cls.getClass().getName() + "' class.", accessEx);
		}
	}

	/**
	 * Locates and instantiates all classes that implement {@link IReceiptNumberGenerator} in the current classpath.
	 * @return The instantiated receipt number generators.
	 * @should Locate all classes that implement IReceiptNumberGenerator
	 * @should Not throw an exception if the class instantiation fails
	 * @should Use the existing instance for the currently defined generator
	 */
	public static IReceiptNumberGenerator[] locateGenerators() {
		// Search for any modules that define classes which implement the IReceiptNumberGenerator interface
		Reflections reflections = new Reflections("org.openmrs.module");
		List<Class<? extends IReceiptNumberGenerator>> classes = new ArrayList<Class<? extends IReceiptNumberGenerator>>();
		for (Class<? extends IReceiptNumberGenerator> cls : reflections.getSubTypesOf(IReceiptNumberGenerator.class)) {
			// We only care about public instantiable classes so ignore others
			if (!cls.isInterface() && !Modifier.isAbstract(cls.getModifiers()) && Modifier.isPublic(cls.getModifiers())) {
				classes.add(cls);
			}
		}

		// Now attempt to instantiate each found class
		List<IReceiptNumberGenerator> instances = new ArrayList<IReceiptNumberGenerator>();
		for (Class<? extends IReceiptNumberGenerator> cls : classes) {
			if (generator != null && cls.equals(generator.getClass())) {
				instances.add(generator);
			} else {
				try {
					instances.add(cls.newInstance());
				} catch (Exception ex) {
					// We don't care about specific exceptions here.  Just log and ignore the class
					LOG.warn("Could not instantiate the '" + cls.getName() + "' class.  It will be ignored.");
				}
			}
		}

		// Finally, copy the instances to an array
		IReceiptNumberGenerator[] results = new IReceiptNumberGenerator[instances.size()];
		instances.toArray(results);

		return results;
	}

	/**
	 * Resets this factory, effectively creating a new instance. If you are using this for anything other than testing you
	 * are likely doing something wrong.
	 */
	static void reset() {
		generator = null;
	}

	/**
	 * Singleton implementation for storing and retrieving the generator in the database.
	 */
	private enum FactoryImpl {
		INSTANCE;

		@SuppressWarnings("unchecked")
		public Class<? super IReceiptNumberGenerator> getGeneratorClass() throws ClassNotFoundException {
			Class<? super IReceiptNumberGenerator> result = null;

			String propertyValue =
			        Context.getAdministrationService().getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR);
			if (!StringUtils.isEmpty(propertyValue)) {
				LOG.debug("Loading receipt number generator '" + propertyValue + "'...");
				result = (Class<? super IReceiptNumberGenerator>)Class.forName(propertyValue);
				LOG.debug("Receipt number generator loaded.");
			} else {
				LOG.warn("Request for receipt number generator when none has been defined.");
			}

			return result;
		}

		public void setGeneratorClass(Class<? extends IReceiptNumberGenerator> generatorClass) {
			String className = (generatorClass == null) ? "" : generatorClass.getName();
			GlobalProperty property = new GlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR, className);

			Context.getAdministrationService().saveGlobalProperty(property);
		}
	}
}
