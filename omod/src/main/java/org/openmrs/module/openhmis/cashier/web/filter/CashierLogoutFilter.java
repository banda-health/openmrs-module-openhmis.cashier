package org.openmrs.module.openhmis.cashier.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.commons.api.ProviderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashierLogoutFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(CashierLogoutFilter.class);
    private static final String PROVIDER_ERROR_MESSAGE = "Could not locate the Provider";

    @Autowired private ProviderService providerService;
    @Autowired private ITimesheetService timesheetService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.debug("doCashierLogoutFilter");
        clockOutCashier();
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug("initCashierLogoutFilter");
    }

    @Override
    public void destroy() {
        LOG.debug("destroyCashierLogoutFilter");
    }

    private void clockOutCashier() {

        if (userIsNotCashier()) {
            return;
        }

        Provider provider = ProviderHelper.getCurrentProvider(providerService);

        if (provider == null) {
            LOG.error(PROVIDER_ERROR_MESSAGE);
            throw new APIException(PROVIDER_ERROR_MESSAGE);
        }

        Timesheet timesheet = timesheetService.getCurrentTimesheet(provider);
        if (cashierIsClockedIn(timesheet)) {
            timesheet.setClockOut(new Date());
            timesheetService.save(timesheet);
        }
    }

    private boolean userIsNotCashier() {
        User authenticatedUser = Context.getAuthenticatedUser();
        return !authenticatedUser.hasPrivilege(CashierPrivilegeConstants.MANAGE_TIMESHEETS) || authenticatedUser.isSuperUser();
    }

    private boolean cashierIsClockedIn(Timesheet timesheet) {
        return timesheet != null && timesheet.getClockIn() != null;
    }

}
