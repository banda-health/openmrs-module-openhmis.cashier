package org.openmrs.module.webservices.rest.resource;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.IEntityService;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.resource.impl.ServiceSearcher;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

public abstract class BaseRestDataResource<E extends BaseOpenmrsData> extends DataDelegatingCrudResource<E> implements IDataServiceResource<E> {

	@Override
	public abstract E newDelegate();

	
	@Override
	public E save(E delegate) {
		IDataService<E> service = Context.getService(getServiceClass());
		service.save(delegate);
		return delegate;
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("display");
		description.addProperty("voided");
		return description;
	}

	@Override
	public E getByUniqueId(String uniqueId) {
		IEntityService<E> service = Context.getService(getServiceClass());
		E entity = service.getByUuid(uniqueId);
		return entity;
	}

	@Override
	protected void delete(E delegate, String reason, RequestContext context)
			throws ResponseException {
		IDataService<E> service = Context.getService(getServiceClass());
		service.voidEntity(delegate, reason);
	}

	@Override
	public void purge(E delegate, RequestContext context)
			throws ResponseException {
		IDataService<E> service = Context.getService(getServiceClass());
		service.purge(delegate);		
	}
	
	@Override
	protected NeedsPaging<E> doGetAll(RequestContext context) throws ResponseException {
		IDataService<E> service = Context.getService(getServiceClass());
		return new NeedsPaging<E>(service.getAll(), context);
	}

	@Override
	protected AlreadyPaged<E> doSearch(String query, RequestContext context) {
		return new ServiceSearcher<E>(IItemService.class, "getResources", "getCountOfResources").search(query,
               context);
	}
}
