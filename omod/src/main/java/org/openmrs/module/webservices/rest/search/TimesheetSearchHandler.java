package org.openmrs.module.webservices.rest.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.Provider;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.commons.api.ProviderHelper;
import org.openmrs.module.webservices.rest.resource.AlreadyPagedWithLength;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.stereotype.Component;

@Component
public class TimesheetSearchHandler implements SearchHandler {
	private final SearchConfig searchConfig = new SearchConfig("default", RestConstants.VERSION_2 + "/cashier/timesheet", Arrays.asList("1.9.*"),
			new SearchQuery.Builder("Find a timesheet by date")
				.withRequiredParameters("date").build()
	);

	@Override
	public PageableResult search(RequestContext context)
			throws ResponseException {
		ITimesheetService service = Context.getService(ITimesheetService.class);
		Provider provider = ProviderHelper.getCurrentProvider();
		Date date;
		if (provider == null)
			return null;
		try {
			date = SimpleDateFormat.getDateInstance().parse(context.getParameter("date"));
		} catch (ParseException e) {
			throw new APIException("Invalid date parameter: " + context.getParameter("date"));
		}
		List<Timesheet> timesheets = service.getTimesheetsByDate(provider, date);
		PageableResult results = new AlreadyPagedWithLength<Timesheet>(context, timesheets, false, timesheets.size());
		return results;
	}

	@Override
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}
}
