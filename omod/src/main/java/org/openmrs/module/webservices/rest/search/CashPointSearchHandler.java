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

/**
 * Created by benjamin on 7/18/14.
 */
@Component
public class CashPointSearchHandler implements SearchHandler {
    private final SearchConfig searchConfig = new SearchConfig("default", CashierRestConstants.CASH_POINT_RESOURCE, Arrays.asList("1.9.*"),
            Arrays.asList(
                    new SearchQuery.Builder("Find a cashpoints by its name, optionally filtering by location")
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

        if (location_uuid == null) {
            // Do a name search
            PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
            List<CashPoint> cashpoints = service.findByName(query, context.getIncludeAll(), pagingInfo);
            AlreadyPagedWithLength<CashPoint> results = new AlreadyPagedWithLength<CashPoint>(context, cashpoints, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
            return results;
        } else {
            //performs the location search
            LocationService locationService = Context.getLocationService();
            Location location = locationService.getLocationByUuid(location_uuid);
                if (query == null) {
                    return searchByLocation(location_uuid, context);
                }
            // Do a name + location search
            PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
            List<CashPoint> cashpoints = service.findCashPoints(location, query, context.getIncludeAll(), pagingInfo);
            PageableResult results = new AlreadyPagedWithLength<CashPoint>(context, cashpoints, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
            return results;
        }
    }

    public PageableResult searchByLocation(String location_uuid, RequestContext context) throws ResponseException {
        LocationService locationService = Context.getLocationService();
        Location location = locationService.getLocationByUuid(location_uuid);
        ICashPointService service = Context.getService(ICashPointService.class);

        PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
        List<CashPoint> cashpoints = service.getCashPointsByLocation(location, context.getIncludeAll(), pagingInfo);
        PageableResult results = new AlreadyPagedWithLength<CashPoint>(context, cashpoints, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
        return results;
    }

    @Override
    public SearchConfig getSearchConfig() {
        return searchConfig;
    }
}
