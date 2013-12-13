package org.openmrs.module.openhmis.cashier.api.search;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.commons.api.entity.search.BaseDataTemplateSearch;

public class BillSearch extends BaseDataTemplateSearch<Bill> {

	public BillSearch() {
		this(new Bill(), false);
	}
	
	public BillSearch(Bill template) {
		this(template, false);
	}

	public BillSearch(Bill template, Boolean includeRetired) {
		super(template, includeRetired);
	}

	@Override
	public void updateCriteria(Criteria criteria) {
		super.updateCriteria(criteria);

		Bill bill = getTemplate();
		if (bill.getCashier() != null) {
			criteria.add(Restrictions.eq("cashier", bill.getCashier()));
		}
		if (bill.getCashPoint() != null) {
			criteria.add(Restrictions.eq("cashPoint", bill.getCashPoint()));
		}
		if (bill.getPatient() != null) {
			criteria.add(Restrictions.eq("patient", bill.getPatient()));
		}
		if (bill.getStatus() != null) {
			criteria.add(Restrictions.eq("status", bill.getStatus()));
		}
	}
}
