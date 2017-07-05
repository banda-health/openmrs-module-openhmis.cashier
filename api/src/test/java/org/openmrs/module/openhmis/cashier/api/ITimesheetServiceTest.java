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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataServiceTest;

public class ITimesheetServiceTest extends IEntityDataServiceTest<ITimesheetService, Timesheet> {
	private ProviderService providerService;
	private ICashPointService cashPointService;

	public static final String TIMESHEET_DATASET = TestConstants.BASE_DATASET_DIR + "TimesheetTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		providerService = Context.getProviderService();
		cashPointService = Context.getService(ICashPointService.class);

		executeDataSet(TestConstants.CORE_DATASET);
		executeDataSet(ICashPointServiceTest.CASH_POINT_DATASET);
		executeDataSet(TIMESHEET_DATASET);
	}

	@Override
	public Timesheet createEntity(boolean valid) {
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
		return 8;
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
	public void getCurrentTimesheet_shouldReturnTheMostRecentTimesheetIfTheCashierIsClockedIntoMultipleTimesheets()
	        throws Exception {
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

	/**
	 * @verifies return empty list if there are no timesheets for date
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnEmptyListIfThereAreNoTimesheetsForDate() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 0, 1).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(0, results.size());
	}

	/**
	 * @verifies return timesheets that start and end on date
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnTimesheetsThatStartAndEndOnDate() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 1, 10).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(3, (int)results.get(0).getId());
	}

	/**
	 * @verifies return timesheets that start on date and end on different date
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnTimesheetsThatStartOnDateAndEndOnDifferentDate() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 1, 11).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(4, (int)results.get(0).getId());
	}

	/**
	 * @verifies return timesheet that start on different date and end on date
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnTimesheetThatStartOnDifferentDateAndEndOnDate() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 1, 14).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(5, (int)results.get(0).getId());
	}

	/**
	 * @verifies return timesheets that start before date but end after date
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnTimesheetsThatStartBeforeDateButEndAfterDate() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 1, 16).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(6, (int)results.get(0).getId());
	}

	/**
	 * @verifies return timesheets that start before date and have not ended
	 * @see ITimesheetService#getTimesheetsByDate(org.openmrs.Provider, java.util.Date)
	 */
	@Test
	public void getTimesheetsByDate_shouldReturnTimesheetsThatStartBeforeDateAndHaveNotEnded() throws Exception {
		Provider cashier = providerService.getProvider(0);
		List<Timesheet> results = service.getTimesheetsByDate(cashier, new GregorianCalendar(2011, 1, 20).getTime());

		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(7, (int)results.get(0).getId());
	}
}
