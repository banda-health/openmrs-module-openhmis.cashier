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
package org.openmrs.module.openhmis.cashier.api.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.model.GroupSequence;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.cashier.api.security.BasicEntityAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseObjectDataServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data service implementation class for {@link SequentialReceiptNumberGeneratorModel}s.
 */
@Transactional
public class SequentialReceiptNumberGeneratorServiceImpl
        extends BaseObjectDataServiceImpl<SequentialReceiptNumberGeneratorModel, BasicEntityAuthorizationPrivileges>
        implements ISequentialReceiptNumberGeneratorService {
	@Override
	protected BasicEntityAuthorizationPrivileges getPrivileges() {
		// No authorization required
		return null;
	}

	@Override
	protected void validate(SequentialReceiptNumberGeneratorModel entity) {
		return;
	}

	@Override
	@Transactional(readOnly = true)
	public SequentialReceiptNumberGeneratorModel getOnly() {
		List<SequentialReceiptNumberGeneratorModel> records = getAll();

		if (records.size() > 0) {
			return records.get(0);
		} else {
			return new SequentialReceiptNumberGeneratorModel();
		}
	}

	@Override
	@Transactional
	public int reserveNextSequence(String group) {
		// Get the sequence
		GroupSequence sequence = getSequence(group);
		if (sequence == null) {
			// Sequence not found so create it
			sequence = new GroupSequence();
			sequence.setGroup(group);
			sequence.setValue(1);
		} else {
			// Increment the value
			sequence.setValue(sequence.getValue() + 1);
		}

		// Store the sequence and save the updated or new sequence
		int result = sequence.getValue();
		saveSequence(sequence);

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GroupSequence> getSequences() {
		return getRepository().select(GroupSequence.class);
	}

	@Override
	@Transactional(readOnly = true)
	public GroupSequence getSequence(String group) {
		if (group == null) {
			throw new IllegalArgumentException("The group must be defined.");
		}

		Criteria criteria = getRepository().createCriteria(GroupSequence.class);
		criteria.add(Restrictions.eq("group", group));

		return getRepository().selectSingle(GroupSequence.class, criteria);
	}

	@Override
	@Transactional
	public GroupSequence saveSequence(GroupSequence sequence) {
		if (sequence == null) {
			throw new NullPointerException("The sequence to save must be defined.");
		}

		return getRepository().save(sequence);
	}

	@Override
	@Transactional
	public void purgeSequence(GroupSequence sequence) {
		if (sequence == null) {
			throw new NullPointerException("The sequence to purge must be defined.");
		}

		getRepository().delete(sequence);
	}
}
