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

import org.junit.Assert;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataServiceTest;
import org.openmrs.module.openhmis.commons.api.f.Action2;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataServiceTest;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;

import java.util.Date;

public class ICashPointServiceTest extends IMetadataDataServiceTest<ICashPointService, CashPoint> {
	public static final String CASH_POINT_DATASET = TestConstants.BASE_DATASET_DIR + "CashPointTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		executeDataSet(CASH_POINT_DATASET);
	}

	@Override
	public CashPoint createEntity(boolean valid) {
		CashPoint cashPoint = new CashPoint();

		if (valid) {
			cashPoint.setName("Test Cash Point");
		}

		cashPoint.setDescription("Test description");
        cashPoint.setLocation(Context.getLocationService().getLocation(1));
        cashPoint.setCreator(Context.getAuthenticatedUser());
        cashPoint.setDateCreated(new Date());


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
        cashPoint.setLocation(Context.getLocationService().getLocation(0));
        cashPoint.setCreator(Context.getAuthenticatedUser());
        cashPoint.setDateChanged(new Date());
	}

    public static void assertCashPoint(CashPoint expected, CashPoint actual) {
        assertOpenmrsMetadata(expected, actual);

        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        if (expected.getLocation() == null) {
            Assert.assertNull(actual.getLocation());
        } else {
            Assert.assertEquals(expected.getLocation().getId(), actual.getLocation().getId());
        }
    }

    @Override
    protected void assertEntity(CashPoint expected, CashPoint  actual) {
        assertCashPoint(expected, actual);
    }

}
