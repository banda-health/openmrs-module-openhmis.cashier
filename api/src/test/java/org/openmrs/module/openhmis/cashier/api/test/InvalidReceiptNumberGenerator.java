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
package org.openmrs.module.openhmis.cashier.api.test;

import org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.model.Bill;

public class InvalidReceiptNumberGenerator implements IReceiptNumberGenerator {
	public InvalidReceiptNumberGenerator(String name) {}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void load() {}

	@Override
	public String generateNumber(Bill bill) {
		return null;
	}

	@Override
	public String getConfigurationPage() {
		return null;
	}

	@Override
	public boolean isLoaded() {
		return false;
	}
}
