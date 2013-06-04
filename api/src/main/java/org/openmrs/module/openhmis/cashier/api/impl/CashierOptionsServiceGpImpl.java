package org.openmrs.module.openhmis.cashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ICashierOptionsService;
import org.openmrs.module.openhmis.cashier.api.model.CashierOptions;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;

import java.math.BigDecimal;

/**
 * Service to load CashierOptions from global options
 * @author daniel
 *
 */
public class CashierOptionsServiceGpImpl implements ICashierOptionsService {
	public CashierOptions getOptions() {
		CashierOptions options = new CashierOptions();
		AdministrationService service = Context.getAdministrationService();
		try {
			options.setDefaultReceiptReportId(Integer.parseInt(service.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY)));
		} catch (NumberFormatException e) { /* Leave unset; must be handled, e.g. in ReceiptController */ }
		try {
			options.setRoundingMode(
				CashierOptions.RoundingMode.valueOf(service.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY)));
		} catch (NullPointerException e) { /* Use default if option is not set */ }
		
		try {
			options.setRoundToNearest(
				new BigDecimal(service.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY)));
		} catch (NumberFormatException e) { /* Use default */ }
		try {
			Integer itemId = Integer.parseInt(service.getGlobalProperty(CashierWebConstants.ROUNDING_ITEM_ID));
			IItemDataService itemService = Context.getService(IItemDataService.class);
			Item roundingItem = itemService.getById(itemId);
			options.setRoundingItemUuid(roundingItem.getUuid());
		} catch (NumberFormatException e) {
			if (options.getRoundToNearest().equals(BigDecimal.ZERO));
				/* Rounding disabled, so just leave this unset */
			else
				throw new APIException("Rounding enabled (nearest " + options.getRoundToNearest().toPlainString() + ") but no rounding item ID specified in options.");
		} catch (NullPointerException e) {
			  throw new APIException("Rounding item ID set in options but item not found", e);
		}
		
		// Will default to false
		options.setTimesheetRequired(Boolean.parseBoolean(service.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY)));

		return options;
	}
}
