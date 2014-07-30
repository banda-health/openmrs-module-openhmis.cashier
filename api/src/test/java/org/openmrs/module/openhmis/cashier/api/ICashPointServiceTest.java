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

import com.google.common.collect.Iterators;
import liquibase.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataServiceTest;

import java.util.Date;
import java.util.List;

public class ICashPointServiceTest extends IMetadataDataServiceTest<ICashPointService, CashPoint> {
	public static final String CASH_POINT_DATASET = TestConstants.BASE_DATASET_DIR + "CashPointTest.xml";

	@Override
	public void before() throws Exception {
		super.before();

		executeDataSet(CASH_POINT_DATASET);
        executeDataSet(TestConstants.CORE_DATASET);
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
		return 7;
	}

	@Override
	protected void updateEntityFields(CashPoint cashPoint) {
		cashPoint.setName(cashPoint.getName() + " updated");
		cashPoint.setDescription(cashPoint.getDescription() + " updated");
        cashPoint.setLocation(Context.getLocationService().getLocation(1));
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

    /**
     * @verifies return an empty list if the location has no cashpoints
     * @see ICashPointService#getCashPointsByLocation(org.openmrs.Location, boolean)
     */
    @Test
    public void getCashPointsByLocation_shouldReturnAnEmptyListIfTheLocationHasNoCashpoints() throws Exception {
        Location location = Context.getLocationService().getLocation(4);

        List<CashPoint> results = service.getCashPointsByLocation(location,false);
        Assert.assertNotNull(results);
        Assert.assertEquals(0,results.size());
    }

    /**
     * @verifies not return retired cashpoints unless specified
     * @see ICashPointService#getCashPointsByLocation(org.openmrs.Location, boolean)
     */
    @Test
    public void getCashPointsByLocation_shouldNotReturnRetiredCashpointsUnlessSpecified() throws Exception {
        CashPoint cashPoint = service.getById(0);
        cashPoint.setRetired(true);
        cashPoint.setRetireReason("reason");
        service.save(cashPoint);
        Location location = Context.getLocationService().getLocation(0);

        Context.flushSession();

        List<CashPoint> cashPoints = service.getCashPointsByLocation(location,false);
        Assert.assertNotNull(cashPoints);
        Assert.assertEquals(2,cashPoints.size());
	    Assert.assertEquals(4,(int)Iterators.get(cashPoints.iterator(),0).getId());
	    Assert.assertEquals(5,(int)Iterators.get(cashPoints.iterator(),1).getId());

	    List<CashPoint> cashPoints1 = service.getCashPointsByLocation(location,true);
	    Assert.assertNotNull(cashPoints1);
	    Assert.assertEquals(3,cashPoints1.size());
	    Assert.assertEquals(0,(int)Iterators.get(cashPoints1.iterator(),0).getId());
    }

    /**
     * @verifies return all cashpoints for the specified location
     * @see ICashPointService#getCashPointsByLocation(org.openmrs.Location, boolean)
     */
    @Test
    public void getCashPointsByLocation_shouldReturnAllCashpointsForTheSpecifiedLocation() throws Exception {
        List<CashPoint> cashPoint = service.getCashPointsByLocation(Context.getLocationService().getLocation(0),false);
        Assert.assertNotNull(cashPoint);
        Assert.assertEquals(3, cashPoint.size());

	    Assert.assertEquals(0,(int)Iterators.get(cashPoint.iterator(),0).getId());
	    Assert.assertEquals(4,(int)Iterators.get(cashPoint.iterator(),1).getId());
	    Assert.assertEquals(5,(int)Iterators.get(cashPoint.iterator(),2).getId());
    }

    /**
     * @verifies throw IllegalArgumentException if the name is null
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test(expected = IllegalArgumentException.class)
    public void findCashPoints_shouldThrowIllegalArgumentExceptionIfTheNameIsNull() throws Exception {
        service.findCashPoints(Context.getLocationService().getLocation(1),null,false);
    }

    /**
     * @verifies throw IllegalArgumentException if the name is empty
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test(expected = IllegalArgumentException.class)
    public void findCashPoints_shouldThrowIllegalArgumentExceptionIfTheNameIsEmpty() throws Exception {
        service.findCashPoints(Context.getLocationService().getLocation(1),"",false);
    }

    /**
     * @verifies throw IllegalArgumentException if the name is longer than 255 characters
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test(expected = IllegalArgumentException.class)
    public void findCashPoints_shouldThrowIllegalArgumentExceptionIfTheNameIsLongerThan255Characters() throws Exception {
        service.findCashPoints(Context.getLocationService().getLocation(1), StringUtils.repeat("A", 256), false);
    }

    /**
     * @verifies return an empty list if no cashpoints are found
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test
    public void findCashPoints_shouldReturnAnEmptyListIfNoCashpointsAreFound() throws Exception {
        Location location = Context.getLocationService().getLocation(4);

        List<CashPoint> results = service.findCashPoints(location,"Test",false);
        Assert.assertNotNull(results);
        Assert.assertEquals(0,results.size());
    }

    /**
     * @verifies not return retired cashpoints unless specified
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test
    public void findCashPoints_shouldNotReturnRetiredCashpointsUnlessSpecified() throws Exception {
        CashPoint cashPoint = service.getById(0);
        cashPoint.setRetired(true);
        cashPoint.setRetireReason("reason");
        service.save(cashPoint);
        Location location = Context.getLocationService().getLocation(0);

        Context.flushSession();

        List<CashPoint> results = service.findCashPoints(location,"Test",false);
        Assert.assertNotNull(results);
        Assert.assertEquals(2,results.size());
	    Assert.assertEquals(4,(int)Iterators.get(results.iterator(),0).getId());
	    Assert.assertEquals(5,(int)Iterators.get(results.iterator(),1).getId());

	    List<CashPoint> results1 = service.findCashPoints(location,"Test",true);
	    Assert.assertNotNull(results1);
	    Assert.assertEquals(3,results1.size());
	    Assert.assertEquals(0,(int)Iterators.get(results1.iterator(),0).getId());
    }

    /**
     * @verifies return cashpoints that start with the specified name
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test
    public void findCashPoints_shouldReturnCashpointsThatStartWithTheSpecifiedName() throws Exception {
        List<CashPoint> results = service.findCashPoints(Context.getLocationService().getLocation(0),"Test 1",false);
        Assert.assertNotNull(results);
        Assert.assertEquals(1,results.size());
	    Assert.assertEquals(0,(int)Iterators.get(results.iterator(),0).getId());

        CashPoint cashPoint = service.getById(0);
        assertEntity(cashPoint, results.get(0));

	    List<CashPoint> results1 = service.findCashPoints(Context.getLocationService().getLocation(2),"Test",false);
	    Assert.assertNotNull(results1);
	    Assert.assertEquals(2,results1.size());
	    Assert.assertEquals(2,(int)Iterators.get(results1.iterator(),0).getId());
	    Assert.assertEquals(6,(int)Iterators.get(results1.iterator(),1).getId());
    }

    /**
     * @verifies return cashpoints for only the specified location
     * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
     */
    @Test
    public void findCashPoints_shouldReturnCashpointsForOnlyTheSpecifiedLocation() throws Exception {
        List<CashPoint> cashPoint = service.findCashPoints(Context.getLocationService().getLocation(0),"Test",false);
        Assert.assertNotNull(cashPoint);
        Assert.assertEquals(3, cashPoint.size());

	    Assert.assertEquals(0,(int)Iterators.get(cashPoint.iterator(),0).getId());
	    Assert.assertEquals(4,(int)Iterators.get(cashPoint.iterator(),1).getId());
	    Assert.assertEquals(5,(int)Iterators.get(cashPoint.iterator(),2).getId());
    }

	/**
	 * @verifies throw IllegalArgumentException if the location is null
	 * @see ICashPointService#getCashPointsByLocation(org.openmrs.Location, boolean)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getCashPointsByLocation_shouldThrowIllegalArgumentExceptionIfTheLocationIsNull() throws Exception {
		Location location = null;
		service.getCashPointsByLocation(location,false);
	}

	/**
	 * @verifies throw IllegalArgumentException if the location is null
	 * @see ICashPointService#findCashPoints(org.openmrs.Location, String, boolean)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findCashPoints_shouldThrowIllegalArgumentExceptionIfTheLocationIsNull() throws Exception {
		Location location = null;
		service.findCashPoints(location,"Test",false);
	}
}
