package org.openmrs.module.webservices.rest.resource;

import java.util.List;

import org.openmrs.OpenmrsMetadata;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;

public class MetadataSearcher<E extends OpenmrsMetadata> {
	protected IMetadataService<E> service;
	
	public MetadataSearcher(Class<IMetadataService<E>> serviceClass) {
		this.service = Context.getService(serviceClass);
	}
	
	public AlreadyPaged<E> searchByName(String nameFragment, RequestContext context) {
		PagingInfo pagingInfo = getPagingInfoFromContext(context);
		List<E> results = service.findByName(nameFragment, context.getIncludeAll(), pagingInfo);
		Boolean hasMoreResults = (pagingInfo.getPage() * pagingInfo.getPageSize()) < pagingInfo.getTotalRecordCount();
		return new AlreadyPaged<E>(context, results, hasMoreResults);
	}
	
	public static PagingInfo getPagingInfoFromContext(RequestContext context) {
		Integer page = (context.getStartIndex() / context.getLimit()) + 1;
		return new PagingInfo(page, context.getLimit());
	}
}
