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

(function () {
	'use strict';
	
	angular.module('app.restfulServices').service('TimesheetRestfulService', TimesheetRestfulService);
	
	TimesheetRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];
	
	function TimesheetRestfulService(EntityRestFactory, PaginationService) {
		var service;
		
		service = {
			loadCashpoints: loadCashpoints,
			loadTimesheet: loadTimesheet,
			loadProvider: loadProvider,
			loadCashierShiftReportId: loadCashierShiftReportId
		};
		
		return service;
		
		/**
		 * Retrieve all cashpoints
		 * @param onLoadCashpointsSuccessful
		 * @param module_name
		 */
		function loadCashpoints(module_name, onLoadCashpointsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'cashPoint';
			EntityRestFactory.loadEntities(requestParams,
				onLoadCashpointsSuccessful,
				errorCallback
			);
		}
		
		/**
		 * Retrieve Timesheets
		 * @param module_name
		 * @param onLoadTimesheetSuccessfull
		 * @param timesheetDate
		 * */
		function loadTimesheet(module_name, onLoadTimesheetSuccessful, timesheetDate) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'timesheet';
			requestParams['date'] = timesheetDate;
			EntityRestFactory.loadEntities(requestParams,
				onLoadTimesheetSuccessful,
				errorCallback);
		}
		
		/**
		 * Retrieve the current provider
		 * @param onLoadProviderSuccessful
		 * @param module_name
		 */
		function loadProvider(module_name, onLoadProviderSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = '';
			EntityRestFactory.setBaseUrl('appui/session', 'v1');
			EntityRestFactory.loadEntities(requestParams,
				onLoadProviderSuccessful,
				errorCallback
			);
			
			//reset base url..
			EntityRestFactory.setBaseUrl(module_name);
		}
		
		/**
		 * Retrieve the report Id for the cashier shift
		 * */
		function loadCashierShiftReportId(module_name, onLoadCashierShiftReportIdSuccessful) {
			var requestParams = [];
			requestParams['resource'] = CASHIER_MODULE_SETTINGS_URL;
			requestParams['setting'] = 'openhmis.cashier.defaultShiftReportId';
			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams,
				onLoadCashierShiftReportIdSuccessful,
				errorCallback
			);
			
			//reset base url..
			EntityRestFactory.setBaseUrl(module_name);
		}
		
		function errorCallback(error) {
			emr.errorAlert(error);
		}
	}
})();
