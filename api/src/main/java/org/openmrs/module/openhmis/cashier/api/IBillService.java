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

import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants;
import org.openmrs.module.openhmis.cashier.api.util.PagingInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IBillService extends IDataService<Bill> {
	/**
	 * Gets the {@link Bill} with the specified receipt number or {@code null} if not found.
	 * @param receiptNumber The receipt number to search for.
	 * @return The {@link Bill} with the specified receipt number or {@code null}.
	 * @throws APIException
	 * @should throw IllegalArgumentException if the receipt number is null
	 * @should throw IllegalArgumentException if the receipt number is empty
	 * @should throw IllegalArgumentException if the receipt number is longer than 255 characters
	 * @should return the bill with the specified reciept number
	 * @should return null if the receipt number is not found
	 */
	@Transactional(readOnly =  true)
	@Authorized( {CashierPrivilegeConstants.VIEW_BILLS})
	Bill getBillByReceiptNumber(String receiptNumber) throws APIException;

	/**
	 *  Returns all {@link Bill}s for the specified patient with the specified paging.
	 * @param patient The {@link Patient}.
	 * @param paging The paging information.
	 * @return All of the bills for the specified patient.
	 * @should throw NullPointerException if patient is null
	 * @should return all bills for the specified patient
	 * @should return an empty list if the specified patient has no bills
	 */
	List<Bill> findPatientBills(Patient patient, PagingInfo paging);

	/**
	 *  Returns all {@link Bill}s for the specified patient with the specified paging.
	 * @param patientId The patient id.
	 * @param paging The paging information.
	 * @return All of the bills for the specified patient.
	 * @should throw IllegalArgumentException if the patientId is less than zero
	 * @should throw NullPointerException if patient is null
	 * @should return all bills for the specified patient
	 * @should return an empty list if the specified patient has no bills
	 */
	List<Bill> findPatientBills(int patientId, PagingInfo paging);
}
