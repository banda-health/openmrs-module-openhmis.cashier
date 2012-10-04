package org.openmrs.module.webservices.rest.resource;

import java.util.Collection;
import java.util.List;

import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.ICashPointService;
import org.openmrs.module.openhmis.cashier.api.IDataService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.BillLineItem;
import org.openmrs.module.openhmis.cashier.api.model.BillStatus;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
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
				// Temporary test code; eventually we may get cash point from user properties
				ICashPointService cashPointService = Context.getService(ICashPointService.class);
				List<CashPoint> cashPointList = cashPointService.getAll();
				if (cashPointList.isEmpty())
					throw new RestClientException("No cash points exist!");
				delegate.setCashPoint(cashPointList.get(0));
			}
			if (delegate.getStatus() == null) {
				// If the total amount paid is greater than or equal to the bill total
				if (delegate.getTotalPaid().compareTo(delegate.getTotal()) >= 0)
					delegate.setStatus(BillStatus.PAID);
				else
					delegate.setStatus(BillStatus.PENDING);
			}
		}
		for (BillLineItem line : delegate.getLineItems())
			line.setBill(delegate);
		return super.save(delegate);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("adjustedBy");
			description.addProperty("billAdjusted");
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
		description.addProperty("lineItems");
		description.addProperty("patient");
		description.addProperty("payments");
		description.addProperty("receiptNumber");
		return description;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<IDataService<Bill>> getServiceClass() {
		return (Class<IDataService<Bill>>)(Object)IBillService.class;
	}

	@Override
	public Bill newDelegate() {
		return new Bill();
	}

}
