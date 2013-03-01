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
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.Provider;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.ITimesheetService;
import org.openmrs.module.openhmis.cashier.api.model.Timesheet;
import org.openmrs.module.openhmis.commons.api.entity.IEntityDataService;
import org.openmrs.module.openhmis.commons.api.ProviderHelper;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Date;
import java.util.List;

@Resource("timesheet")
@Handler(supports = { Timesheet.class }, order = 0)
public class TimesheetResource extends BaseRestDataResource<Timesheet> {
	@Override
	public Timesheet newDelegate() {
		return new Timesheet();
	}

	@Override
	public Class<? extends IEntityDataService<Timesheet>> getServiceClass() {
		return ITimesheetService.class;
	}

	public SimpleObject search(Date date, RequestContext context) throws ResponseException {
		ITimesheetService service = (ITimesheetService) Context.getService(getServiceClass());
		Provider provider = ProviderHelper.getCurrentProvider();
		if (provider == null) {
			return null;
		}

		List<Timesheet> timesheets = service.getTimesheetsByDate(provider, date);
		PageableResult results = new AlreadyPagedWithLength<Timesheet>(context, timesheets, false, timesheets.size());
		return results.toSimpleObject();
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
