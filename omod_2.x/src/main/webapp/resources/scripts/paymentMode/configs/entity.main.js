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
requirejs(['paymentMode/configs/entity.module'], function() {
    angular.bootstrap(document, ['entitiesApp']);
});

emr.loadMessages([
    "openhmis.cashier.paymentMode.name",
    "openhmis.cashier.paymentMode.rest_name",
    "openhmis.cashier.paymentMode.add.attributeType",
    "openhmis.cashier.paymentMode.edit.attributeType",
    "openhmis.cashier.paymentMode.name.required",
    "openhmis.cashier.paymentMode.sortOrder.required",
    "openhmis.cashier.paymentMode.attributeTypes.required",
    "openhmis.cashier.general.attributeTypeInUse.error",
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
    "openhmis.commons.general.postSearchMessage",
    "openhmis.commons.general.anyLocation"
]);
