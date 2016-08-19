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

public class TestReceiptNumberGenerator implements IReceiptNumberGenerator {
	private int loadedCount = 0;
	private boolean loaded = false;

	@Override
	public String getName() {
		return "Test Receipt Number Generator";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public void load() {
		loadedCount++;
		loaded = true;
	}

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
		return loaded;
	}

	public int getLoadedCount() {
		return loadedCount;
	}
}
