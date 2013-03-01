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

import liquibase.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.TestConstants;
import org.openmrs.module.openhmis.cashier.api.model.*;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataServiceTest;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class IBillServiceTest extends IEntityDataServiceTest<IBillService, Bill> {
	public static final String BILL_DATASET = TestConstants.BASE_DATASET_DIR + "BillTest.xml";

	private ProviderService providerService;
	private PatientService patientService;
	private IItemService itemService;
	private IPaymentModeService paymentModeService;
	private ICashPointService cashPointService;

	@Override
	public void before() throws Exception {
		super.before();

		providerService = Context.getProviderService();
		patientService = Context.getPatientService();
		itemService = Context.getService(IItemService.class);
		paymentModeService = Context.getService(IPaymentModeService.class);
		cashPointService = Context.getService(ICashPointService.class);

		executeDataSet(IItemServiceTest.ITEM_DATASET);
		executeDataSet(IPaymentModeServiceTest.PAYMENT_MODE_DATASET);
		executeDataSet(ICashPointServiceTest.CASH_POINT_DATASET);
		executeDataSet(TestConstants.CORE_DATASET);
		executeDataSet(BILL_DATASET);
	}

	@Override
	protected Bill createEntity(boolean valid) {
		Bill bill = new Bill();

		if (valid) {
			bill.setCashier(providerService.getProvider(0));
			bill.setPatient(patientService.getPatient(0));
			bill.setCashPoint(cashPointService.getById(0));
			bill.setReceiptNumber("Test 1234");
			bill.setStatus(BillStatus.PAID);
		}

		Item item = itemService.getById(0);
		bill.addLineItem(item, item.getPrices().iterator().next(), 1);
		item = itemService.getById(1);
		bill.addLineItem(item, item.getPrices().iterator().next(), 1);

		PaymentMode mode = paymentModeService.getById(0);
		bill.addPayment(mode, null, BigDecimal.valueOf(100), BigDecimal.valueOf(100));

		mode = paymentModeService.getById(1);
		bill.addPayment(mode, null, BigDecimal.valueOf(200), BigDecimal.valueOf(200));

		return bill;
	}

	@Override
	protected int getTestEntityCount() {
		return 1;
	}

	@Override
	protected void updateEntityFields(Bill bill) {
		bill.setCashier(providerService.getProvider(1));
		bill.setPatient(patientService.getPatient(2));
		bill.setCashPoint(cashPointService.getById(0));
		bill.setReceiptNumber(bill.getReceiptNumber() + " updated");
		bill.setStatus(BillStatus.PENDING);

		List<BillLineItem> lines = bill.getLineItems();
		if (lines.size() > 0) {
			BillLineItem item = lines.get(0);

			item.setPrice(item.getPrice().multiply(BigDecimal.valueOf(2)));
			item.setPriceName(item.getPriceName() + " updated");

			if (lines.size() > 1) {
				item = lines.get(1);

				bill.removeLineItem(item);
			}
		}

		Item newItem = itemService.getById(2);
		bill.addLineItem(newItem, newItem.getPrices().iterator().next(), 3);

		Set<Payment> payments = bill.getPayments();
		if (payments.size() > 0) {
			Iterator<Payment> iterator = payments.iterator();

			Payment payment = iterator.next();
			payment.setAmount(payment.getAmount().divide(BigDecimal.valueOf(2)));

			if (payments.size() > 1) {
				payment = iterator.next();

				bill.removePayment(payment);
			}
		}

		bill.addPayment(paymentModeService.getById(2), null, BigDecimal.valueOf(303.11), BigDecimal.valueOf(350.00));
	}

	@Override
	protected void assertEntity(Bill expected, Bill actual) {
		super.assertEntity(expected, actual);

		Assert.assertNotNull(expected.getCashier());
		Assert.assertNotNull(actual.getCashier());
		Assert.assertEquals(expected.getCashier().getId(), actual.getCashier().getId());
		Assert.assertNotNull(expected.getPatient());
		Assert.assertNotNull(actual.getPatient());
		Assert.assertEquals(expected.getPatient().getId(), actual.getPatient().getId());
		Assert.assertNotNull(expected.getCashPoint());
		Assert.assertNotNull(actual.getCashPoint());
		Assert.assertEquals(expected.getCashPoint().getId(), actual.getCashPoint().getId());

		Assert.assertEquals(expected.getReceiptNumber(), actual.getReceiptNumber());
		Assert.assertEquals(expected.getStatus(), actual.getStatus());

		Assert.assertEquals(expected.getLineItems().size(), actual.getLineItems().size());
		BillLineItem[] expectedItems = new BillLineItem[expected.getLineItems().size()];
		expected.getLineItems().toArray(expectedItems);
		BillLineItem[] actualItems = new BillLineItem[actual.getLineItems().size()];
		actual.getLineItems().toArray(actualItems);
		for (int i = 0; i < expected.getLineItems().size(); i++) {
			Assert.assertEquals(expectedItems[i].getId(), actualItems[i].getId());
			Assert.assertEquals(expectedItems[i].getItem(), actualItems[i].getItem());
			Assert.assertEquals(expectedItems[i].getPrice(), actualItems[i].getPrice());
			Assert.assertEquals(expectedItems[i].getPriceName(), actualItems[i].getPriceName());
			Assert.assertEquals(expectedItems[i].getQuantity(), actualItems[i].getQuantity());
			Assert.assertEquals(expectedItems[i].getUuid(), actualItems[i].getUuid());
		}

		Assert.assertEquals(expected.getPayments().size(), actual.getPayments().size());
		Payment[] expectedPayments = new Payment[expected.getPayments().size()];
		expected.getPayments().toArray(expectedPayments);
		Payment[] actualPayments = new Payment[actual.getPayments().size()];
		actual.getPayments().toArray(actualPayments);
		for (int i = 0; i < expected.getPayments().size(); i++) {
			Assert.assertEquals(expectedPayments[i].getId(), actualPayments[i].getId());
			Assert.assertEquals(expectedPayments[i].getPaymentMode(), actualPayments[i].getPaymentMode());
			Assert.assertEquals(expectedPayments[i].getAmount(), actualPayments[i].getAmount());
			Assert.assertEquals(expectedPayments[i].getUuid(), actualPayments[i].getUuid());
		}
	}

	/**
	 * @verifies throw IllegalArgumentException if the receipt number is null
	 * @see IBillService#getBillByReceiptNumber(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getBillByReceiptNumber_shouldThrowIllegalArgumentExceptionIfTheReceiptNumberIsNull() throws Exception {
		service.getBillByReceiptNumber(null);
	}

	/**
	 * @verifies throw IllegalArgumentException if the receipt number is empty
	 * @see IBillService#getBillByReceiptNumber(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getBillByReceiptNumber_shouldThrowIllegalArgumentExceptionIfTheReceiptNumberIsEmpty() throws Exception {
		service.getBillByReceiptNumber("");
	}

	/**
	 * @verifies throw IllegalArgumentException if the receipt number is longer than 255 characters
	 * @see IBillService#getBillByReceiptNumber(String)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void getBillByReceiptNumber_shouldThrowIllegalArgumentExceptionIfTheReceiptNumberIsLongerThan255Characters() throws Exception {
		service.getBillByReceiptNumber(StringUtils.repeat("A", 256));
	}

	/**
	 * @verifies return the bill with the specified reciept number
	 * @see IBillService#getBillByReceiptNumber(String)
	 */
	@Test
	public void getBillByReceiptNumber_shouldReturnTheBillWithTheSpecifiedRecieptNumber() throws Exception {
		Bill bill = service.getBillByReceiptNumber("test 1 receipt number");
		Assert.assertNotNull(bill);

		Bill expected = service.getById(0);

		assertEntity(expected, bill);
	}

	/**
	 * @verifies return null if the receipt number is not found
	 * @see IBillService#getBillByReceiptNumber(String)
	 */
	@Test
	public void getBillByReceiptNumber_shouldReturnNullIfTheReceiptNumberIsNotFound() throws Exception {
		Bill bill = service.getBillByReceiptNumber("not a valid number");

		Assert.assertNull(bill);
	}

	@Test
	public void save_adjustedBill() throws Exception {
		Bill bill = createEntity(true);
		bill.setBillAdjusted(service.getById(0));
		service.save(bill);

		Context.flushSession();

		bill = service.getById(bill.getId());
		Assert.assertNotNull(bill);
		Assert.assertNotNull(bill.getBillAdjusted());

		Bill adjustedBill = service.getById(bill.getBillAdjusted().getId());
		Assert.assertNotNull(adjustedBill);
		Assert.assertEquals(BillStatus.ADJUSTED, adjustedBill.getStatus());
		Assert.assertTrue(adjustedBill.getAdjustedBy().size() > 0);

		boolean foundAdjustor = false;
		for (Bill adjustor : adjustedBill.getAdjustedBy()) {
			if (adjustor.getId() == bill.getId()) {
				foundAdjustor = true;
				break;
			}
		}

		Assert.assertTrue("Could not find the adjusting bill.", foundAdjustor);
	}

	/**
	 * @verifies throw NullPointerException if patient is null
	 * @see IBillService#findPatientBills(org.openmrs.Patient, org.openmrs.module.openhmis.cashier.api.util.PagingInfo)
	 */
	@Test(expected = NullPointerException.class)
	public void findPatientBills_shouldThrowNullPointerExceptionIfPatientIsNull() throws Exception {
		service.findPatientBills(null, null);
	}

	/**
	 * @verifies return all bills for the specified patient
	 * @see IBillService#findPatientBills(org.openmrs.Patient, org.openmrs.module.openhmis.cashier.api.util.PagingInfo)
	 */
	@Test
	public void findPatientBills_shouldReturnAllBillsForTheSpecifiedPatient() throws Exception {
		Patient patient = patientService.getPatient(0);

		List<Bill> bills = service.findPatientBills(patient, null);

		Assert.assertNotNull(bills);
		Assert.assertEquals(1, bills.size());
		assertEntity(service.getById(0), bills.get(0));

		bills = service.findPatientBills(patient.getId(), null);
		Assert.assertNotNull(bills);
		Assert.assertEquals(1, bills.size());
		assertEntity(service.getById(0), bills.get(0));
	}

	/**
	 * @verifies return an empty list if the specified patient has no bills
	 * @see IBillService#findPatientBills(org.openmrs.Patient, org.openmrs.module.openhmis.cashier.api.util.PagingInfo)
	 */
	@Test
	public void findPatientBills_shouldReturnAnEmptyListIfTheSpecifiedPatientHasNoBills() throws Exception {
		Patient patient = patientService.getPatient(1);

		List<Bill> bills = service.findPatientBills(patient, null);
		Assert.assertNotNull(bills);
		Assert.assertEquals(0, bills.size());

		bills = service.findPatientBills(1, null);
		Assert.assertNotNull(bills);
		Assert.assertEquals(0, bills.size());
	}

	/**
	 * @verifies throw IllegalArgumentException if the patientId is less than zero
	 * @see IBillService#findPatientBills(int, org.openmrs.module.openhmis.cashier.api.util.PagingInfo)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void findPatientBills_shouldThrowIllegalArgumentExceptionIfThePatientIdIsLessThanZero() throws Exception {
		service.findPatientBills(-1, null);
	}
}
