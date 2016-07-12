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
package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Location;

/**
 * Model class that represents a location where {@link org.openmrs.module.openhmis.cashier.api.model.Bill}'s can be created
 * and paid for.
 */
public class CashPoint extends BaseOpenmrsMetadata {
	public static final long serialVersionUID = 0L;

	private Integer cashPointId;
	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Integer getId() {
		return this.cashPointId;
	}

	@Override
	public void setId(Integer id) {
		this.cashPointId = id;
	}

	public String toString() {
		return getName();
	}
}
