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

(function() {
	'use strict';

	angular.module('app.restfulServices').service('CashierBillRestfulService', CashierBillRestfulService);

	CashierBillRestfulService.$inject = ['EntityRestFactory', 'CashierBillFunctions'];

	function CashierBillRestfulService(EntityRestFactory, CashierBillFunctions) {
		
		var service;
		var CASHIER_PAGE_URL = ROOT_URL + '/module/openhmis/cashier/';
		
		service = {
			getPaymentModes: getPaymentModes,
			loadPaymentModeAttributes: loadPaymentModeAttributes,
			searchPerson: searchPerson,
			searchItems: searchItems,
			loadItemDetails: loadItemDetails,
			setBaseUrl: setBaseUrl,
			loadBill: loadBill,
			processPayment: processPayment,
			getRoundingItem: getRoundingItem,
			getTimesheet: getTimesheet,
			getCashier: getCashier,
			getCashPoints: getCashPoints,
			checkAdjustmentReasonRequired: checkAdjustmentReasonRequired,
			checkAllowBillAdjustment: checkAllowBillAdjustment,
			checkAutofillPaymentAmount: checkAutofillPaymentAmount,
		};

		return service;

		/**
		 * Retrieve payment modes
		 * @param onLoadPaymentModesSuccessful
		 */
		function getPaymentModes(module_name, onLoadPaymentModesSuccessful) {
			setBaseUrl(module_name);
			var requestParams = [];
			requestParams['rest_entity_name'] = 'paymentMode';
			EntityRestFactory.loadEntities(requestParams,
				onLoadPaymentModesSuccessful,
				errorCallback
			);
		}

		function loadPaymentModeAttributes(module_name, uuid, onLoadPaymentModeAttributesSuccessful) {
			setBaseUrl(module_name);
			var requestParams = {};
			requestParams['rest_entity_name'] = 'paymentMode/' + uuid;
			EntityRestFactory.loadEntities(requestParams,
				onLoadPaymentModeAttributesSuccessful,
				errorCallback
			);
		}

		function loadItemDetails(uuid, lineItem) {
			setBaseUrl('inventory');
			var requestParams = {};
			requestParams['rest_entity_name'] = 'item/' + uuid;
			EntityRestFactory.loadEntities(requestParams,
				function(data) {
					lineItem.prices = data.prices;
					CashierBillFunctions.reOrderItemPrices(lineItem, data)
				},
				errorCallback
			);
		}

		function searchPerson(q, type) {
			var requestParams = [];
			requestParams['q'] = q;
			if (type === 'patient') {
				requestParams['v'] = "custom:(patientIdentifier:(uuid,identifier)," +
					"person:(personName))";
			}

			return EntityRestFactory.autocompleteSearch(requestParams, type, 'inventory', 'v1');
		}

		function searchItems(q) {
			setBaseUrl("inventory");
			var requestParams = {};
			requestParams['q'] = q;
			requestParams['limit'] = 15;
			requestParams['startIndex'] = 1;
			return EntityRestFactory.autocompleteSearch(requestParams, 'item');
		}

		function loadBill(module_name, uuid, onLoadBillSuccessful) {
			setBaseUrl(module_name);
			var requestParams = {};
			requestParams['rest_entity_name'] = 'bill/' + uuid;
			EntityRestFactory.loadEntities(requestParams,
				onLoadBillSuccessful,
				errorCallback
			);
		}

		function processPayment(module_name, billUuid, payment, onProcessPaymentSuccessful) {
			setBaseUrl(module_name);
			EntityRestFactory.post('bill/' + billUuid + '/payment', '',
				payment,
				onProcessPaymentSuccessful,
				errorCallback
			);
		}

		function getRoundingItem(onLoadRoundingItemSuccessful) {
			var requestParams = [];
			requestParams['resource'] = 'options.json';
			EntityRestFactory.setCustomBaseUrl(CASHIER_PAGE_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadRoundingItemSuccessful, errorCallback);
		}

		function getTimesheet(onLoadTimesheetSuccessful) {
			var requestParams = [];
			requestParams['resource'] = CASHIER_MODULE_SETTINGS_URL;
			requestParams['setting'] = 'timesheet';
			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadTimesheetSuccessful, errorCallback);
		}

		function checkAdjustmentReasonRequired(onLoadAdjustmentReasonSuccessful) {
			var requestParams = [];
			requestParams['resource'] = CASHIER_MODULE_SETTINGS_URL;
			requestParams['setting'] = 'openhmis.cashier.adjustmentReasonField';
			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadAdjustmentReasonSuccessful, errorCallback);
		}

		function checkAllowBillAdjustment(onLoadAllowBillAdjustmentSuccessful) {
			var requestParams = [];
			requestParams['resource'] = CASHIER_MODULE_SETTINGS_URL;
			requestParams['setting'] = 'openhmis.cashier.allowBillAdjustments';
			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadAllowBillAdjustmentSuccessful, errorCallback);
		}

		function checkAutofillPaymentAmount(onLoadAutofillPaymentAmountSuccessful) {
			var requestParams = [];
			requestParams['resource'] = CASHIER_MODULE_SETTINGS_URL;
			requestParams['setting'] = 'openhmis.cashier.autofillPaymentAmount';
			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadAutofillPaymentAmountSuccessful, errorCallback);
		}

		function getCashier(url, cashierUuid, onLoadCashierSuccessful) {
			url = url.split(cashierUuid).join("");
			var requestParams = [];
			requestParams['resource'] = cashierUuid;
			EntityRestFactory.setCustomBaseUrl(url);
			EntityRestFactory.loadResults(requestParams,
				onLoadCashierSuccessful, errorCallback);
		}

		function getCashPoints(module_name, onLoadCashPointsSuccessful) {
			setBaseUrl(module_name);
			var requestParams = {};
			requestParams['rest_entity_name'] = 'cashPoint';
			EntityRestFactory.loadEntities(requestParams,
				onLoadCashPointsSuccessful,
				errorCallback
			);
		}

		function setBaseUrl(module_name) {
			EntityRestFactory.setBaseUrl(module_name);
		}
		
		function errorCallback(error){
			emr.errorAlert(error);
		}
	}
})();
