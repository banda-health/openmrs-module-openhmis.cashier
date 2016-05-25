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
	TimesheetController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'TimesheetModel', 'TimesheetRestfulService', 'TimesheetFunctions'];
	
	function TimesheetController($stateParams, $injector, $scope, $filter, EntityRestFactory, TimesheetModel, TimesheetRestfulService, TimesheetFunctions) {
		var self = this;

		var module_name = 'cashier';
		var entity_name_message_key = emr.message("openhmis.cashier.page.timesheet");
		var rest_entity_name = emr.message("openhmis.cashier.page.timesheet.rest_name");
		var cancel_page = '#';

		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters
			|| function () {
				self.bindBaseParameters(module_name, rest_entity_name,
					entity_name_message_key, cancel_page);
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
				
				$scope.loadClockOutTime = function() {
						if ($scope.timesheets != null && $scope.timesheets[0].clockOut == null ) {
							$scope.entity.clockOut = TimesheetFunctions.formatDate(new Date);
						}
					}

				$scope.loadClockInTime = function () {
						$scope.entity.clockIn = TimesheetFunctions.formatDate(new Date());
				}
				
				$scope.getTimesheets = function(){
						$scope.shiftDate = document.getElementById('shiftDate').val();
					}
				
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
				TimesheetRestfulService.loadTimesheet(module_name, self.onloadCurrentTimesheetSuccessful,timesheetDate);
			}
		
		//callback
		self.onLoadCashpointsSuccessful = self.onLoadCashpointsSuccessful || function(data){
				$scope.cashpoints = data.results;
			}

		self.onloadCurrentTimesheetSuccessful = self.onloadCurrentTimesheetSuccessful || function(data) {
				$scope.timesheets = data.results;
				/*Get the latest timesheet for the day if multiple exist*/
				if ($scope.timesheets) {
					//check if the timesheet exists and has a clockOut time filled
					if (data.results[0].clockOut != null) {
						$scope.entity.clockIn = TimesheetFunctions.formatDate(new Date);
						$scope.entity.clockOut = "";
					} else {
						$scope.entity.clockIn = TimesheetFunctions.formatDate(new Date(data.results[0].clockIn));
						$scope.entity.clockOut = "";
					}
					$scope.entity.cashPoint = data.results[0].cashPoint;
				} else {
					$scope.entity.clockIn = TimesheetFunctions.formatDate(new Date());
					$scope.entity.clockOut = "";
				}
			}
		
		self.onloadTimesheetWithGivenDateSuccessfull = self.onloadTimesheetWithGivenDateSuccessfull || function(data) {
				
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
