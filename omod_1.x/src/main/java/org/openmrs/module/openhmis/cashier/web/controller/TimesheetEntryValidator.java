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
package org.openmrs.module.openhmis.cashier.web.controller;

import java.util.Date;

import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the Timesheet entry. Implents {@link Validator}
 */
public class TimesheetEntryValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return Timesheet.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Timesheet timesheet = (Timesheet)target;

		if (timesheet.getClockIn() == null) {
			errors.rejectValue("clockIn", "openhmis.cashier.timesheet.entry.error.clockIn.empty");
		} else if (timesheet.getClockIn().after(new Date())) {
			errors.rejectValue("clockIn", "openhmis.cashier.timesheet.entry.error.clockIn.future");
		}

		if (timesheet.getClockOut() != null && timesheet.getClockOut().after(new Date())) {
			errors.rejectValue("clockOut", "openhmis.cashier.timesheet.entry.error.clockOut.future");
		}

		if (timesheet.getClockOut() != null && timesheet.getClockOut().before(timesheet.getClockIn())) {
			errors.rejectValue("clockOut", "openhmis.cashier.timesheet.entry.error.clockOut.before.clockIn");
		}
	}
}
