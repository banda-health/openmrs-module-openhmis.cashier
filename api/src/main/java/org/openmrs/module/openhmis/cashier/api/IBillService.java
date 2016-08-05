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

import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.search.BillSearch;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface that represents classes which perform data operations for {@link Bill}s.
 */
@Transactional
public interface IBillService extends IEntityDataService<Bill> {
	/**
	 * Gets the {@link Bill} with the specified receipt number or {@code null} if not found.
	 * @param receiptNumber The receipt number to search for.
	 * @return The {@link Bill} with the specified receipt number or {@code null}.
	 * @should throw IllegalArgumentException if the receipt number is null
	 * @should throw IllegalArgumentException if the receipt number is empty
	 * @should throw IllegalArgumentException if the receipt number is longer than 255 characters
	 * @should return the bill with the specified reciept number
	 * @should return null if the receipt number is not found
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_BILLS })
	Bill getBillByReceiptNumber(String receiptNumber);

	/**
	 * Returns all {@link Bill}s for the specified patient with the specified paging.
	 * @param patient The {@link Patient}.
	 * @param paging The paging information.
	 * @return All of the bills for the specified patient.
	 * @should throw NullPointerException if patient is null
	 * @should return all bills for the specified patient
	 * @should return an empty list if the specified patient has no bills
	 */
	List<Bill> getBillsByPatient(Patient patient, PagingInfo paging);

	/**
	 * Returns all {@link Bill}s for the specified patient with the specified paging.
	 * @param patientId The patient id.
	 * @param paging The paging information.
	 * @return All of the bills for the specified patient.
	 * @should throw IllegalArgumentException if the patientId is less than zero
	 * @should throw NullPointerException if patient is null
	 * @should return all bills for the specified patient
	 * @should return an empty list if the specified patient has no bills
	 */
	List<Bill> getBillsByPatientId(int patientId, PagingInfo paging);

	/**
	 * Gets all bills using the specified {@link BillSearch} settings.
	 * @param billSearch The bill search settings.
	 * @return The bills found or an empty list if no bills were found.
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_BILLS })
	List<Bill> getBills(BillSearch billSearch);

	/**
	 * Gets all bills using the specified {@link BillSearch} settings.
	 * @param billSearch The bill search settings.
	 * @param pagingInfo The paging information.
	 * @return The bills found or an empty list if no bills were found.
	 * @should throw NullPointerException if bill search is null
	 * @should throw NullPointerException if bill search template object is null
	 * @should return an empty list if no bills are found via the search
	 * @should return bills filtered by cashier
	 * @should return bills filtered by cash point
	 * @should return bills filtered by patient
	 * @should return bills filtered by status
	 * @should return all bills if paging is null
	 * @should return paged bills if paging is specified
	 * @should not return retired bills from search unless specified
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_BILLS })
	List<Bill> getBills(BillSearch billSearch, PagingInfo pagingInfo);

	@Override
	@Authorized(PrivilegeConstants.VIEW_BILLS)
	Bill getByUuid(String uuid);
}
