package org.openmrs.module.webservices.rest.resource;

import java.util.ArrayList;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SubResource(parent = BillResource.class, path = "payment")
@Handler(supports = Payment.class, order = 0)
public class PaymentResource extends DelegatingSubResource<Payment, Bill, BillResource> {

	@Override
	public Payment newDelegate() {
		return new Payment();
	}

	@Override
	public Payment save(Payment delegate) {
		IBillService service = Context.getService(IBillService.class);
		service.save(delegate.getBill());
		return delegate;
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
		}
		else if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("paymentMode");
			description.addProperty("attributes");
			description.addProperty("amount");
		}
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("paymentMode");
		description.addProperty("attributes");
		description.addProperty("amount");		
		return description;
	}


	@Override
	public Bill getParent(Payment instance) {
		return instance.getBill();
	}

	@Override
	public void setParent(Payment instance, Bill parent) {
		instance.setBill(parent);
	}

	//TODO: Fix improper paging
	@Override
	public PageableResult doGetAll(Bill parent, RequestContext context)
			throws ResponseException {
		AlreadyPaged<Payment> results = new AlreadyPaged<Payment>(context, new ArrayList<Payment>(parent.getPayments()), false);
		return results;
	}

	@Override
	public Payment getByUniqueId(String uniqueId) {		
		return null;
	}

	@Override
	protected void delete(Payment delegate, String reason,
			RequestContext context) throws ResponseException {
		IBillService service = Context.getService(IBillService.class);
		delegate.setVoided(true);
		delegate.setVoidReason(reason);
		delegate.setVoidedBy(Context.getAuthenticatedUser());
		service.save(delegate.getBill());
	}

	@Override
	public void purge(Payment delegate, RequestContext context)
			throws ResponseException {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = delegate.getBill();
		bill.removePayment(delegate);
		service.save(bill);
	}
}
