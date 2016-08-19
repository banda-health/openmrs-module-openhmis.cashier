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

import org.openmrs.module.openhmis.cashier.api.model.Bill;

/**
 * Represents classes that can generate receipt numbers.
 */
public interface IReceiptNumberGenerator {
	/**
	 * The name of the receipt number generator.
	 * @return The generator name.
	 */
	String getName();

	/**
	 * A description of the receipt number generator.
	 * @return The generator description.
	 */
	String getDescription();

	/**
	 * Gets the optional configuration page URL to configure this generator.
	 * @return The configuration page or a {@code null} or empty string if there is no configuration page.
	 */
	String getConfigurationPage();

	/**
	 * Gets whether this generator has been loaded.
	 * @return {@code true} if this generator has been loaded; otherwise, {@code false}.
	 */
	boolean isLoaded();

	/**
	 * Performs any loading needed by the generator.
	 */
	void load();

	/**
	 * Generates a new receipt number for the specified {@link Bill}. Note that the receipt number field for the specified
	 * bill is NOT set.
	 * @param bill The bill to generate a new receipt number for.
	 * @return The generated receipt number.
	 */
	String generateNumber(Bill bill);
}
