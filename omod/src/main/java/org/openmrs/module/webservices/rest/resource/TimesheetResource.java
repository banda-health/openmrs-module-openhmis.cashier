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
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Resource(name=RestConstants.VERSION_2 + "/cashier/timesheet", supportedClass=Timesheet.class, supportedOpenmrsVersions={"1.9"})
public class TimesheetResource extends BaseRestDataResource<Timesheet> {
	@Override
	public Timesheet newDelegate() {
		return new Timesheet();
	}

	@Override
	public Class<? extends IEntityDataService<Timesheet>> getServiceClass() {
		return ITimesheetService.class;
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof RefRepresentation) {
			description.addProperty("id");
		}

		return description;
	}

	public String getDisplayString(Timesheet instance) {
		return instance.getClockIn().toString() + " to " + (instance.getClockOut() != null ? instance.getClockOut() : " open");
	}
}
