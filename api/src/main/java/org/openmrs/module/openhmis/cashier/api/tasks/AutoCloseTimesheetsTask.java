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
			if (LOG.isDebugEnabled())
				LOG.debug("Starting Auto Close Timesheets Task...");

			startExecuting();

			try {
				ITimesheetService timesheetService = Context.getService(ITimesheetService.class);

				timesheetService.closeOpenTimesheets();
			}
			catch (Exception e) {
				LOG.error("Error while auto closing open timesheets:", e);
			} finally {
				stopExecuting();
			}
		}
	}
}
