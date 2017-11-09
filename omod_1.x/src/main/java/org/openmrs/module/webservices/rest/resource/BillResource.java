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

import com.google.common.collect.Iterators;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.cashier.api.model.BillStatus;
import org.openmrs.module.openhmis.cashier.api.model.Payment;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.cashier.api.util.RoundingUtil;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * REST resource representing a {@link Bill}.
 */
@Resource(name = RestConstants.VERSION_2 + "/cashier/bill", supportedClass = Bill.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class BillResource extends BaseRestDataResource<Bill> {
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (!(rep instanceof RefRepresentation)) {
			description.addProperty("adjustedBy", Representation.REF);
			description.addProperty("billAdjusted", Representation.REF);
			description.addProperty("cashPoint", Representation.REF);
			description.addProperty("cashier", Representation.REF);
			description.addProperty("dateCreated");
			description.addProperty("lineItems");
			description.addProperty("patient", Representation.REF);
			description.addProperty("payments", Representation.FULL);
			description.addProperty("receiptNumber");
			description.addProperty("status");
			description.addProperty("adjustmentReason");
			description.addProperty("id");
		}
		return description;
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		return getRepresentationDescription(new DefaultRepresentation());
	}

	@PropertySetter("lineItems")
	public void setBillLineItems(Bill instance, List<BillLineItem> lineItems) {
		if (instance.getLineItems() == null) {
			instance.setLineItems(new ArrayList<BillLineItem>(lineItems.size()));
		}
		BaseRestDataResource.syncCollection(instance.getLineItems(), lineItems);
		for (BillLineItem item : instance.getLineItems()) {
			item.setBill(instance);
		}
	}

	@PropertySetter("payments")
	public void setBillPayments(Bill instance, Set<Payment> payments) {
		if (instance.getPayments() == null) {
			instance.setPayments(new HashSet<Payment>(payments.size()));
		}
		BaseRestDataResource.syncCollection(instance.getPayments(), payments);
		for (Payment payment : instance.getPayments()) {
			instance.addPayment(payment);
		}
	}

	@PropertySetter("billAdjusted")
	public void setBillAdjusted(Bill instance, Bill billAdjusted) {
		billAdjusted.addAdjustedBy(instance);
		instance.setBillAdjusted(billAdjusted);
	}

	@PropertySetter("status")
	public void setBillStatus(Bill instance, BillStatus status) {
		if (instance.getStatus() == null) {
			instance.setStatus(status);
		} else if (instance.getStatus() == BillStatus.PENDING && status == BillStatus.POSTED) {
			instance.setStatus(status);
		}
		if (status == BillStatus.POSTED) {
			RoundingUtil.handleRoundingLineItem(instance);
		}
	}

	@PropertySetter("adjustmentReason")
	public void setAdjustReason(Bill instance, String adjustReason) {
		if (instance.getBillAdjusted().getUuid() != null) {
			instance.getBillAdjusted().setAdjustmentReason(adjustReason);
		}
	}

	@Override
	public Bill save(Bill bill) {
		//TODO: Test all the ways that this could fail

		if (bill.getId() == null) {
			if (bill.getCashier() == null) {
				Provider cashier = getCurrentCashier(bill);
				if (cashier == null) {
					throw new RestClientException("Couldn't find Provider for the current user ("
					        + Context.getAuthenticatedUser().getName() + ")");
				}

				bill.setCashier(cashier);
			}

			if (bill.getCashPoint() == null) {
				loadBillCashPoint(bill);
			}

			// Now that all all attributes have been set (i.e., payments and bill status) we can check to see if the bill
			// is fully paid.
			bill.checkPaidAndUpdateStatus();
			if (bill.getStatus() == null) {
				bill.setStatus(BillStatus.PENDING);
			}
		}

		return super.save(bill);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<IEntityDataService<Bill>> getServiceClass() {
		return (Class<IEntityDataService<Bill>>)(Object)IBillService.class;
	}

	public String getDisplayString(Bill instance) {
		return instance.getReceiptNumber();
	}

	@Override
	public Bill newDelegate() {
		return new Bill();
	}

	private Provider getCurrentCashier(Bill bill) {
		User currentUser = Context.getAuthenticatedUser();
		ProviderService service = Context.getProviderService();
		Collection<Provider> providers = service.getProvidersByPerson(currentUser.getPerson());

		return Iterators.get(providers.iterator(), 0, null);
	}

	private void loadBillCashPoint(Bill bill) {
		ITimesheetService service = Context.getService(ITimesheetService.class);
		Timesheet timesheet = service.getCurrentTimesheet(bill.getCashier());
		if (timesheet == null) {
			AdministrationService adminService = Context.getAdministrationService();
			boolean timesheetRequired;
			try {
				timesheetRequired =
				        Boolean.parseBoolean(adminService.getGlobalProperty(ModuleSettings.TIMESHEET_REQUIRED_PROPERTY));
			} catch (Exception e) {
				timesheetRequired = false;
			}

			if (timesheetRequired) {
				throw new RestClientException("A current timesheet does not exist for cashier " + bill.getCashier());
			} else if (bill.getBillAdjusted() != null) {
				// If this is an adjusting bill, copy cash point from billAdjusted
				bill.setCashPoint(bill.getBillAdjusted().getCashPoint());
			} else {
				throw new RestClientException("Cash point cannot be null!");
			}
		} else {
			CashPoint cashPoint = timesheet.getCashPoint();
			if (cashPoint == null) {
				throw new RestClientException("No cash points defined for the current timesheet!");
			}
			bill.setCashPoint(cashPoint);
		}
	}
}
