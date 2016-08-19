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
requirejs(['cashPoint/configs/entity.module'], function() {
    angular.bootstrap(document, ['entitiesApp']);
});

emr.loadMessages([
    "openhmis.cashier.cashPoint.name",
    "openhmis.cashier.cashPoint.rest_name",
    "openhmis.cashier.cashPoint.name.namePlural",
    "openhmis.cashier.cashPoint.new",
    "openhmis.cashier.cashPoint.retire",
    "openhmis.cashier.cashPoint.unretire",
    "openhmis.cashier.cashPoint.delete",
    "openhmis.cashier.cashPoint.enterSearchPhrase",
    "openhmis.cashier.cashPoint.created.success",
    "openhmis.cashier.cashPoint.updated.success",
    "openhmis.cashier.cashPoint.retired.success",
    "openhmis.cashier.cashPoint.unretired.success",
    "openhmis.cashier.cashPoint.confirm.delete",
    "openhmis.cashier.cashPoint.deleted.success",
    "openhmis.cashier.cashPoint.name.required",
    "openhmis.cashier.cashPoint.location.required",
    "openhmis.cashier.cashPoint.error.duplicate",
    "openhmis.cashier.cashPoint.retireReason.required",
    "openhmis.cashier.cashPoint.error.notFound",
    "openhmis.cashier.cashPoint.retired.reason",
    "openhmis.cashier.location.name",
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
    "openhmis.cashier.cashPoint.name.required",
    "openhmis.commons.general.postSearchMessage",
    "openhmis.commons.general.anyLocation",
]);
