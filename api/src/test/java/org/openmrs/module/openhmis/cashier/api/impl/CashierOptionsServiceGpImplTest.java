package org.openmrs.module.openhmis.cashier.api.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class CashierOptionsServiceGpImplTest {
	private CashierOptionsServiceGpImpl optionsService = null;
	private AdministrationService adminService = null;
	private IItemDataService itemService = null;

	@Before
	public void before() {
		adminService = mock(AdministrationService.class);
		itemService = mock(IItemDataService.class);

		optionsService = new CashierOptionsServiceGpImpl(adminService, itemService);
	}

	/**
	 * @verifies load cashier options from the database
	 * @see CashierOptionsServiceGpImpl#getOptions()
	 */
	@Test
	public void getOptions_shouldLoadCashierOptionsFromTheDatabase() throws Exception {
		when(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY))
				.thenReturn("1");
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY))
				.thenReturn(CashierOptions.RoundingMode.MID.toString());
		when(adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY))
				.thenReturn("5");
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID))
				.thenReturn("1");
		when(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY))
				.thenReturn("true");

		Item item = new Item();
		when(itemService.getById(1))
				.thenReturn(item);

		CashierOptions options = optionsService.getOptions();

		Assert.assertNotNull(options);
		Assert.assertEquals(1, options.getDefaultReceiptReportId());
		Assert.assertEquals(CashierOptions.RoundingMode.MID, options.getRoundingMode());
		Assert.assertEquals(new BigDecimal(5), options.getRoundToNearest());
		Assert.assertEquals(item.getUuid(), options.getRoundingItemUuid());
		Assert.assertEquals(true, options.isTimesheetRequired());
	}

	/**
	 * @verifies throw APIException if rounding is set but rounding item is not
	 * @see CashierOptionsServiceGpImpl#getOptions()
	 */
	@Test(expected = APIException.class)
	public void getOptions_shouldThrowAPIExceptionIfRoundingIsSetButRoundingItemIsNot() throws Exception {
		when(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY))
				.thenReturn(CashierOptions.RoundingMode.FLOOR.toString());
		when(adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY))
				.thenReturn("5");
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY))
				.thenReturn(null);

		optionsService.getOptions();
	}

	/**
	 * @verifies not throw exception if numeric options are null
	 * @see CashierOptionsServiceGpImpl#getOptions()
	 */
	@Test
	public void getOptions_shouldNotThrowExceptionIfNumericOptionsAreNull() throws Exception {
		when(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY))
				.thenReturn(null);

		CashierOptions options = optionsService.getOptions();

		Assert.assertNotNull(options);
	}

	/**
	 * @verifies default to false if timesheet required is not specified
	 * @see CashierOptionsServiceGpImpl#getOptions()
	 */
	@Test
	public void getOptions_shouldDefaultToFalseIfTimesheetRequiredIsNotSpecified() throws Exception {
		when(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY))
				.thenReturn(null);

		CashierOptions options = optionsService.getOptions();

		Assert.assertNotNull(options);
		Assert.assertEquals(false, options.isTimesheetRequired());
	}

	/**
	 * @verifies throw APIException if rounding is set but rounding item cannot be found
	 * @see CashierOptionsServiceGpImpl#getOptions()
	 */
	@Test(expected = APIException.class)
	public void getOptions_shouldThrowAPIExceptionIfRoundingIsSetButRoundingItemCannotBeFound() throws Exception {
		when(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY))
				.thenReturn(null);
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY))
				.thenReturn(CashierOptions.RoundingMode.FLOOR.toString());
		when(adminService.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY))
				.thenReturn("5");
		when(adminService.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID))
				.thenReturn("5");
		when(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY))
				.thenReturn(null);

		when(itemService.getById(5))
				.thenReturn(null);

		optionsService.getOptions();
	}
}
