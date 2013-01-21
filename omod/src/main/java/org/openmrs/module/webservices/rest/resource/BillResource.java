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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.annotation.Handler;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.cashier.api.model.BillStatus;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.springframework.web.client.RestClientException;

@Resource("bill")
@Handler(supports = { Bill.class }, order = 0)
public class BillResource extends BaseRestDataResource<Bill> {
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("adjustedBy", Representation.REF);
			description.addProperty("billAdjusted", Representation.REF);
			description.addProperty("cashPoint");
			description.addProperty("cashier", Representation.REF);
			description.addProperty("lineItems");
			description.addProperty("patient");
			description.addProperty("payments");
			description.addProperty("receiptNumber");
			description.addProperty("status");
		}
		return description;
	}

	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("adjustedBy");
		description.addProperty("billAdjusted");
		description.addProperty("cashPoint");		
		description.addProperty("lineItems");
		description.addProperty("patient");
		description.addProperty("payments");
		description.addProperty("receiptNumber");
		description.addProperty("status");		
		return description;
	}

	@PropertySetter("lineItems")
	public void setBillLineItems(Bill instance, List<BillLineItem> lineItems) {
		if (instance.getLineItems() == null)
			instance.setLineItems(new ArrayList<BillLineItem>(lineItems.size()));
		BaseRestDataResource.updateCollection(instance.getLineItems(), lineItems);
		for (BillLineItem item: instance.getLineItems())
			item.setBill(instance);
	}

	@PropertySetter("payments")
	public void setBillPayments(Bill instance, Set<Payment> payments) {
		if (instance.getPayments() == null)
			instance.setPayments(new HashSet<Payment>(payments.size()));
		BaseRestDataResource.updateCollection(instance.getPayments(), payments);
		for (Payment payment: instance.getPayments())
			instance.addPayment(payment);
	}
	
	@PropertySetter("billAdjusted")
	public void setBillAdjusted(Bill instance, Bill billAdjusted) {
		billAdjusted.addAdjustedBy(instance);
		instance.setBillAdjusted(billAdjusted);
	}
	
	@PropertySetter("status")
	public void setBillStatus(Bill instance, BillStatus status) {
		if (instance.getStatus() == null)
			instance.setStatus(status);
		else if (instance.getStatus() == BillStatus.PENDING && status == BillStatus.POSTED)
			instance.setStatus(status);
	}

	@Override
	public Bill save (Bill delegate) {
		//TODO: Test all the ways that this could fail
		if (delegate.getId() == null) {
			if (delegate.getCashier() == null) {
				User currentUser = Context.getAuthenticatedUser();
				ProviderService service = Context.getProviderService();
				Collection<Provider> providers = service.getProvidersByPerson(currentUser.getPerson());
				for (Provider provider : providers) { delegate.setCashier(provider); break; }
				if (delegate.getCashier() == null)
					throw new RestClientException("Couldn't find Provider for the current user (" + currentUser.getName() + ")");
			}
			if (delegate.getCashPoint() == null) {
				ITimesheetService service = Context.getService(ITimesheetService.class);
				Timesheet timesheet = service.getCurrentTimesheet(delegate.getCashier());
				if (timesheet == null) {
					AdministrationService adminService = Context.getAdministrationService();
					boolean timesheetRequired;
					try {
						timesheetRequired = Boolean.parseBoolean(adminService.getGlobalProperty(CashierWebConstants.TIMESHEET_REQUIRED_PROPERTY));
					} catch (Exception e) {
						timesheetRequired = false;
					}
					if (timesheetRequired)
						throw new RestClientException("A current timesheet does not exist for cashier " + delegate.getCashier());
					// If this is an adjusting bill, copy cash point from billAdjusted
					else if (delegate.getBillAdjusted() != null)
						delegate.setCashPoint(delegate.getBillAdjusted().getCashPoint());
					else
						throw new RestClientException("Cash point cannot be null!");
				}
				else {
					CashPoint cashPoint = timesheet.getCashPoint();
					if (cashPoint == null)
						throw new RestClientException("No cash points defined for the current timesheet!");
					delegate.setCashPoint(cashPoint);					
				}
			}
			// Now that all all attributes have been set (i.e., payments and
			// bill status) we can check to see if the bill is fully paid.
			delegate.checkPaidAndUpdateStatus();
			if (delegate.getStatus() == null)
				delegate.setStatus(BillStatus.PENDING);
		}
		return super.save(delegate);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<IDataService<Bill>> getServiceClass() {
		return (Class<IDataService<Bill>>)(Object)IBillService.class;
	}

	public String getDisplayString(Bill instance) {
		return instance.getReceiptNumber();
	}
	
	@Override
	public Bill newDelegate() {
		return new Bill();
	}

}
