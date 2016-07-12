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

import java.util.Date;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Provider;

/**
 * Model class to represent a cashier timesheet entry.
 */
public class Timesheet extends BaseOpenmrsData {
	public static final long serialVersionUID = 0L;

	private Integer timesheetId;
	private Provider cashier;
	private CashPoint cashPoint;
	private Date clockIn;
	private Date clockOut;

	@Override
	public Integer getId() {
		return timesheetId;
	}

	@Override
	public void setId(Integer id) {
		this.timesheetId = id;
	}

	public Provider getCashier() {
		return cashier;
	}

	public void setCashier(Provider cashier) {
		this.cashier = cashier;
	}

	public CashPoint getCashPoint() {
		return cashPoint;
	}

	public void setCashPoint(CashPoint cashPoint) {
		this.cashPoint = cashPoint;
	}

	public Date getClockIn() {
		return clockIn;
	}

	public void setClockIn(Date clockIn) {
		this.clockIn = clockIn;
	}

	public Date getClockOut() {
		return clockOut;
	}

	public void setClockOut(Date clockOut) {
		this.clockOut = clockOut;
	}
}
