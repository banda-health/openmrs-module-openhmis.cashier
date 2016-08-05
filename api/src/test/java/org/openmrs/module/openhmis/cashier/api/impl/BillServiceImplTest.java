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
package org.openmrs.module.openhmis.cashier.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.IBillServiceTest;
import org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.ReceiptNumberGeneratorFactory;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@PrepareForTest(ReceiptNumberGeneratorFactory.class)
public class BillServiceImplTest extends IBillServiceTest {
	@Rule
	public PowerMockRule rule = new PowerMockRule();

	@BeforeClass
	public static void beforeClass() throws Exception {
		PowerMockAgent.initializeIfNeeded();
	}

	IReceiptNumberGenerator receiptNumberGenerator;

	@Before
	public void before() throws Exception {
		super.before();

		mockStatic(ReceiptNumberGeneratorFactory.class);
		receiptNumberGenerator = mock(IReceiptNumberGenerator.class);

		when(ReceiptNumberGeneratorFactory.getGenerator())
		        .thenReturn(receiptNumberGenerator);
	}

	@Override
	protected IBillService createService() {
		return Context.getService(IBillService.class);
	}

	/**
	 * @verifies Generate a new receipt number if one has not been defined.
	 * @see BillServiceImpl#save(org.openmrs.module.openhmis.cashier.api.model.Bill)
	 */
	@Test
	public void save_shouldGenerateANewReceiptNumberIfOneHasNotBeenDefined() throws Exception {
		Bill bill = createEntity(true);
		bill.setReceiptNumber(null);

		String receiptNumber = "Test Number";
		when(receiptNumberGenerator.generateNumber(bill))
		        .thenReturn(receiptNumber);

		service.save(bill);
		Context.flushSession();

		Bill savedBill = service.getById(bill.getId());
		Assert.assertEquals(receiptNumber, savedBill.getReceiptNumber());

		verify(receiptNumberGenerator, times(1)).generateNumber(bill);
	}

	/**
	 * @verifies Not generate a receipt number if one has already been defined.
	 * @see BillServiceImpl#save(org.openmrs.module.openhmis.cashier.api.model.Bill)
	 */
	@Test
	public void save_shouldNotGenerateAReceiptNumberIfOneHasAlreadyBeenDefined() throws Exception {
		String receiptNumber = "Test Number";
		Bill bill = createEntity(true);
		bill.setReceiptNumber(receiptNumber);

		service.save(bill);
		Context.flushSession();

		Bill savedBill = service.getById(bill.getId());
		Assert.assertEquals(receiptNumber, savedBill.getReceiptNumber());

		verify(receiptNumberGenerator, times(0)).generateNumber(bill);
	}

	/**
	 * @verifies Throw APIException if receipt number cannot be generated.
	 * @see BillServiceImpl#save(org.openmrs.module.openhmis.cashier.api.model.Bill)
	 */
	@Test(expected = APIException.class)
	public void save_shouldThrowAPIExceptionIfReceiptNumberCannotBeGenerated() throws Exception {
		Bill bill = createEntity(true);
		bill.setReceiptNumber(null);

		when(receiptNumberGenerator.generateNumber(bill))
		        .thenThrow(new APIException("Test exception"));

		service.save(bill);
	}
}
