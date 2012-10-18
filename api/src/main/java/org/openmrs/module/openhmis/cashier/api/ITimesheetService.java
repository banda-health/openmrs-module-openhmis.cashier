/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.Provider;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;

public interface ITimesheetService extends IDataService<Timesheet> {
	/**
	 * Gets the current {@link Timesheet} that the specified {@link Provider}.
	 * @param cashier The cashier.
	 * @return The {@link Timesheet} or {@code null} is the cashier is not clocked in.
	 * @should return the current timesheet for the cashier
	 * @should return null if the cashier has no timesheets
	 * @should return the most recent timesheet if the cashier is clocked into multiple timesheets
	 * @should return null if the timesheet is clocked out
	 */
	Timesheet getCurrentTimesheet(Provider cashier);
}
