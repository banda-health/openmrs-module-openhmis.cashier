/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.api.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.module.openhmis.cashier.api.IBillServiceTest;
import org.openmrs.module.openhmis.cashier.api.ReceiptNumberGeneratorFactory;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class BillServiceImplTest extends IBillServiceTest {
	ReceiptNumberGeneratorFactory factory;

	/**
	 * @verifies Generate a new receipt number if one has not been defined.
	 * @see BillServiceImpl#save(org.openmrs.module.openhmis.cashier.api.model.Bill)
	 */
	@Test
	public void save_shouldGenerateANewReceiptNumberIfOneHasNotBeenDefined() throws Exception {
		Bill bill = createEntity(true);
		bill.setReceiptNumber(null);


	}

	/**
	 * @verifies Not generate a receipt number if one has already been defined.
	 * @see BillServiceImpl#save(org.openmrs.module.openhmis.cashier.api.model.Bill)
	 */
	@Test
	public void save_shouldNotGenerateAReceiptNumberIfOneHasAlreadyBeenDefined() throws Exception {

	}
}
