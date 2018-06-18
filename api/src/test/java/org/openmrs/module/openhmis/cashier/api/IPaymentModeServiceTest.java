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

import org.junit.Assert;
import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;
import org.openmrs.module.openhmis.cashier.api.model.PaymentModeAttributeType;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataServiceTest;

public class IPaymentModeServiceTest extends IMetadataDataServiceTest<IPaymentModeService, PaymentMode> {
	public static final String PAYMENT_MODE_DATASET = TestConstants.BASE_DATASET_DIR + "PaymentModeTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		executeDataSet(PAYMENT_MODE_DATASET);
	}

	@Override
	public PaymentMode createEntity(boolean valid) {
		PaymentMode mode = new PaymentMode();

		if (valid) {
			mode.setName("Test Payment Mode");
		}

		mode.setDescription("Test Description");

		mode.addAttributeType("Test 1 Attribute Type", "", "", true);
		mode.addAttributeType("Test 2 Attribute Type", "", "", false);

		return mode;
	}

	@Override
	protected int getTestEntityCount() {
		return 3;
	}

	@Override
	protected void updateEntityFields(PaymentMode mode) {
		mode.setName(mode.getName() + " updated");
		mode.setDescription(mode.getDescription() + " updated");

		List<PaymentModeAttributeType> attributeTypes = mode.getAttributeTypes();
		if (attributeTypes.size() > 0) {
			PaymentModeAttributeType attributeType = attributeTypes.get(0);
			attributeType.setName(attributeType.getName() + " updated");
			attributeType.setFormat("updated");
			attributeType.setRegExp("updated");
			attributeType.setRequired(!attributeType.getRequired());

			if (attributeTypes.size() > 1) {
				attributeType = attributeTypes.get(attributeTypes.size() - 1);

				mode.removeAttributeType(attributeType);
			}
		}

		mode.addAttributeType("Test 3 Attribute Type", "", "", true);
	}

	@Override
	protected void assertEntity(PaymentMode expected, PaymentMode actual) {
		super.assertEntity(expected, actual);

		Assert.assertEquals(expected.getName(), actual.getName());
		Assert.assertEquals(expected.getDescription(), actual.getDescription());

		List<PaymentModeAttributeType> expectedTypes = expected.getAttributeTypes();
		List<PaymentModeAttributeType> actualTypes = actual.getAttributeTypes();
		Assert.assertEquals(expectedTypes.size(), actualTypes.size());

		for (int i = 0; i < expectedTypes.size(); i++) {
			PaymentModeAttributeType expectedType = expectedTypes.get(i);
			PaymentModeAttributeType actualType = actualTypes.get(i);

			Assert.assertEquals(expectedType.getName(), actualType.getName());
			Assert.assertEquals(expectedType.getFormat(), actualType.getFormat());
			Assert.assertEquals(expectedType.getRegExp(), actualType.getRegExp());
			Assert.assertEquals(expectedType.getRequired(), actualType.getRequired());
		}
	}
}
