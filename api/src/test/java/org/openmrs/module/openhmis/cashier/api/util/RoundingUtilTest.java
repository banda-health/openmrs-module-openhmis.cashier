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
package org.openmrs.module.openhmis.cashier.api.util;


import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsServiceTest;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.inventory.api.IItemDataServiceTest;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class RoundingUtilTest extends BaseModuleContextSensitiveTest {
	private ICashierOptionsService cashOptService;
	
	@Before
	public void before() throws Exception {
		cashOptService = Context.getService(ICashierOptionsService.class);
		
		executeDataSet(ICashierOptionsServiceTest.OPTIONS_DATASET_VALID);
		executeDataSet(IItemDataServiceTest.ITEM_DATASET);
	}
	
	/**
	 * Assumes that the roundToNearest option is 5
	 * @see RoundingUtil#addRoundingLineItem(Bill)
	 * @verifies add a rounding line item with the appropriate value
	 */
	@Test
	public void addRoundingLineItem_shouldAddARoundingLineItemWithTheAppropriateValue()
			throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(new BigDecimal(5), cashierOptions.getRoundToNearest());
		
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		item.setPrice(new BigDecimal(3));
		item.setQuantity(1);
		bill.addLineItem(item);
		
		// One line item
		Assert.assertEquals(1, bill.getLineItems().size());
		// Do rounding
		RoundingUtil.addRoundingLineItem(bill);
		// Now two line items
		Assert.assertEquals(2, bill.getLineItems().size());
		// Assert rounding item's price
		BillLineItem rounding = bill.getLineItems().get(1);
		Assert.assertEquals(new BigDecimal(2), rounding.getTotal());
		// Assert evenly divisible by nearest
		Assert.assertEquals(new BigDecimal(0), bill.getTotal().remainder(cashierOptions.getRoundToNearest()));
	}

	/**
	 * @see RoundingUtil#addRoundingLineItem(Bill)
	 * @verifies not modify a bill that needs no rounding
	 */
	@Test
	public void addRoundingLineItem_shouldNotModifyABillThatNeedsNoRounding()
			throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(new BigDecimal(5), cashierOptions.getRoundToNearest());
		
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		item.setPrice(new BigDecimal(5));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.addRoundingLineItem(bill);
		Assert.assertEquals(1, bill.getLineItems().size());
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearest() throws Exception {
		Assert.assertEquals(new BigDecimal(5),
			RoundingUtil.round(new BigDecimal(3), new BigDecimal(5), CashierOptions.RoundingMode.MID));
		Assert.assertEquals(new BigDecimal(10),
			RoundingUtil.round(new BigDecimal(12), new BigDecimal(5), CashierOptions.RoundingMode.MID));
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest ceiling
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearestCeiling() throws Exception {
		Assert.assertEquals(new BigDecimal(5),
			RoundingUtil.round(new BigDecimal(1), new BigDecimal(5), CashierOptions.RoundingMode.CEILING));
		BigDecimal decimal = new BigDecimal(BigInteger.ONE, 2); // 0.01
		Assert.assertEquals(new BigDecimal(1),
			RoundingUtil.round(decimal, new BigDecimal(1), CashierOptions.RoundingMode.CEILING));
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest floor
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearestFloor() throws Exception {
		Assert.assertEquals(new BigDecimal(5),
			RoundingUtil.round(new BigDecimal(9), new BigDecimal(5), CashierOptions.RoundingMode.FLOOR));
		BigDecimal decimal = new BigDecimal(BigInteger.valueOf(199), 2); // 1.99
		Assert.assertEquals(new BigDecimal(1),
			RoundingUtil.round(decimal, new BigDecimal(1), CashierOptions.RoundingMode.FLOOR));
	}
}
