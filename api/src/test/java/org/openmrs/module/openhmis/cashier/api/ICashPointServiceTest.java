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

import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataServiceTest;

public class ICashPointServiceTest extends IMetadataDataServiceTest<ICashPointService, CashPoint> {
	public static final String CASH_POINT_DATASET = TestConstants.BASE_DATASET_DIR + "CashPointTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		executeDataSet(CASH_POINT_DATASET);
	}

	@Override
	protected CashPoint createEntity(boolean valid) {
		CashPoint cashPoint = new CashPoint();

		if (valid) {
			cashPoint.setName("Test Cash Point");
		}

		cashPoint.setDescription("Test description");

		return cashPoint;
	}

	@Override
	protected int getTestEntityCount() {
		return 3;
	}

	@Override
	protected void updateEntityFields(CashPoint cashPoint) {
		cashPoint.setName(cashPoint.getName() + " updated");
		cashPoint.setDescription(cashPoint.getDescription() + " updated");
	}
}
