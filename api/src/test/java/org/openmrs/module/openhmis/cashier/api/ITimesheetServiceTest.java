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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;

import java.util.Calendar;

public class ITimesheetServiceTest extends IDataServiceTest<ITimesheetService, Timesheet> {
	private ProviderService providerService;
	private ICashPointService cashPointService;

	public static final String TIMESHEET_DATASET = BASE_DATASET_DIR + "TimesheetTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		providerService = Context.getProviderService();
		cashPointService = Context.getService(ICashPointService.class);

		executeDataSet(ICashPointServiceTest.CASH_POINT_DATASET);
		executeDataSet(CORE_DATASET);
		executeDataSet(TIMESHEET_DATASET);
	}

	@Override
	protected Timesheet createEntity(boolean valid) {
		Timesheet timesheet = new Timesheet();

		if (valid) {
			timesheet.setCashier(providerService.getProvider(0));
			timesheet.setCashPoint(cashPointService.getById(0));
		}

		// Holy crap, date stuff really sucks in Java... there must be a more sane library out there?
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR, 9);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		timesheet.setClockIn(cal.getTime());

		cal.add(Calendar.HOUR, 8);
		timesheet.setClockOut(cal.getTime());

		return timesheet;
	}

	@Override
	protected int getTestEntityCount() {
		return 3;
	}

	@Override
	protected void updateEntityFields(Timesheet entity) {
		entity.setCashier(providerService.getProvider(1));
		entity.setCashPoint(cashPointService.getById(1));

		Calendar cal = Calendar.getInstance();

		cal.setTime(entity.getClockIn());
		cal.add(Calendar.DAY_OF_MONTH, -10);
		entity.setClockIn(cal.getTime());

		if (entity.getClockOut() == null) {
			cal.setTime(entity.getClockIn());
			cal.add(Calendar.HOUR, 8);
		} else {
			cal.setTime(entity.getClockOut());
		}

		cal.add(Calendar.DAY_OF_MONTH, -10);
		entity.setClockOut(cal.getTime());
	}

	@Override
	protected void assertEntity(Timesheet expected, Timesheet actual) {
		super.assertEntity(expected, actual);

		Assert.assertNotNull(expected.getCashier());
		Assert.assertNotNull(actual.getCashier());
		Assert.assertEquals(expected.getCashier().getId(), actual.getCashier().getId());
		Assert.assertNotNull(expected.getCashPoint());
		Assert.assertNotNull(actual.getCashPoint());
		Assert.assertEquals(expected.getCashPoint().getId(), actual.getCashPoint().getId());

		Assert.assertEquals(expected.getClockIn(), actual.getClockIn());
		Assert.assertEquals(expected.getClockOut(), actual.getClockOut());
	}

	/**
	 * @verifies return the current timesheet for the cashier
	 * @see ITimesheetService#getCurrentTimesheet(org.openmrs.Provider)
	 */
	@Test
	public void getCurrentTimesheet_shouldReturnTheCurrentTimesheetForTheCashier() throws Exception {
		Timesheet timesheet = createEntity(true);
		timesheet.setClockOut(null);

		timesheet = service.save(timesheet);
		Context.flushSession();

		Timesheet current = service.getCurrentTimesheet(timesheet.getCashier());

		Assert.assertNotNull(current);
		assertEntity(timesheet, current);
	}

	/**
	 * @verifies return null if the cashier has no timesheets
	 * @see ITimesheetService#getCurrentTimesheet(org.openmrs.Provider)
	 */
	@Test
	public void getCurrentTimesheet_shouldReturnNullIfTheCashierHasNoTimesheets() throws Exception {
		Provider cashier = providerService.getProvider(2);
		Assert.assertNotNull(cashier);

		Timesheet timesheet = service.getCurrentTimesheet(cashier);
		Assert.assertNull(timesheet);
	}

	/**
	 * @verifies return the most recent timesheet if the cashier is clocked into multiple timesheets
	 * @see ITimesheetService#getCurrentTimesheet(org.openmrs.Provider)
	 */
	@Test
	public void getCurrentTimesheet_shouldReturnTheMostRecentTimesheetIfTheCashierIsClockedIntoMultipleTimesheets() throws Exception {
		Provider cashier = providerService.getProvider(0);
		Timesheet original = service.getCurrentTimesheet(cashier);

		Assert.assertNotNull(original);

		Timesheet timesheet = createEntity(true);
		timesheet.setCashier(cashier);
		timesheet.setClockOut(null);

		service.save(timesheet);
		Context.flushSession();

		Timesheet current = service.getCurrentTimesheet(cashier);
		Assert.assertNotNull(current);
		Assert.assertFalse(original.getId().equals(current.getId()));
	}

	/**
	 * @verifies return null if the timesheet is clocked out
	 * @see ITimesheetService#getCurrentTimesheet(org.openmrs.Provider)
	 */
	@Test
	public void getCurrentTimesheet_shouldReturnNullIfTheTimesheetIsClockedOut() throws Exception {
		Provider cashier = providerService.getProvider(1);
		Assert.assertNotNull(cashier);

		Timesheet timesheet = service.getCurrentTimesheet(cashier);
		Assert.assertNull(timesheet);
	}
}
