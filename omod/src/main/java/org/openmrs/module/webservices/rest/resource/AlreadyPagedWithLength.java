package org.openmrs.module.webservices.rest.resource;

import java.util.List;

import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

public class AlreadyPagedWithLength<T> extends AlreadyPaged<T> {
	private long length = 0;

	public AlreadyPagedWithLength(RequestContext context, List<T> results, boolean hasMoreResults, long length) {
		super(context, results, hasMoreResults);
		this.length = length;
	}
	
	@Override
	public SimpleObject toSimpleObject() throws ResponseException {
		SimpleObject obj = super.toSimpleObject();
		obj.add("length", this.length);
		return obj;
	}

}
