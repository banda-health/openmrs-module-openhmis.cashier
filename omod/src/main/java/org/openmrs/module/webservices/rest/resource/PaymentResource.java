package org.openmrs.module.webservices.rest.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.response.ConversionException;
import org.openmrs.module.webservices.rest.web.response.ObjectNotFoundException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SubResource(parent = BillResource.class, path = "payment")
@Handler(supports = Payment.class, order = 0)
public class PaymentResource extends DelegatingSubResource<Payment, Bill, BillResource> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(
			Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("uuid");
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("paymentMode");
			description.addProperty("attributes");
			description.addProperty("amount");
			description.addProperty("dateCreated");
			description.addProperty("voided");
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
	
	@PropertySetter("attributes")
	public void setPaymentAttributes(Payment instance, Set<PaymentAttribute> attributes) {
		if (instance.getAttributes() == null)
			instance.setAttributes(new HashSet<PaymentAttribute>());
		BaseRestDataResource.updateCollection(instance.getAttributes(), attributes);
		for (PaymentAttribute attr: instance.getAttributes())
			attr.setPayment(instance);
	}

	@PropertySetter("amount")
	public void setPaymentAmount(Payment instance, Object price) throws ConversionException {
		instance.setAmount(ItemPriceResource.objectToBigDecimal(price));
	}
	
	@PropertyGetter("dateCreated")
	public String getPaymentDate(Payment instance) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return format.format(instance.getDateCreated());
	}

	@Override
	public Payment save(Payment delegate) {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = delegate.getBill();
		bill.addPayment(delegate);
		service.save(bill);
		return delegate;
	}

	@Override
	public void delete(String parentUniqueId, String uuid, String reason, RequestContext context) throws ResponseException {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = service.getByUuid(parentUniqueId);
		try {
			Map<String, Payment> map = BaseRestDataResource.mapUuidToObject(bill.getPayments());
			Payment payment = map.get(uuid);
			payment.setVoided(true);
			payment.setVoidReason(reason);
			payment.setVoidedBy(Context.getAuthenticatedUser());
			service.save(bill);
		}
		catch (Exception e) { throw new ObjectNotFoundException(); }
	}
	
	@Override
	protected void delete(Payment delegate, String reason, RequestContext context) throws ResponseException {
		delete(delegate.getBill().getUuid(), delegate.getUuid(), reason, context);
	}

	@Override
	public void purge(String parentUniqueId, String uuid, RequestContext context) throws ResponseException {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = service.getByUuid(parentUniqueId);
		try {
			Map<String, Payment> map = BaseRestDataResource.mapUuidToObject(bill.getPayments());
			Payment payment = map.get(uuid);
			bill.removePayment(payment);
			service.save(bill);
		}
		catch (Exception e) { throw new ObjectNotFoundException(); }
	}
	
	@Override
	public void purge(Payment delegate, RequestContext context) throws ResponseException {
		purge(delegate.getBill().getUuid(), delegate.getUuid(), context);
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
	public Bill getParent(Payment instance) {
		return instance.getBill();
	}

	@Override
	public void setParent(Payment instance, Bill parent) {
		instance.setBill(parent);
	}

	@Override
	public Payment newDelegate() {
		return new Payment();
	}
}
