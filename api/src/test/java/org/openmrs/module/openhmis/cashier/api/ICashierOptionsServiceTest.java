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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.commons.api.BaseModuleContextTest;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataServiceTest;
import org.openmrs.module.openhmis.inventory.api.IItemDataServiceTest;

public class ICashierOptionsServiceTest extends BaseModuleContextTest {
	public static final String OPTIONS_DATASET_VALID = TestConstants.BASE_DATASET_DIR + "CashierOptionsTestValid.xml";
	public static final String OPTIONS_DATASET_INVALID = TestConstants.BASE_DATASET_DIR + "CashierOptionsTestInvalid.xml";

	private ICashierOptionsService cashierOptionsService;

	@Before
	public void before() {
		cashierOptionsService = Context.getService(ICashierOptionsService.class);
	}

	/**
	 * @see ICashierOptionsService#getOptions()
	 * @verifies Load options
	 */
	@Test
	public void getOptions_shouldLoadOptions() throws Exception {
		executeDataSet(OPTIONS_DATASET_VALID);
		executeDataSet(IDepartmentDataServiceTest.DEPARTMENT_DATASET);
		executeDataSet(IItemDataServiceTest.ITEM_DATASET);

		CashierOptions options = cashierOptionsService.getOptions();
		Assert.assertEquals("4028814B399565AA01399681B1B5000E", options.getRoundingItemUuid());
		Assert.assertEquals(3, options.getDefaultReceiptReportId());
		Assert.assertEquals(CashierOptions.RoundingMode.MID, options.getRoundingMode());
		Assert.assertEquals(5, (int)options.getRoundToNearest());
		Assert.assertEquals(true, options.isTimesheetRequired());
	}

	/**
	 * @see ICashierOptionsService#getOptions()
	 * @verifies Revert to defaults if there are problems loading options
	 */
	@Test
	public void getOptions_shouldRevertToDefaultsIfThereAreProblemsLoadingOptions()
	        throws Exception {
		executeDataSet(OPTIONS_DATASET_INVALID);
		CashierOptions reference = new CashierOptions();
		CashierOptions options = cashierOptionsService.getOptions();
		Assert.assertEquals(reference.getRoundingItemUuid(), options.getRoundingItemUuid());
		Assert.assertEquals(reference.getDefaultReceiptReportId(), options.getDefaultReceiptReportId());
		Assert.assertEquals(reference.getRoundingMode(), options.getRoundingMode());
		Assert.assertEquals(reference.getRoundToNearest(), options.getRoundToNearest());
		Assert.assertEquals(reference.isTimesheetRequired(), options.isTimesheetRequired());
	}

}
