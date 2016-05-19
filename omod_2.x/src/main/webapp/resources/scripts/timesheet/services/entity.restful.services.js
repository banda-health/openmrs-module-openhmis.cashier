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
			loadCashpoints : loadCashpoints,
			loadCurrentTimesheet: loadCurrentTimesheet,
		};
		
		return service;
		
		/**
		 * Retrieve all cashpoints
		 * @param onLoadCashpointsSuccessful
		 */
		function loadCashpoints(module_name, onLoadCashpointsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'cashPoint';
			EntityRestFactory.loadEntities(requestParams,
				onLoadCashpointsSuccessful,
				errorCallback
			);

			//reset base url..
			EntityRestFactory.setBaseUrl(module_name);
		}

		function loadCurrentTimesheet(module_name, onLoadTimesheetSuccessful, timesheetDate) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'timesheet?date=' + timesheetDate +'&v=full';
			EntityRestFactory.loadEntities(requestParams, onLoadTimesheetSuccessful, errorCallback);
			
			//reset base url..
			EntityRestFactory.setBaseUrl(module_name);
		}

		function errorCallback(error){
			console.log(error);
		}
	}
})();
