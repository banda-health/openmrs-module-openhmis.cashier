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
package org.openmrs.module.openhmis.cashier.api;

import java.util.List;

import org.openmrs.module.openhmis.cashier.api.model.GroupSequence;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents classes that provide data access services to model types that implement
 * {@link SequentialReceiptNumberGeneratorModel}. The {@link SequentialReceiptNumberGeneratorModel} model class.
 */
public interface ISequentialReceiptNumberGeneratorService extends IObjectDataService<SequentialReceiptNumberGeneratorModel> {
	/**
	 * Gets the first {@link SequentialReceiptNumberGeneratorModel} or creates a new model if none have been defined.
	 * @return The first or new model.
	 * @should return the first model.
	 * @should return a new model if none has been defined.
	 */
	SequentialReceiptNumberGeneratorModel getOnly();

	/**
	 * Reserves the next sequence value for the specified group.
	 * @param group The grouping value.
	 * @return The next sequence value.
	 * @should Increment and return the sequence value for existing groups
	 * @should Create a new sequence with a value of one if the group does not exist
	 * @should Throw IllegalArgumentException if the group is null
	 */
	@Transactional
	int reserveNextSequence(String group);

	/**
	 * Returns all sequences.
	 * @return All sequences in the database.
	 * @should return all sequences
	 * @should return an empty list if no sequences have been defined
	 */
	@Transactional(readOnly = true)
	List<GroupSequence> getSequences();

	/**
	 * Returns the sequence for the specified group.
	 * @param group The group sequence to find.
	 * @return The group sequence
	 * @should Throw an IllegalArgumentException if group is null
	 * @should return the specified sequence
	 * @should return null if the sequence cannot be found
	 * @should return the sequence if group is empty
	 */
	@Transactional(readOnly = true)
	GroupSequence getSequence(String group);

	/**
	 * Saves the sequence, creating a new sequences or updating an existing one.
	 * @param sequence The sequence to save.
	 * @return The saved sequence.
	 * @should Throw a NullPointerException if sequence is null
	 * @should return the saved sequence
	 * @should update the sequence successfully
	 * @should create the sequence successfully
	 */
	@Transactional
	GroupSequence saveSequence(GroupSequence sequence);

	/**
	 * Complete removes the specified sequence from the database.
	 * @param sequence The sequence to remove.
	 * @should Throw a NullPointerException if the sequence is null
	 * @should delete the sequence from the database
	 * @should not throw an exception if the sequence is not in the database
	 */
	@Transactional
	void purgeSequence(GroupSequence sequence);
}
