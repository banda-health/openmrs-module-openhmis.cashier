/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.OpenmrsMetadata;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;

import java.util.List;

public class MetadataSearcher<E extends OpenmrsMetadata> {
	protected IMetadataService<E> service;
	
	public MetadataSearcher(Class<? extends IMetadataService<E>> serviceClass) {
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
