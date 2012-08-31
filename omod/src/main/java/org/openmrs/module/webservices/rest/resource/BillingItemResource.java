package org.openmrs.module.webservices.rest.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IItemService;
import org.openmrs.module.openhmis.cashier.api.model.Item;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.MetadataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.resource.impl.ServiceSearcher;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource("billingitem")
@Handler(supports = { Item.class }, order = 0)
public class BillingItemResource extends MetadataDelegatingCrudResource<Item> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("retired");
		return description;
	}

	@Override
	public Item newDelegate() {
		return new Item();
	}

	@Override
	public Item save(Item item) {
		IItemService service = Context.getService(IItemService.class);
		service.save(item);
		return item;
	}

	@Override
	public Item getByUniqueId(String uuid) {
		IItemService service = Context.getService(IItemService.class);
		Item item = service.getByUuid(uuid);
		return item;
	}

	@Override
	public void purge(Item item, RequestContext context) throws ResponseException {
		IItemService service = Context.getService(IItemService.class);
		service.purge(item);
	}
	
	@Override
	protected NeedsPaging<Item> doGetAll(RequestContext context) throws ResponseException {
		IItemService service = Context.getService(IItemService.class);
		return new NeedsPaging<Item>(service.getAll(false), context);
	}

	@Override
	protected AlreadyPaged<Item> doSearch(String query, RequestContext context) {
		return new ServiceSearcher<Item>(IItemService.class, "getItems", "getCountOfItems").search(query,
               context);
	}
}
