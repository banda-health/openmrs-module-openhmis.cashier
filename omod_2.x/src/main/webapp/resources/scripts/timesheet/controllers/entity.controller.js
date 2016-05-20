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
	
	var base = angular.module('app.genericManageController');
	base.controller("TimesheetController", TimesheetController);
	TimesheetController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'TimesheetModel', 'TimesheetRestfulService', 'TimesheetFunctions'];
	
	function TimesheetController($stateParams, $injector, $scope, $filter, EntityRestFactory, TimesheetModel, TimesheetRestfulService, TimesheetFunctions) {
		var self = this;

		var module_name = 'cashier';
		var entity_name = emr.message("openhmis.cashier.page.timesheet");
		var rest_entity_name = emr.message("openhmis.cashier.page.timesheet.rest_name");

		// @Override
		self.getModelAndEntityName = self.getModelAndEntityName || function() {
				self.bindBaseParameters(module_name, rest_entity_name, entity_name);
			}
		
		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
		// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function (uuid) {
				self.loadCashpoints();
				self.loadCurrentTimesheets();
			};
		
		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
		self.loadCashpoints = self.loadCashpoints || function(){
				TimesheetRestfulService.loadCashpoints(module_name, self.onLoadCashpointsSuccessful);
			}
		self.loadCurrentTimesheets = self.loadCurrentTimesheets || function() {
				var timesheetDate = TimesheetFunctions.formatDate(new Date());
				TimesheetRestfulService.loadCurrentTimesheet(module_name, self.onloadCurrentTimesheetsuccessful,timesheetDate);
			}
		
		//callback
		self.onLoadCashpointsSuccessful = self.onLoadCashpointsSuccessful || function(data){
				$scope.cashpoints = data.results;
			}

		self.onloadCurrentTimesheetsuccessful = self.onloadCurrentTimesheetsuccessful || function(data) {
				$scope.timesheets = data.results;

				/*Get the latest timesheet for the day if multiple*/
				$scope.clockIn = TimesheetFunctions.formatDate(new Date(data.results[0].clockIn));
				$scope.clockOut = TimesheetFunctions.formatDate(new Date(data.results[0].clockOut));
				$scope.cashpointUuid = data.results[0].cashPoint.name;
			}

		
		// @Override
		self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
				$scope.submitted = false;
				return true;
			}
		
		/* ENTRY POINT: Instantiate the base controller which loads the page */
		$injector.invoke(base.GenericManageController, self, {
			$scope: $scope,
			$filter: $filter,
			$stateParams: $stateParams,
			EntityRestFactory: EntityRestFactory,
			GenericMetadataModel: TimesheetModel
		});
	}
})();
