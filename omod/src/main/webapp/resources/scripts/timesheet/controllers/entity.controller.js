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
	TimesheetController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory', 'TimesheetModel',
		'TimesheetRestfulService', 'TimesheetFunctions', '$window', '$location'];
	
	function TimesheetController($stateParams, $injector, $scope, $filter, EntityRestFactory, TimesheetModel,
	                             TimesheetRestfulService, TimesheetFunctions, $window, $location) {
		var self = this;
		
		var entity_name_message_key = "openhmis.cashier.page.timesheet";
		var REST_ENTITY_NAME = "timesheet";
		var TIMESHEET_ACCESS_DENIED_PAGE_URL = 'entities.page#/accessDenied';
		var REDIRECT_URL;
		
		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters
			|| function () {
				self.checkPrivileges(PRIVILEGE_CASHIER_TIMESHEETS);
				self.bindBaseParameters(CASHIER_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key,
					CASHIER_LANDING_PAGE_URL);
			}
		
		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
		// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function (uuid) {
				REDIRECT_URL =  $location.search().redirectUrl;
				self.loadCurrentProvider();
				self.loadCashpoints();
				self.loadCurrentTimesheets();
				self.loadCashierShiftReportId();
				$scope.showTimesheetRow = false;
				$scope.generateCashierShiftReport = self.generateCashierShiftReport;
				
				$scope.loadClockOutTime = function () {
					if ($scope.timesheets != null && $scope.timesheets.clockOut == null) {
						$scope.clockOut = TimesheetFunctions.formatDate(new Date());
					}
				}
				
				$scope.loadClockInTime = function () {
					$scope.clockIn = TimesheetFunctions.formatDate(new Date());
				}
				
				TimesheetFunctions.onChangeDatePicker('shiftDate-display',
					self.onTimesheetShiftReportDateSuccessCallback);
				
				$scope.selectedTimesheet = function (timesheetId) {
					$scope.timesheetId = timesheetId;
				}
				
				$scope.generateReport = function () {
					var contextPath = ROOT_URL;
					var url =  REPORTS_PAGE_URL + "reportId=" + $scope.cashierShiftReportId + "&timesheetId=" + $scope.timesheetId;
					window.open(contextPath + url, "pdfDownload");
				}
			};
		
		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
		self.onTimesheetShiftReportDateSuccessCallback = self.onTimesheetShiftReportDateSuccessCallback || function (data) {
				$scope.selectedReportDate = data;
				var selectedReportDate = TimesheetFunctions.formatDate(data);
				TimesheetRestfulService.loadTimesheet(CASHIER_MODULE_NAME, self.onLoadSelectedReportDateTimesheetSuccessful, selectedReportDate);
			}
		
		self.onLoadSelectedReportDateTimesheetSuccessful = self.onLoadSelectedReportDateTimesheetSuccessful || function (data) {
				$scope.selectedDatesTimesheet = data.results;
				$scope.showTimesheetRow = data.results.length != 0;
			}
		
		self.loadCashierShiftReportId = self.loadCashierShiftReportId || function () {
				TimesheetRestfulService.loadCashierShiftReportId(CASHIER_MODULE_NAME, self.onLoadCashierShiftReportIdSuccessful);
			}
		
		self.onLoadCashierShiftReportIdSuccessful = self.onLoadCashierShiftReportIdSuccessful || function (data) {
				$scope.cashierShiftReportId = data.results;
			}
		
		self.loadCashpoints = self.loadCashpoints || function () {
				TimesheetRestfulService.loadCashpoints(CASHIER_MODULE_NAME, self.onLoadCashpointsSuccessful);
			}
		self.loadCurrentProvider = self.loadCurrentProvider || function () {
				TimesheetRestfulService.loadProvider(CASHIER_MODULE_NAME, self.onLoadProviderSuccessful);
			}
		self.loadCurrentTimesheets = self.loadCurrentTimesheets || function () {
				TimesheetRestfulService.loadTimesheet(CASHIER_MODULE_NAME, self.onloadCurrentTimesheetSuccessful, TimesheetFunctions.formatDate(new Date()));
			}
		
		//callback
		self.onLoadCashpointsSuccessful = self.onLoadCashpointsSuccessful || function (data) {
				$scope.cashpoints = data.results;
			}
		
		self.onLoadProviderSuccessful = self.onLoadProviderSuccessful || function (data) {
				if (data.currentProvider == null) {
					$window.location.replace(TIMESHEET_ACCESS_DENIED_PAGE_URL);
				} else {
					$scope.cashier = data.currentProvider.uuid;
					$scope.accessDenied = false;
				}
			}
		
		self.generateCashierShiftReport = self.generateCashierShiftReport || function (id) {
				TimesheetFunctions.generateCashierShiftReport(id, $scope);
			}
		
		self.onloadCurrentTimesheetSuccessful = self.onloadCurrentTimesheetSuccessful || function (data) {
				$scope.timesheets = data.results[0];
				//Get the latest timesheet for the day if multiple exist
				if ($scope.timesheets != undefined) {
					//check if the timesheet exists and has a clockOut time filled
					if ($scope.timesheets.clockOut != null) {
						$scope.clockIn = TimesheetFunctions.formatDate(new Date());
						$scope.showClockOutSection = false;
						$scope.clockOut = "";
					} else {
						$scope.clockIn = TimesheetFunctions.formatDate(new Date(data.results[0].clockIn));
						$scope.clockOut = "";
						$scope.showClockOutSection = true;
						$scope.entity.cashPoint = data.results[0].cashPoint;
						$scope.entity.uuid = data.results[0].uuid;
						$scope.entity.id = data.results[0].id;
					}
				} else {
					$scope.clockIn = TimesheetFunctions.formatDate(new Date());
					$scope.clockOut = "";
					$scope.showClockOutSection = false;
				}
			}

		self.cancel = self.cancel || function() {
				if (REDIRECT_URL !== undefined) {
					window.location = REDIRECT_URL;
				} else {
					window.location = CASHIER_LANDING_PAGE_URL;
				}
			}
		
		// @Override
		self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
				$scope.submitted = false;
				if (!angular.isDefined($scope.clockOut) || $scope.clockOut == "") {
					$scope.entity.clockOut = null;
				}
				
				if (!angular.isDefined($scope.clockIn) || $scope.clockIn == "") {
					emr.errorAlert(emr.message("openhmis.cashier.page.reports.box.select.clock.in.error"));
					return false;
				} else {
					$scope.entity.clockIn = TimesheetFunctions.convertToDate($scope.clockIn);
				}
				
				if (!angular.isDefined($scope.entity.cashPoint) || $scope.entity.cashPoint == null
					|| $scope.entity.cashPoint == "") {
					emr.errorAlert(emr.message("openhmis.cashier.page.timesheet.box.cashpoint.empty"));
					return false;
				}
				
				if ($scope.entity.clockOut == null && $scope.entity.cashPoint != "") {
					emr.successAlert(emr.message("openhmis.cashier.page.timesheet.box.clockIn.message"));
				}
				
				if ($scope.clockOut != "" && $scope.entity.cashPoint != null) {
					$scope.entity.clockOut = TimesheetFunctions.convertToDate($scope.clockOut);
					emr.successAlert(emr.message("openhmis.cashier.page.timesheet.box.clockOut.message"));
				}
				
				/**
				 * Performs checks to either get the current logged in cashier
				 * or the cashier the started the timesheet.
				 * */
				if ($scope.timesheets) {
					if ($scope.timesheets.clockOut != null) {
						$scope.entity.cashier = $scope.cashier;
					} else {
						$scope.entity.cashier = $scope.timesheets.cashier;
					}
				} else {
					$scope.entity.cashier = $scope.cashier;
				}
				
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
