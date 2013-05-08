package org.openmrs.module.openhmis.cashier.api;


import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.TestConstants;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class ICashierOptionsServiceTest extends BaseModuleContextSensitiveTest {
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
		executeDataSet(IItemServiceTest.ITEM_DATASET);
		CashierOptions options = cashierOptionsService.getOptions();
		Assert.assertEquals("4028814B399565AA01399681B1B5000E", options.getRoundingItemUuid());
		Assert.assertEquals(3, options.getDefaultReceiptReportId());
		Assert.assertEquals(CashierOptions.RoundingMode.MID, options.getRoundingMode());
		Assert.assertTrue(new BigDecimal(5).equals(options.getRoundToNearest()));
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

	/**
	 * @see ICashierOptionsService#getOptions()
	 * @verifies throw APIException if a rounding item ID is specified but the item cannot be retrieved
	 */
	@Test(expected = APIException.class)
	public void getOptions_shouldThrowAPIExceptionIfARoundingItemIDIsSpecifiedButTheItemCannotBeRetrieved()
			throws Exception {
		executeDataSet(OPTIONS_DATASET_VALID);		
		cashierOptionsService.getOptions();
	}
}