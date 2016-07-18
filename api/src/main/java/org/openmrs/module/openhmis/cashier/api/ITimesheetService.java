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

import java.util.Date;
import java.util.List;

import org.openmrs.Provider;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;

/**
 * Interface that represents classes which perform data operations for {@link Timesheet}s.
 */
public interface ITimesheetService extends IEntityDataService<Timesheet> {
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

	/**
	 * Gets all the {@link Timesheet}'s for the specified user on the specified day.
	 * @param cashier The cashier.
	 * @param date The date.
	 * @return All the timesheets for the cashier on the specified day.
	 * @should return empty list if there are no timesheets for date
	 * @should return timesheets that start and end on date
	 * @should return timesheets that start on date and end on different date
	 * @should return timesheet that start on different date and end on date
	 * @should return timesheets that start before date but end after date
	 * @should return timesheets that start before date and have not ended
	 */
	List<Timesheet> getTimesheetsByDate(Provider cashier, Date date);

	/**
	 * Closes all open {@link Timesheet}'s.
	 * @should return close all open timesheets
	 */
	void closeOpenTimesheets();
}
