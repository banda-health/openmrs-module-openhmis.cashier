package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.IMetadataService;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.openhmis.cashier.api.model.ItemCode;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SubResource(parent = ItemResource.class, path = "code")
@Handler(supports = ItemCode.class, order = 0)
public class ItemCodeResource extends DelegatingSubResource<ItemCode, Item, ItemResource> implements IMetadataServiceResource<ItemCode> {

	@Override
	public ItemCode newDelegate() {
		return new ItemCode();
	}

	@Override
	public ItemCode save(ItemCode delegate) {
		IMetadataService<ItemCode> service = Context.getService(getServiceClass());
		return service.save(delegate);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("codes");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("code");
		return description;
	}

	@Override
	public Item getParent(ItemCode instance) {
		return instance.getItem();
	}

	@Override
	public void setParent(ItemCode instance, Item parent) {
		instance.setItem(parent);
	}

	@Override
	public PageableResult doGetAll(Item parent, RequestContext context)
			throws ResponseException {
		IMetadataService<ItemCode> service = Context.getService(getServiceClass());
		return new NeedsPaging<ItemCode>(service.getAll(), context);
	}

	@Override
	public ItemCode getByUniqueId(String uniqueId) {
		IMetadataService<ItemCode> service = Context.getService(getServiceClass());
		return service.getByUuid(uniqueId);
	}

	@Override
	protected void delete(ItemCode delegate, String reason,
			RequestContext context) throws ResponseException {
		IMetadataService<ItemCode> service = Context.getService(getServiceClass());
		service.retire(delegate, reason);
	}

	@Override
	public void purge(ItemCode delegate, RequestContext context)
			throws ResponseException {
		IMetadataService<ItemCode> service = Context.getService(getServiceClass());
		service.purge(delegate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IMetadataService<ItemCode>> getServiceClass() {
		return (Class<IMetadataService<ItemCode>>)(Object)IItemService.class;
	}
}
