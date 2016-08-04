package org.openmrs.module.openhmis.cashier.api.model;

/**
 * The allowable settings for the cashier module.
 */
public class CashierSettings {
	public static final long serialVersionUID = 1L;

	private Boolean adjustmentReasonField;
	private Boolean allowBillAdjustment;
	private Boolean autoFillPaymentAmount;
	private Integer defaultReceiptReportId;
	private Integer defaultShitReportId;
	private Boolean cashierMandatory;
	private Integer cashierRoundingToNearest;
	private String cashierRoundingMode;
	private Boolean cashierTimesheetRequired;
	private Integer patientDashboard2BillCount;

	public Boolean getAdjustmentReasonField() {
		return adjustmentReasonField;
	}

	public void setAdjustmentReasonField(Boolean adjustmentReasonField) {
		this.adjustmentReasonField = adjustmentReasonField;
	}

	public Boolean getAllowBillAdjustment() {
		return allowBillAdjustment;
	}

	public void setAllowBillAdjustment(Boolean allowBillAdjustment) {
		this.allowBillAdjustment = allowBillAdjustment;
	}

	public Boolean getAutoFillPaymentAmount() {
		return autoFillPaymentAmount;
	}

	public void setAutoFillPaymentAmount(Boolean autoFillPaymentAmount) {
		this.autoFillPaymentAmount = autoFillPaymentAmount;
	}

	public Integer getDefaultReceiptReportId() {
		return defaultReceiptReportId;
	}

	public void setDefaultReceiptReportId(Integer defaultReceiptReportId) {
		this.defaultReceiptReportId = defaultReceiptReportId;
	}

	public Integer getDefaultShitReportId() {
		return defaultShitReportId;
	}

	public void setDefaultShitReportId(Integer defaultShitReportId) {
		this.defaultShitReportId = defaultShitReportId;
	}

	public Boolean getCashierMandatory() {
		return cashierMandatory;
	}

	public void setCashierMandatory(Boolean cashierMandatory) {
		this.cashierMandatory = cashierMandatory;
	}

	public Integer getCashierRoundingToNearest() {
		return cashierRoundingToNearest;
	}

	public void setCashierRoundingToNearest(Integer cashierRoundingToNearest) {
		this.cashierRoundingToNearest = cashierRoundingToNearest;
	}

	public String getCashierRoundingMode() {
		return cashierRoundingMode;
	}

	public void setCashierRoundingMode(String cashierRoundingMode) {
		this.cashierRoundingMode = cashierRoundingMode;
	}

	public Boolean getCashierTimesheetRequired() {
		return cashierTimesheetRequired;
	}

	public void setCashierTimesheetRequired(Boolean cashierTimesheetRequired) {
		this.cashierTimesheetRequired = cashierTimesheetRequired;
	}

	public Integer getPatientDashboard2BillCount() {
		return patientDashboard2BillCount;
	}

	public void setPatientDashboard2BillCount(Integer numberOfBillsToShowOnEachPage) {
		this.patientDashboard2BillCount = numberOfBillsToShowOnEachPage;
	}
}
