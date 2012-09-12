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

import org.openmrs.module.openhmis.cashier.api.model.PaymentMode;

public class IPaymentModeServiceTest extends IMetadataServiceTest<IPaymentModeService, PaymentMode> {
	public static final String PAYMENT_MODE_DATASET = BASE_DATASET_DIR + "PaymentModeTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		executeDataSet(PAYMENT_MODE_DATASET);
	}

	@Override
	protected PaymentMode createEntity(boolean valid) {
		return null;
	}

	@Override
	protected int getTestEntityCount() {
		return 0;
	}

	@Override
	protected void updateEntityFields(PaymentMode entity) {
	}
}
