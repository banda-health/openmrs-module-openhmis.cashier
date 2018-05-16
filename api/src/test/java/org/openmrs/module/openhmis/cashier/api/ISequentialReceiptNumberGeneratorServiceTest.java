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
import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.GroupSequence;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataServiceTest;

public class ISequentialReceiptNumberGeneratorServiceTest
        extends IObjectDataServiceTest<ISequentialReceiptNumberGeneratorService, SequentialReceiptNumberGeneratorModel> {
	public static final String SEQUENTIAL_RECEIPT_NUMBER_GENERATOR_DATASET =
	        TestConstants.BASE_DATASET_DIR + "SequentialReceiptNumberGenerator.xml";

	@Override
	public Properties getRuntimeProperties() {
		Properties properties = super.getRuntimeProperties();

		// This is needed for proper locking in the in-memory database
		properties.setProperty(Environment.URL, "jdbc:h2:mem:openmrs;DB_CLOSE_DELAY=30;MVCC=TRUE");

		return properties;
	}

	@Before
	public void before() throws Exception {
		super.before();

		executeDataSet(SEQUENTIAL_RECEIPT_NUMBER_GENERATOR_DATASET);
	}

	@Override
	public SequentialReceiptNumberGeneratorModel createEntity(boolean valid) {
		SequentialReceiptNumberGeneratorModel model = new SequentialReceiptNumberGeneratorModel();
		model.setGroupingType(SequentialReceiptNumberGenerator.GroupingType.NONE);
		model.setSequenceType(SequentialReceiptNumberGenerator.SequenceType.COUNTER);
		model.setSeparator("-");
		model.setSequencePadding(4);
		model.setIncludeCheckDigit(true);

		if (valid) {
			model.setCashierPrefix(SequentialReceiptNumberGeneratorModel.DEFAULT_CASHIER_PREFIX);
			model.setCashPointPrefix(SequentialReceiptNumberGeneratorModel.DEFAULT_CASH_POINT_PREFIX);
		} else {
			model.setCashierPrefix(null);
			model.setCashPointPrefix(null);
		}

		return model;
	}

	protected GroupSequence createSequence(String group, int value) {
		GroupSequence result = new GroupSequence();

		result.setGroup(group);
		result.setValue(value);

		return result;
	}

	@Override
	protected int getTestEntityCount() {
		return 1;
	}

	@Override
	protected void updateEntityFields(SequentialReceiptNumberGeneratorModel entity) {
		entity.setCashierPrefix("UP");
		entity.setCashPointPrefix("UCP");
		entity.setGroupingType(SequentialReceiptNumberGenerator.GroupingType.CASH_POINT);
		entity.setSequenceType(SequentialReceiptNumberGenerator.SequenceType.DATE_TIME_COUNTER);
		entity.setSeparator("_");
		entity.setSequencePadding(8);
		entity.setIncludeCheckDigit(!entity.getIncludeCheckDigit());
	}

	@Override
	protected void assertEntity(SequentialReceiptNumberGeneratorModel expected, SequentialReceiptNumberGeneratorModel actual) {
		Assert.assertEquals(expected.getCashierPrefix(), actual.getCashierPrefix());
		Assert.assertEquals(expected.getCashPointPrefix(), actual.getCashPointPrefix());
		Assert.assertEquals(expected.getGroupingType(), actual.getGroupingType());
		Assert.assertEquals(expected.getSeparator(), actual.getSeparator());
		Assert.assertEquals(expected.getSequencePadding(), actual.getSequencePadding());
		Assert.assertEquals(expected.getSequenceType(), actual.getSequenceType());
		Assert.assertEquals(expected.getIncludeCheckDigit(), actual.getIncludeCheckDigit());
	}

	/**
	 * @verifies Increment and return the sequence value for existing groups
	 * @see ISequentialReceiptNumberGeneratorService#reserveNextSequence(String)
	 */
	@Test
	public void reserveNextSequence_shouldIncrementAndReturnTheSequenceValueForExistingGroups() throws Exception {
		GroupSequence sequence = createSequence("test", 1);
		service.saveSequence(sequence);
		sequence = createSequence("test2", 53);
		service.saveSequence(sequence);
		sequence = createSequence("test3", 10);
		service.saveSequence(sequence);
		Context.flushSession();

		int result = service.reserveNextSequence("test");
		Assert.assertEquals(2, result);
		sequence = service.getSequence("test");
		Assert.assertNotNull(sequence);
		Assert.assertEquals(2, sequence.getValue());
		Context.flushSession();

		result = service.reserveNextSequence("test");
		Assert.assertEquals(3, result);
		sequence = service.getSequence("test");
		Assert.assertNotNull(sequence);
		Assert.assertEquals(3, sequence.getValue());
		Context.flushSession();

		result = service.reserveNextSequence("test2");
		Assert.assertEquals(54, result);
		sequence = service.getSequence("test2");
		Assert.assertNotNull(sequence);
		Assert.assertEquals(54, sequence.getValue());
		Context.flushSession();

		result = service.reserveNextSequence("test3");
		Assert.assertEquals(11, result);
		sequence = service.getSequence("test3");
		Assert.assertNotNull(sequence);
		Assert.assertEquals(11, sequence.getValue());
	}

	/**
	 * @verifies Create a new sequence with a value of one if the group does not exist
	 * @see ISequentialReceiptNumberGeneratorService#reserveNextSequence(String)
	 */
	@Test
	public void reserveNextSequence_shouldCreateANewSequenceWithAValueOfOneIfTheGroupDoesNotExist() throws Exception {
		GroupSequence sequence = service.getSequence("test");
		Assert.assertNull(sequence);

		int result = service.reserveNextSequence("test");
		Assert.assertEquals(1, result);

		Context.flushSession();

		sequence = service.getSequence("test");
		Assert.assertNotNull(sequence);
		Assert.assertEquals(1, sequence.getValue());
	}

	/**
	 * @verifies Throw IllegalArgumentException if the group is null
	 * @see ISequentialReceiptNumberGeneratorService#reserveNextSequence(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void reserveNextSequence_shouldThrowIllegalArgumentExceptionIfTheGroupIsNull() throws Exception {
		service.reserveNextSequence(null);
	}

	/**
	 * @verifies return all sequences
	 * @see ISequentialReceiptNumberGeneratorService#getSequences()
	 */
	@Test
	public void getSequences_shouldReturnAllSequences() throws Exception {
		List<GroupSequence> sequences = service.getSequences();

		Assert.assertNotNull(sequences);
		Assert.assertEquals(4, sequences.size());
	}

	/**
	 * @verifies return an empty list if no sequences have been defined
	 * @see ISequentialReceiptNumberGeneratorService#getSequences()
	 */
	@Test
	public void getSequences_shouldReturnAnEmptyListIfNoSequencesHaveBeenDefined() throws Exception {
		List<GroupSequence> sequences = service.getSequences();
		for (GroupSequence sequence : sequences) {
			service.purgeSequence(sequence);
		}

		Context.flushSession();

		sequences = service.getSequences();
		Assert.assertNotNull(sequences);
		Assert.assertEquals(0, sequences.size());
	}

	/**
	 * @verifies Throw a NullPointerException if sequence is null
	 * @see ISequentialReceiptNumberGeneratorService#saveSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test(expected = NullPointerException.class)
	public void saveSequence_shouldThrowANullPointerExceptionIfSequenceIsNull() throws Exception {
		service.saveSequence(null);
	}

	/**
	 * @verifies return the saved sequence
	 * @see ISequentialReceiptNumberGeneratorService#saveSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test
	public void saveSequence_shouldReturnTheSavedSequence() throws Exception {
		GroupSequence sequence = new GroupSequence();
		sequence.setGroup("New Group");
		sequence.setValue(50);

		sequence = service.saveSequence(sequence);

		Assert.assertNotNull(sequence);
		Assert.assertNotNull(sequence.getId());
		Assert.assertEquals("New Group", sequence.getGroup());
		Assert.assertEquals(50, sequence.getValue());
	}

	/**
	 * @verifies update the sequence successfully
	 * @see ISequentialReceiptNumberGeneratorService#saveSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test
	public void saveSequence_shouldUpdateTheSequenceSuccessfully() throws Exception {
		GroupSequence sequence = service.getSequence("Test Seq 1");
		int oldValue = sequence.getValue();
		sequence.setValue(oldValue + 10);

		service.saveSequence(sequence);

		Context.flushSession();

		sequence = service.getSequence(sequence.getGroup());
		Assert.assertNotNull(sequence);
		Assert.assertEquals(oldValue + 10, sequence.getValue());
	}

	/**
	 * @verifies create the sequence successfully
	 * @see ISequentialReceiptNumberGeneratorService#saveSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test
	public void saveSequence_shouldCreateTheSequenceSuccessfully() throws Exception {
		GroupSequence sequence = new GroupSequence();
		sequence.setGroup("New Group");
		sequence.setValue(50);

		Assert.assertNull(sequence.getId());

		sequence = service.saveSequence(sequence);

		Assert.assertNotNull(sequence);
		Assert.assertNotNull(sequence.getId());
	}

	/**
	 * @verifies Throw a NullPointerException if the sequence is null
	 * @see ISequentialReceiptNumberGeneratorService#purgeSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test(expected = NullPointerException.class)
	public void purgeSequence_shouldThrowANullPointerExceptionIfTheSequenceIsNull() throws Exception {
		service.purgeSequence(null);
	}

	/**
	 * @verifies delete the sequence from the database
	 * @see ISequentialReceiptNumberGeneratorService#purgeSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test
	public void purgeSequence_shouldDeleteTheSequenceFromTheDatabase() throws Exception {
		GroupSequence sequence = service.getSequence("Test Seq 1");
		service.purgeSequence(sequence);

		Context.flushSession();

		sequence = service.getSequence("Test Seq 1");
		Assert.assertNull(sequence);
	}

	/**
	 * @verifies not throw an exception if the sequence is not in the database
	 * @see ISequentialReceiptNumberGeneratorService#purgeSequence(org.openmrs.module.openhmis.cashier.api.model.GroupSequence)
	 */
	@Test
	public void purgeSequence_shouldNotThrowAnExceptionIfTheSequenceIsNotInTheDatabase() throws Exception {
		GroupSequence sequence = service.getSequence("Test Seq 1");
		service.purgeSequence(sequence);

		Context.flushSession();

		service.purgeSequence(sequence);
	}

	/**
	 * @verifies Throw an IllegalArgumentException if group is null
	 * @see ISequentialReceiptNumberGeneratorService#getSequence(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getSequence_shouldThrowAnIllegalArgumentExceptionIfGroupIsNull() throws Exception {
		service.getSequence(null);
	}

	/**
	 * @verifies return the sequence if group is empty
	 * @see ISequentialReceiptNumberGeneratorService#getSequence(String)
	 */
	@Test
	public void getSequence_shouldReturnTheSequenceIfGroupIsEmpty() throws Exception {
		GroupSequence sequence = service.getSequence("");
		Assert.assertNotNull(sequence);
		Assert.assertEquals("", sequence.getGroup());
		Assert.assertEquals(18, sequence.getValue());
	}

	/**
	 * @verifies return the specified sequence
	 * @see ISequentialReceiptNumberGeneratorService#getSequence(String)
	 */
	@Test
	public void getSequence_shouldReturnTheSpecifiedSequence() throws Exception {
		GroupSequence sequence = service.getSequence("Test Seq 1");

		Assert.assertNotNull(sequence);
		Assert.assertEquals("Test Seq 1", sequence.getGroup());
		Assert.assertEquals(10, sequence.getValue());
	}

	/**
	 * @verifies return null if the sequence cannot be found
	 * @see ISequentialReceiptNumberGeneratorService#getSequence(String)
	 */
	@Test
	public void getSequence_shouldReturnNullIfTheSequenceCannotBeFound() throws Exception {
		GroupSequence sequence = service.getSequence("Not A Valid Sequence");

		Assert.assertNull(sequence);
	}

	/**
	 * @verifies return the first model.
	 * @see ISequentialReceiptNumberGeneratorService#getOnly()
	 */
	@Test
	public void getOnly_shouldReturnTheFirstModel() throws Exception {
		SequentialReceiptNumberGeneratorModel model = service.getOnly();

		Assert.assertNotNull(model);
		Assert.assertEquals((Integer)0, model.getId());
	}

	/**
	 * @verifies return a new model if none has been defined.
	 * @see ISequentialReceiptNumberGeneratorService#getOnly()
	 */
	@Test
	public void getOnly_shouldReturnANewModelIfNoneHasBeenDefined() throws Exception {
		SequentialReceiptNumberGeneratorModel model = service.getOnly();
		service.purge(model);

		model = service.getOnly();
		Assert.assertNotNull(model);
		Assert.assertNull(model.getId());
	}
}
