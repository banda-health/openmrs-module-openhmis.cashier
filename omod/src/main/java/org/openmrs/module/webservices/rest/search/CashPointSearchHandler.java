package org.openmrs.module.webservices.rest.search;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.web.CashierRestConstants;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.webservices.rest.resource.AlreadyPagedWithLength;
import org.openmrs.module.webservices.rest.resource.PagingUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CashPointSearchHandler implements SearchHandler {
    private final SearchConfig searchConfig = new SearchConfig("default", CashierRestConstants.CASH_POINT_RESOURCE, Arrays.asList("1.9.*"),
            Arrays.asList(
                    new SearchQuery.Builder("Find a cashpoint by its name, optionally filtering by location")
                            .withRequiredParameters("q")
                            .withOptionalParameters("location_uuid").build()
            )
    );

    @Override
    public PageableResult search(RequestContext context) throws ResponseException {
        String query = context.getParameter("q");
        String location_uuid = context.getParameter("location_uuid");
        query = query.isEmpty() ? null : query;
        location_uuid = StringUtils.isEmpty(location_uuid) ? null : location_uuid;

        ICashPointService service = Context.getService(ICashPointService.class);
        LocationService locationService = Context.getLocationService();
        Location location = locationService.getLocationByUuid(location_uuid);
        PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

        List<CashPoint> cashpoints = null;
        PageableResult results = null;

        if (location_uuid == null) {
            // Do a name search
            cashpoints = service.getByNameFragment(query, context.getIncludeAll(), pagingInfo);
        } else if (query == null) {
            //performs the location search
            cashpoints = service.getCashPointsByLocation(location, context.getIncludeAll(), pagingInfo);
        } else {
            // Do a name & location search
            cashpoints = service.getCashPointsByLocationAndName(location, query, context.getIncludeAll(), pagingInfo);
        }

        results = new AlreadyPagedWithLength<CashPoint>(context, cashpoints, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
        return results;
    }

    @Override
    public SearchConfig getSearchConfig() {
        return searchConfig;
    }
}
