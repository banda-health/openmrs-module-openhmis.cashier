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
	
	var base = angular.module('app.genericEntityController');
	base.controller("TimesheetController", TimesheetController);
	TimesheetController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'TimesheetModel', 'TimesheetRestfulService'];
	
	function TimesheetController($stateParams, $injector, $scope, $filter, EntityRestFactory, TimesheetModel, TimesheetRestfulService) {
		var self = this;
		
		var module_name = 'cashier';
		var entity_name_message_key = "openhmis.cashier.page.timesheet";
		var rest_entity_name = emr.message("openhmis.cashier.page.timesheet.rest_name");
		
		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
				self.bindBaseParameters(module_name, rest_entity_name, entity_name_message_key);
			};
		// @Override
		self.getModelAndEntityName = self.getModelAndEntityName || function() {
				self.bindBaseParameters(module_name, rest_entity_name, entity_name_message_key);
			};
		
		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
		// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function (uuid) {
				self.loadCashpoints();
				self.loadTimesheets();
			};
		
		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
		self.loadCashpoints = self.loadCashpoints || function(){
				TimesheetRestfulService.loadCashpoints(module_name, self.onLoadCashpointsSuccessful);
			}
		var timesheetDate = "05/20/2016 14:53";
		self.loadTimesheets = self.loadTimesheets || function() {
				TimesheetRestfulService.loadCurrentTimesheet(module_name, self.onLoadTimesheetSuccessful,timesheetDate);
			}
		
		//callback
		self.onLoadCashpointsSuccessful = self.onLoadCashpointsSuccessful || function(data){
				$scope.cashpoints = data.results;
			}

		self.onLoadTimesheetSuccessful = self.onLoadTimesheetSuccessful || function(data) {
				$scope.timesheets = data.results;
				console.log("#########################################################");
				console.log(data.results);
				var dates = data.results[0].display;
				var clockIn = dates.split(" to", 1);
				var clockOut = dates.split(" to ");
				$scope.entity.clockIn = new Date(clockIn);
				$scope.entity.clockOut = new Date(clockOut[clockOut.length-1]);
			}
		
		// @Override
		self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
				$scope.submitted = false;
				return true;
			}
		
		/* ENTRY POINT: Instantiate the base controller which loads the page */
		$injector.invoke(base.GenericEntityController, self, {
			$scope: $scope,
			$filter: $filter,
			$stateParams: $stateParams,
			EntityRestFactory: EntityRestFactory,
			GenericMetadataModel: TimesheetModel
		});
	}
})();
