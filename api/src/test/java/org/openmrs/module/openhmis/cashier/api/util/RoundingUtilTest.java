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
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsServiceTest;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.cashier.api.model.CashierSettings;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataServiceTest;
import org.openmrs.module.openhmis.inventory.api.model.Item;
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
	 * @see RoundingUtil#handleRoundingLineItem(Bill)
	 * @verifies add a rounding line item with the appropriate value
	 */
	@Test
	public void addRoundingLineItem_shouldAddARoundingLineItemWithTheAppropriateValue()
	        throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(5, (int)cashierOptions.getRoundToNearest());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(3));
		item.setQuantity(1);
		bill.addLineItem(item);

		// One line item
		Assert.assertEquals(1, bill.getLineItems().size());
		// Do rounding
		RoundingUtil.handleRoundingLineItem(bill);
		// Now two line items
		Assert.assertEquals(2, bill.getLineItems().size());
		// Assert rounding item's price
		BillLineItem rounding = bill.getLineItems().get(1);
		Assert.assertEquals(new BigDecimal(2), rounding.getTotal());
		// Assert evenly divisible by nearest
		Assert.assertEquals(new BigDecimal(0), bill.getTotal().remainder(
		    new BigDecimal(cashierOptions.getRoundToNearest())));
	}

	/**
	 * @verifies round bills with a non zero amount correctly for MID
	 * @see RoundingUtil#handleRoundingLineItem(Bill)
	 */
	@Test
	public void handleRoundingLineItem_shouldRoundBillsWithANonZeroAmountCorrectlyForMID() throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(5, (int)cashierOptions.getRoundToNearest());
		Assert.assertEquals(CashierOptions.RoundingMode.MID, cashierOptions.getRoundingMode());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(3.5));
		item.setQuantity(1);
		bill.addLineItem(item);

		// One line item
		Assert.assertEquals(1, bill.getLineItems().size());
		// Do rounding
		RoundingUtil.handleRoundingLineItem(bill);
		// Now two line items
		Assert.assertEquals(2, bill.getLineItems().size());
		// Assert rounding item's price
		BillLineItem rounding = bill.getLineItems().get(1);
		Assert.assertEquals(new BigDecimal(1.5), rounding.getTotal());
	}

	/**
	 * @verifies round bills with a non zero amount correctly for CEILING
	 * @see RoundingUtil#handleRoundingLineItem(Bill)
	 */
	@Test
	public void handleRoundingLineItem_shouldRoundBillsWithANonZeroAmountCorrectlyForCEILING() throws Exception {
		CashierSettings settings = ModuleSettings.loadSettings();
		settings.setCashierRoundingMode(CashierOptions.RoundingMode.CEILING.toString());
		ModuleSettings.saveSettings(settings);

		settings = ModuleSettings.loadSettings();
		Assert.assertEquals(5, (int)settings.getCashierRoundingToNearest());
		Assert.assertEquals(CashierOptions.RoundingMode.CEILING.toString(), settings.getCashierRoundingMode());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(3.5));
		item.setQuantity(1);
		bill.addLineItem(item);

		// One line item
		Assert.assertEquals(1, bill.getLineItems().size());
		// Do rounding
		RoundingUtil.handleRoundingLineItem(bill);
		// Now two line items
		Assert.assertEquals(2, bill.getLineItems().size());
		// Assert rounding item's price
		BillLineItem rounding = bill.getLineItems().get(1);
		Assert.assertEquals(new BigDecimal(1.5), rounding.getTotal());
	}

	/**
	 * @verifies round bills with a non zero amount correctly for FLOOR
	 * @see RoundingUtil#handleRoundingLineItem(Bill)
	 */
	@Test
	public void handleRoundingLineItem_shouldRoundBillsWithANonZeroAmountCorrectlyForFLOOR() throws Exception {
		CashierSettings settings = ModuleSettings.loadSettings();
		settings.setCashierRoundingMode(CashierOptions.RoundingMode.FLOOR.toString());
		ModuleSettings.saveSettings(settings);

		settings = ModuleSettings.loadSettings();
		Assert.assertEquals(5, (int)settings.getCashierRoundingToNearest());
		Assert.assertEquals(CashierOptions.RoundingMode.FLOOR.toString(), settings.getCashierRoundingMode());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(3.5));
		item.setQuantity(1);
		bill.addLineItem(item);

		// One line item
		Assert.assertEquals(1, bill.getLineItems().size());
		// Do rounding
		RoundingUtil.handleRoundingLineItem(bill);
		// Now two line items
		Assert.assertEquals(2, bill.getLineItems().size());
		// Assert rounding item's price
		BillLineItem rounding = bill.getLineItems().get(1);
		Assert.assertEquals(new BigDecimal(-3.5), rounding.getTotal());
	}

	/**
	 * @see RoundingUtil#handleRoundingLineItem(Bill)
	 * @verifies not modify a bill that needs no rounding
	 */
	@Test
	public void addRoundingLineItem_shouldNotModifyABillThatNeedsNoRounding()
	        throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(5, (int)cashierOptions.getRoundToNearest());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(5));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(1, bill.getLineItems().size());
	}

	@Test
	public void roundingInAdjustedBill_shouldNotAddRoundingLineItemIfRoundingDifferenceIsZero() throws Exception {
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(5));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(1, bill.getLineItems().size());
	}

	@Test
	public void roundingInAdjustedBill_shouldAddRoundingLineItemIfRoundingDifferenceIsNotZero() throws Exception {
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(7));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(2, bill.getLineItems().size());
	}

	@Test
	public void roundingInAdjustedBill_shouldUpdateRoundingLineItemIfRoundingDifferenceIsNotZero() throws Exception {
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(7));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(2, bill.getLineItems().size());

		CashierOptions cashierOptions = cashOptService.getOptions();
		IItemDataService itemService = Context.getService(IItemDataService.class);
		Item roundingItem = itemService.getByUuid(cashierOptions.getRoundingItemUuid());

		BillLineItem roundingLineItem = null;
		for (BillLineItem lineItem : bill.getLineItems()) {
			if (roundingItem.equals(lineItem.getItem())) {
				roundingLineItem = lineItem;
			}
		}

		Assert.assertNotNull(roundingLineItem);
		Assert.assertEquals(roundingLineItem.getTotal().intValue(), -2);

		bill.addLineItem(item);
		Assert.assertEquals(3, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(3, bill.getLineItems().size());

		roundingLineItem = null;
		for (BillLineItem lineItem : bill.getLineItems()) {
			if (roundingItem.equals(lineItem.getItem())) {
				roundingLineItem = lineItem;
			}
		}

		Assert.assertNotNull(roundingLineItem);
		Assert.assertEquals(roundingLineItem.getTotal().intValue(), 1);

	}

	@Test
	public void roundingInAdjustedBill_shouldDeleteRoundingLineItemIfRoundingDifferenceIsZero() throws Exception {
		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(7));
		item.setQuantity(1);
		bill.addLineItem(item);

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(2, bill.getLineItems().size());

		BillLineItem item2 = new BillLineItem();
		item2.setItem(lineItemItem);
		item2.setLineItemOrder(0);
		item2.setPrice(new BigDecimal(3));
		item2.setQuantity(1);
		bill.addLineItem(item2);

		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(2, bill.getLineItems().size());

		CashierOptions cashierOptions = cashOptService.getOptions();
		IItemDataService itemService = Context.getService(IItemDataService.class);
		Item roundingItem = itemService.getByUuid(cashierOptions.getRoundingItemUuid());

		boolean containsRoundingItem = false;
		for (BillLineItem lineItem : bill.getLineItems()) {
			if (roundingItem.equals(lineItem.getItem())) {
				containsRoundingItem = true;
			}
		}

		Assert.assertEquals(false, containsRoundingItem);
	}

	@Test
	public void roundingInAdjustedBill_shouldConsiderRoundingOfPreviousBill() throws Exception {
		CashierOptions cashierOptions = cashOptService.getOptions();
		Assert.assertEquals(5, (int)cashierOptions.getRoundToNearest());

		// Test bill
		Bill bill = new Bill();
		BillLineItem item = new BillLineItem();
		Item lineItemItem = new Item(1);
		item.setItem(lineItemItem);
		item.setLineItemOrder(0);
		item.setPrice(new BigDecimal(8));
		item.setQuantity(1);
		bill.addLineItem(item);

		IItemDataService itemService = Context.getService(IItemDataService.class);
		Item roundingItem = itemService.getByUuid(cashierOptions.getRoundingItemUuid());

		Assert.assertEquals(1, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(2, bill.getLineItems().size());
		item.setLineItemOrder(1);
		bill.addLineItem(item);
		Assert.assertEquals(3, bill.getLineItems().size());
		RoundingUtil.handleRoundingLineItem(bill);
		Assert.assertEquals(3, bill.getLineItems().size());
		BigDecimal roundingValue = new BigDecimal(0);
		BigDecimal itemTotal = new BigDecimal(0);
		int roundingItemCounter = 0;
		for (BillLineItem lineItem : bill.getLineItems()) {
			if (lineItem.getItem() != null && roundingItem.getId().equals(lineItem.getItem().getId())) {
				roundingValue = roundingValue.add(lineItem.getTotal());
				roundingItemCounter++;
			} else {
				itemTotal = itemTotal.add(lineItem.getTotal());
			}
		}
		Assert.assertEquals(16, itemTotal.intValue());
		Assert.assertEquals(-1, roundingValue.intValue());
		Assert.assertEquals(1, roundingItemCounter);
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearest() throws Exception {
		Assert.assertEquals(new BigDecimal(5), RoundingUtil.round(new BigDecimal(3), 5, CashierOptions.RoundingMode.MID));
		Assert.assertEquals(new BigDecimal(10), RoundingUtil.round(new BigDecimal(12), 5, CashierOptions.RoundingMode.MID));
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest ceiling
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearestCeiling() throws Exception {
		Assert.assertEquals(new BigDecimal(5), RoundingUtil.round(new BigDecimal(1), 5,
		    CashierOptions.RoundingMode.CEILING));
		BigDecimal decimal = new BigDecimal(BigInteger.ONE, 2); // 0.01
		Assert.assertEquals(new BigDecimal(1), RoundingUtil.round(decimal, 1, CashierOptions.RoundingMode.CEILING));
	}

	/**
	 * @see RoundingUtil#round(java.math.BigDecimal, java.math.BigDecimal, CashierOptions.RoundingMode)
	 * @verifies round to nearest floor
	 */
	@Test
	public void roundBillTotal_shouldRoundToNearestFloor() throws Exception {
		Assert.assertEquals(new BigDecimal(5), RoundingUtil.round(new BigDecimal(9), 5, CashierOptions.RoundingMode.FLOOR));
		BigDecimal decimal = new BigDecimal(BigInteger.valueOf(199), 2); // 1.99
		Assert.assertEquals(new BigDecimal(1), RoundingUtil.round(decimal, 1, CashierOptions.RoundingMode.FLOOR));
	}
}
