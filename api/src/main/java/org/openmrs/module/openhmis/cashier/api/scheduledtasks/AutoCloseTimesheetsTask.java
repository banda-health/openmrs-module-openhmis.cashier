package org.openmrs.module.openhmis.cashier.api.scheduledtasks;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;

/**
 * A scheduled task that automatically closes all open timesheets
 */
/**
 * Created by pochu on 4/25/2014.
 */

    public class AutoCloseTimesheetsTask extends AbstractTask {

        private static final Log log = LogFactory.getLog(AutoCloseTimesheetsTask.class);
        private ITimesheetService timesheetService;


        /**
         * @see org.openmrs.scheduler.tasks.AbstractTask#execute()
         */
        @Override
        public void execute() {
            if (!isExecuting) {
                if (log.isDebugEnabled())
                    log.debug("Starting Auto Close Timesheets Task...");


                startExecuting();
                try {

                    this.timesheetService = Context.getService(ITimesheetService.class);
                    this.timesheetService.closeOpenTimesheets();

                }
                catch (Exception e) {
                    log.error("Error while auto closing open timesheets:", e);
                }
                finally {
                    stopExecuting();
                }
            }
        }
    }
