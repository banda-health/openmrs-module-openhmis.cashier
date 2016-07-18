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
package org.openmrs.module.openhmis.cashier.api.search;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.commons.api.entity.search.BaseDataTemplateSearch;

/**
 * A search template class for the {@link Bill} model.
 */
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
