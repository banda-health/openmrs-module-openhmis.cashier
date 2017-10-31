/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.resource;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.IPaymentModeService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.openhmis.cashier.api.model.PaymentAttribute;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;
import org.openmrs.module.webservices.rest.helper.Converter;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubResource;
import org.openmrs.module.webservices.rest.web.response.ObjectNotFoundException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * REST resource representing a {@link Payment}.
 */
@SubResource(parent = BillResource.class, path = "payment", supportedClass = Payment.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class PaymentResource extends DelegatingSubResource<Payment, Bill, BillResource> {
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");

		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("instanceType", Representation.REF);
			description.addProperty("attributes");
			description.addProperty("amount");
			description.addProperty("amountTendered");
			description.addProperty("dateCreated");
			description.addProperty("voided");
		}

		return description;
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("instanceType");
		description.addProperty("attributes");
		description.addProperty("amount");
		description.addProperty("amountTendered");

		return description;
	}

	// Work around TypeVariable issue on base generic property (BaseCustomizableInstanceData.getInstanceType)
	@PropertySetter("instanceType")
	public void setPaymentMode(Payment instance, String uuid) {
		IPaymentModeService service = Context.getService(IPaymentModeService.class);

		PaymentMode mode = service.getByUuid(uuid);
		if (mode == null) {
			throw new ObjectNotFoundException();
		}

		instance.setInstanceType(mode);
	}

	@PropertySetter("attributes")
	public void setPaymentAttributes(Payment instance, Set<PaymentAttribute> attributes) {
		if (instance.getAttributes() == null) {
			instance.setAttributes(new HashSet<PaymentAttribute>());
		}

		BaseRestDataResource.syncCollection(instance.getAttributes(), attributes);
		for (PaymentAttribute attr : instance.getAttributes()) {
			attr.setOwner(instance);
		}
	}

	@PropertySetter("amount")
	public void setPaymentAmount(Payment instance, Object price) {
		instance.setAmount(Converter.objectToBigDecimal(price));
	}

	@PropertySetter("amountTendered")
	public void setPaymentAmountTendered(Payment instance, Object price) {
		instance.setAmountTendered(Converter.objectToBigDecimal(price));
	}

	@PropertyGetter("dateCreated")
	public Long getPaymentDate(Payment instance) {
		return instance.getDateCreated().getTime();
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
	protected void delete(Payment delegate, String reason, RequestContext context) {
		delete(delegate.getBill().getUuid(), delegate.getUuid(), reason, context);
	}

	@Override
	public void delete(String parentUniqueId, final String uuid, String reason, RequestContext context) {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = findBill(service, parentUniqueId);
		Payment payment = findPayment(bill, uuid);

		payment.setVoided(true);
		payment.setVoidReason(reason);
		payment.setVoidedBy(Context.getAuthenticatedUser());

		service.save(bill);
	}

	@Override
	public void purge(Payment delegate, RequestContext context) {
		purge(delegate.getBill().getUuid(), delegate.getUuid(), context);
	}

	@Override
	public void purge(String parentUniqueId, String uuid, RequestContext context) {
		IBillService service = Context.getService(IBillService.class);
		Bill bill = findBill(service, parentUniqueId);
		Payment payment = findPayment(bill, uuid);

		bill.removePayment(payment);
		service.save(bill);
	}

	@Override
	public PageableResult doGetAll(Bill parent, RequestContext context) {
		return new AlreadyPaged<Payment>(context, new ArrayList<Payment>(parent.getPayments()), false);
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

	private Bill findBill(IBillService service, String billUUID) {
		Bill bill = service.getByUuid(billUUID);
		if (bill == null) {
			throw new ObjectNotFoundException();
		}

		return bill;
	}

	private Payment findPayment(Bill bill, final String paymentUUID) {
		Payment payment = Iterators.tryFind(bill.getPayments().iterator(), new Predicate<Payment>() {
			@Override
			public boolean apply(@Nullable Payment input) {
				return input != null && input.getUuid().equals(paymentUUID);
			}
		}).orNull();

		if (payment == null) {
			throw new ObjectNotFoundException();
		}

		return payment;
	}
}
