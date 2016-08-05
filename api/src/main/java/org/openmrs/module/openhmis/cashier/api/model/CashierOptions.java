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
package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;

/**
 * Model class that give the options a {@link org.openmrs.Person} (Cashier) has.
 */
public class CashierOptions {
	public static final long serialVersionUID = 0L;

	private Integer roundToNearest = 0;
	private RoundingMode roundingMode = RoundingMode.MID;
	private String roundingItemUuid;
	private int defaultReceiptReportId;
	private boolean timesheetRequired = false;

	public String getRoundingItemUuid() {
		return roundingItemUuid;
	}

	public void setRoundingItemUuid(String roundingItemUuid) {
		this.roundingItemUuid = roundingItemUuid;
	}

	// Getters & setters
	public Integer getRoundToNearest() {
		return roundToNearest;
	}

	public void setRoundToNearest(Integer roundToNearest) {
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

	public boolean isTimesheetRequired() {
		return timesheetRequired;
	}

	public void setTimesheetRequired(boolean timesheetRequired) {
		this.timesheetRequired = timesheetRequired;
	}

	/**
	 * Defines the collection of constants to be used for setting the rounding mode
	 */
	public enum RoundingMode {
		FLOOR(1), MID(2), CEILING(3);

		@SuppressWarnings("unused")
		private int value;

		private RoundingMode(int value) {
			this.value = value;
		}
	}
}
