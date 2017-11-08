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
 *
 */

/* initialize and bootstrap application */
requirejs(['timesheet/configs/entity.module'], function () {
	angular.bootstrap(document, ['entitiesApp']);
});

emr.loadMessages([
	"Location.hierarchy.heading",
	"general.edit",
	"general.new",
	"general.name",
	"general.description",
	"general.cancel",
	"general.save",
	"general.retireReason",
	"general.purge",
	"general.retire",
	"general.unretire",
	"openhmis.cashier.page.timesheet.rest_name",
	"openhmis.cashier.page.timesheet.box.clockIn.message",
	"openhmis.cashier.page.timesheet.box.clockOut.message",
	"openhmis.cashier.page.reports.box.timesheets.shift.date.error",
	"openhmis.cashier.page.timesheet.box.cashpoint.empty",
	"openhmis.cashier.page.reports.box.select.clock.in.error",
	"openhmis.cashier.timesheet.entry.error.notProvider"
]);
