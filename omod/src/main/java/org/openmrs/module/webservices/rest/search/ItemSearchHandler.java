package org.openmrs.module.webservices.rest.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IDepartmentService;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.resource.AlreadyPagedWithLength;
import org.openmrs.module.webservices.rest.resource.MetadataSearcher;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.stereotype.Component;

@Component
public class ItemSearchHandler implements SearchHandler {
	private final SearchConfig searchConfig = new SearchConfig("default", RestConstants.VERSION_2 + "/cashier/item", Arrays.asList("1.9.*"),
			Arrays.asList(
				new SearchQuery.Builder("Find an item by its name or code, optionally filtering by department")
					.withRequiredParameters("q")
					.withOptionalParameters("department_uuid").build()
			)
		);
	
	@Override
	public PageableResult search(RequestContext context) throws ResponseException {
		String query = context.getParameter("q");
		String department_uuid = context.getParameter("department_uuid");
		query = query.isEmpty() ? null : query;
		department_uuid = (department_uuid == null || department_uuid.isEmpty()) ? null : department_uuid;
		IItemService service = (IItemService) Context.getService(IItemService.class);
		// Try searching by code
		SimpleObject resultByCode = searchByCode(query, context, service);
		if (resultByCode != null)
			return new AlreadyPaged<SimpleObject>(context, Arrays.asList(resultByCode), false);

		if (department_uuid == null) {
			// Do a name search
			PagingInfo pagingInfo = MetadataSearcher.getPagingInfoFromContext(context);
			List<Item> items = service.findByName(query, context.getIncludeAll(), pagingInfo);
			AlreadyPagedWithLength<Item> results = new AlreadyPagedWithLength<Item>(context, items, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
			return results;
		}
		else {
			IDepartmentService deptService = (IDepartmentService) Context.getService(IDepartmentService.class);
			Department department = deptService.getByUuid(department_uuid);
			// Get all items in the department if no name query is given
			if (query == null)
				return searchByDepartment(department_uuid, context);
			// Do a name + department search
			PagingInfo pagingInfo = MetadataSearcher.getPagingInfoFromContext(context);
			List<Item> items = service.findItems(department, query, context.getIncludeAll(), pagingInfo);
			PageableResult results = new AlreadyPagedWithLength<Item>(context, items, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
			return results;
		}
	}
 	
	protected SimpleObject searchByCode(String query, RequestContext context, IItemService service) throws ResponseException {
		if (query == null) return null;
		if (service == null) service = (IItemService) Context.getService(IItemService.class);
		Item itemByCode = service.getItemByCode(query);
		if (itemByCode != null) {
			List<Item> list = new ArrayList<Item>(1);
			list.add(itemByCode);
			return new AlreadyPaged<Item>(context, list, false).toSimpleObject();
		}
		return null;
	}
	
	public PageableResult searchByDepartment(String department_uuid, RequestContext context) throws ResponseException {
		IItemService service = (IItemService) Context.getService(IItemService.class);
		IDepartmentService deptService = (IDepartmentService) Context.getService(IDepartmentService.class);
		Department department = deptService.getByUuid(department_uuid);
		
		PagingInfo pagingInfo = MetadataSearcher.getPagingInfoFromContext(context);
		List<Item> items = service.getItemsByDepartment(department, context.getIncludeAll(), pagingInfo);
		PageableResult results = new AlreadyPagedWithLength<Item>(context, items, pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
		return results;
	}
					
	@Override
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}
}
