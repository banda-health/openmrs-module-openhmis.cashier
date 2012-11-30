package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;

public class CashierOptions {
	public enum RoundingMode {
		FLOOR(1), MID(2), CEILING(3);
		
		@SuppressWarnings("unused")
		private int value;

		private RoundingMode(int value) {
			this.value = value;
		}
	}
	
	private BigDecimal roundToNearest = new BigDecimal(1);
	private RoundingMode roundingMode = RoundingMode.MID;
	private int defaultReceiptReportId;
	private String defaultReceiptReportName;
	private boolean timesheetRequired;
	
	public void loadFromGlobalOptions() {
		AdministrationService service = Context.getAdministrationService();
		setDefaultReceiptReportId(Integer.parseInt(service.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY)));
		setDefaultReceiptReportName(service.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_NAME_PROPERTY));
		try {
			setRoundingMode(
				CashierOptions.RoundingMode.valueOf(service.getGlobalProperty(CashierWebConstants.ROUNDING_MODE_PROPERTY)));
		} catch (NullPointerException e) { /* Use default if option is not set */ }
		
		try {
			setRoundToNearest(
				new BigDecimal(service.getGlobalProperty(CashierWebConstants.ROUND_TO_NEAREST_PROPERTY)));
		} catch (NullPointerException e) { /* Use default */ }
		
		setTimesheetRequired(Boolean.parseBoolean(service.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY)));
	}
	
	// Getters & setters
	public BigDecimal getRoundToNearest() {
		return roundToNearest;
	}
	public void setRoundToNearest(BigDecimal roundToNearest) {
		this.roundToNearest = roundToNearest;
	}
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}
	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}
	public int getDefaultReceiptReportId() {
		return defaultReceiptReportId;
	}
	public void setDefaultReceiptReportId(int defaultReceiptReportId) {
		this.defaultReceiptReportId = defaultReceiptReportId;
	}
	public String getDefaultReceiptReportName() {
		return defaultReceiptReportName;
	}
	public void setDefaultReceiptReportName(String defaultReceiptReportName) {
		this.defaultReceiptReportName = defaultReceiptReportName;
	}
	public boolean isTimesheetRequired() {
		return timesheetRequired;
	}
	public void setTimesheetRequired(boolean timesheetRequired) {
		this.timesheetRequired = timesheetRequired;
	}
}
