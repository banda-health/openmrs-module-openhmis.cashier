package org.openmrs.module.webservices.rest.resource;

import java.util.List;

import org.openmrs.OpenmrsMetadata;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;

public class MetadataSearcher<E extends OpenmrsMetadata> {
	private IMetadataService<E> service;
	
	public MetadataSearcher(Class<IMetadataService<E>> serviceClass) {
		this.service = Context.getService(serviceClass);
	}
	
	public AlreadyPaged<E> searchByName(String nameFragment, RequestContext context) {
		Integer page = (context.getStartIndex() / context.getLimit()) + 1;
		PagingInfo pagingInfo = new PagingInfo(page, context.getLimit());
		List<E> results = service.findByName(nameFragment, context.getIncludeAll(), pagingInfo);
		Boolean hasMoreResults = (page * pagingInfo.getPageSize()) < pagingInfo.getTotalRecordCount();
		return new AlreadyPaged<E>(context, results, hasMoreResults);
	}
}
