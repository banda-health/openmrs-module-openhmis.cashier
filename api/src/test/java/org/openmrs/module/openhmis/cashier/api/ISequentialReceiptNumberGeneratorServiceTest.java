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
package org.openmrs.module.openhmis.cashier.api;

import junit.framework.Assert;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;

public class ISequentialReceiptNumberGeneratorServiceTest
		extends IEntityServiceTest<ISequentialReceiptNumberGeneratorService, SequentialReceiptNumberGeneratorModel> {
	public static final String SEQUENTIAL_RECEIPT_NUMBER_GENERATOR_DATASET =
			BASE_DATASET_DIR + "SequentialReceiptNumberGenerator.xml";

	@Override
	protected SequentialReceiptNumberGeneratorModel createEntity(boolean valid) {
		SequentialReceiptNumberGeneratorModel model = new SequentialReceiptNumberGeneratorModel();
		model.setGroupingType(SequentialReceiptNumberGenerator.GroupingType.NONE);
		model.setSequenceType(SequentialReceiptNumberGenerator.SequenceType.COUNTER);
		model.setSeparator("-");
		model.setSequencePadding(4);
		model.includeCheckDigit(true);

		if (valid) {
			model.setCashierPrefix(SequentialReceiptNumberGeneratorModel.DEFAULT_CASHIER_PREFIX);
			model.setCashPointPrefix(SequentialReceiptNumberGeneratorModel.DEFAULT_CASH_POINT_PREFIX);
		} else {
			model.setCashierPrefix(null);
			model.setCashPointPrefix(null);
		}

		return model;
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
		entity.includeCheckDigit(!entity.hasCheckDigit());
	}

	@Override
	protected void assertEntity(SequentialReceiptNumberGeneratorModel expected, SequentialReceiptNumberGeneratorModel actual) {
		Assert.assertEquals(expected.getCashierPrefix(), actual.getCashierPrefix());
		Assert.assertEquals(expected.getCashPointPrefix(), actual.getCashPointPrefix());
		Assert.assertEquals(expected.getGroupingType(), actual.getGroupingType());
		Assert.assertEquals(expected.getSeparator(), actual.getSeparator());
		Assert.assertEquals(expected.getSequencePadding(), actual.getSequencePadding());
		Assert.assertEquals(expected.getSequenceType(), actual.getSequenceType());
		Assert.assertEquals(expected.hasCheckDigit(), actual.hasCheckDigit());
	}
}
