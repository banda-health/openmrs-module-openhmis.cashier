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
package org.openmrs.module.openhmis.cashier.api.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * A scheduled task that automatically closes all open timesheets
 */
public class AutoCloseTimesheetsTask extends AbstractTask {
	private static final Log LOG = LogFactory.getLog(AutoCloseTimesheetsTask.class);

	@Override
	public void execute() {
		if (!isExecuting) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting Auto Close Timesheets Task...");
			}

			startExecuting();

			try {
				ITimesheetService timesheetService = Context.getService(ITimesheetService.class);

				timesheetService.closeOpenTimesheets();
			} catch (Exception e) {
				LOG.error("Error while auto closing open timesheets:", e);
			} finally {
				stopExecuting();
			}
		}
	}
}
