/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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

import org.openmrs.Location;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.openhmis.cashier.api.model.CashPoint;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICashPointService extends IMetadataDataService<CashPoint> {
    /**
     * Gets all the cashpoints for the specified {@link Location}.
     * @param location The location.
     * @param includeRetired Whether retired cashpoints should be included in the results.
     * @return All cashpoints for the specified {@link Location}.
     * @throws APIException
     * @should throw IllegalArgumentException if the location is null
     * @should return an empty list if the location has no cashpoints
     * @should not return retired cashpoints unless specified
     * @should return all cashpoints for the specified location
     */
    @Transactional(readOnly = true)
    @Authorized( { PrivilegeConstants.MANAGE_METADATA})
    List<CashPoint> getCashPointsByLocation(Location location, boolean includeRetired) throws APIException;

    /**
     * Gets all the cashpoints for the specified {@link org.openmrs.Location}.
     * @param location The location.
     * @param includeRetired Whether retired cashpoints should be included in the results.
     * @param pagingInfo The paging information
     * @return All cashpoints for the specified {@link Location}.
     * @throws org.openmrs.api.APIException
     */
    @Transactional(readOnly = true)
    @Authorized( { PrivilegeConstants.MANAGE_METADATA})
    List<CashPoint> getCashPointsByLocation(Location location, boolean includeRetired, PagingInfo pagingInfo) throws APIException;

    /**
     * Gets all cashpoints in the specified {@link Location} that start with the specified name.
     * @param location The location to search within.
     * @param name The cashpoints name fragment.
     * @param includeRetired Whether retired cashpoints should be included in the results.
     * @return All cashpoints in the specified {@link Location} that start with the specified name.
     * @throws APIException
     * @should throw IllegalArgumentException if the location is null
     * @should throw IllegalArgumentException if the name is null
     * @should throw IllegalArgumentException if the name is empty
     * @should throw IllegalArgumentException if the name is longer than 255 characters
     * @should return an empty list if no cashpoints are found
     * @should not return retired cashpoints unless specified
     * @should return cashpoints that start with the specified name
     * @should return cashpoints for only the specified location
     */
    @Transactional(readOnly = true)
    @Authorized( { PrivilegeConstants.MANAGE_METADATA})
    List<CashPoint> getCashPointsByLocationAndName(Location location, String name, boolean includeRetired) throws APIException;

    /**
     * Gets all cashpoints in the specified {@link Location} that start with the specified name.
     * @param location The location to search within.
     * @param name The cashpoints name fragment.
     * @param includeRetired Whether retired cashpoints should be included in the results.
     * @param pagingInfo The paging information.
     * @return All cashpoints in the specified {@link Location} that start with the specified name.
     * @throws APIException
     */
    @Transactional(readOnly = true)
    @Authorized( { PrivilegeConstants.MANAGE_METADATA})
    List<CashPoint> getCashPointsByLocationAndName(Location location, String name, boolean includeRetired, PagingInfo pagingInfo) throws APIException;
}

