package org.openmrs.module.webservices.rest.resource;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.*;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

public abstract class BaseRestMetadataResource<E extends BaseOpenmrsMetadata> extends MetadataDelegatingCrudResource<E> {

	@Override
	public abstract E newDelegate();

	@Override
	public E save(E delegate) {
		@SuppressWarnings("unchecked")
		IMetadataService<E> service = (IMetadataService<E>) Context.getService(delegate.getClass());
		service.save(delegate);
		return delegate;
	}

	protected DelegatingResourceDescription getDefaultRepresentationDescription() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("display");
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("retired");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		return getDefaultRepresentationDescription();
	}

	@Override
	public E getByUniqueId(String uniqueId) {
		@SuppressWarnings("unchecked")
		IMetadataService<E> service = (IMetadataService<E>) Context.getService(newDelegate().getClass());
		E entity = service.getByUuid(uniqueId);
		return entity;
	}

	@Override
	public void purge(E delegate, RequestContext context)
			throws ResponseException {
		@SuppressWarnings("unchecked")
		IMetadataService<E> service = (IMetadataService<E>) Context.getService(delegate.getClass());
		service.purge(delegate);		
	}
	
	@Override
	protected NeedsPaging<E> doGetAll(RequestContext context) throws ResponseException {
		@SuppressWarnings("unchecked")
		IMetadataService<E> service = (IMetadataService<E>) Context.getService(newDelegate().getClass());
		return new NeedsPaging<E>(service.getAll(), context);
	}

	@Override
	protected AlreadyPaged<E> doSearch(String query, RequestContext context) {
		return new ServiceSearcher<E>(IItemService.class, "getResources", "getCountOfResources").search(query,
               context);
	}
}
