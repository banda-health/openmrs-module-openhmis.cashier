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
package org.openmrs.module.openhmis.cashier.api.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.ISequentialReceiptNumberGeneratorService;
import org.openmrs.module.openhmis.cashier.api.model.GroupSequence;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.cashier.api.security.BasicEntityAuthorizationPrivileges;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SequentialReceiptNumberGeneratorServiceImpl
		extends BaseEntityServiceImpl<SequentialReceiptNumberGeneratorModel, BasicEntityAuthorizationPrivileges>
		implements ISequentialReceiptNumberGeneratorService{
	@Override
	protected BasicEntityAuthorizationPrivileges getPrivileges() {
		// No authorization required
		return null;
	}

	@Override
	protected void validate(SequentialReceiptNumberGeneratorModel entity) throws APIException {
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
	public int reserveNextSequence(String group) throws APIException {
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
		return dao.select(GroupSequence.class);
	}

	@Override
	@Transactional(readOnly = true)
	public GroupSequence getSequence(String group) {
		if (group == null) {
			throw new IllegalArgumentException("The group must be defined.");
		}

		Criteria criteria = dao.createCriteria(GroupSequence.class);
		criteria.add(Restrictions.eq("group", group));

		return dao.selectSingle(GroupSequence.class, criteria);
	}

	@Override
	@Transactional
	public GroupSequence saveSequence(GroupSequence sequence) {
		if (sequence == null) {
			throw new NullPointerException("The sequence to save must be defined.");
		}

		return dao.save(sequence);
	}

	@Override
	@Transactional
	public void purgeSequence(GroupSequence sequence) {
		if (sequence == null) {
			throw new NullPointerException("The sequence to purge must be defined.");
		}

		dao.delete(sequence);
	}
}
