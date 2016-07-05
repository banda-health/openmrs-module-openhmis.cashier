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
	base.controller("CashierBillController", CashierBillController);
	CashierBillController.$inject = ['$window', '$sce', '$stateParams', '$injector', '$scope', 'CookiesService',
		'$filter', 'EntityRestFactory', 'CashierBillModel', 'CashierBillRestfulService',
		'CommonsRestfulFunctions', 'PaginationService', 'LineItemModel', 'CashierBillFunctions', 'EntityFunctions'];

	function CashierBillController($window, $sce, $stateParams, $injector, $scope, CookiesService, $filter,
	                               EntityRestFactory, CashierBillModel,
	                               CashierBillRestfulService, CommonsRestfulFunctions,
	                               PaginationService, LineItemModel, CashierBillFunctions, EntityFunctions) {
		var self = this;

		var module_name = 'cashier';
		var entity_name_message_key = "openhmis.cashier.bill";
		var cancel_page = 'entities.page';
		var rest_entity_name = emr.message("openhmis.cashier.restful_name");

		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
				self.bindBaseParameters(module_name, rest_entity_name, entity_name_message_key, cancel_page);
			};

		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
			// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function (uuid) {
				//check if timesheet is required
				$scope.cashPoints = [];
				CashierBillRestfulService.getTimesheet(function(data){
					if(data !== undefined){
						$scope.cashier = data.cashier;
						$scope.cashPoint = data.cashPoint;
						if(data.isTimeSheetRequired === true
							&& (data.cashier === undefined || data.cashPoint === undefined)){
							// redirect to timesheet page.
							window.location = '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/timesheet/entities.page#/';
						}

						if(data.cashPoint === undefined){
							CashierBillRestfulService.getCashPoints(module_name, function(data){
								$scope.cashPoints = data.results;
								$scope.cashPoint = $scope.cashPoints[0];
							});
						}
					}
				});

				$scope.totalNumOfResults = 0;
				$scope.limit = CookiesService.get('limit') || 5;
				$scope.currentPage = CookiesService.get('currentPage') || 1;
				$scope.pagingFrom = PaginationService.pagingFrom;
				$scope.pagingTo = PaginationService.pagingTo;
				$scope.patient;
				$scope.patients = [];
				$scope.searchPatients = self.searchPatients;
				$scope.selectPatient = self.selectPatient;
				$scope.changePatient = self.changePatient;
				$scope.loadItemDetails = self.loadItemDetails;
				$scope.computeTotalPrice = self.computeTotalPrice;
				$scope.STATUS = 'PENDING';
				$scope.totalPayableAmount = 0.00;
				$scope.totalAmountDue = 0.00;
				$scope.totalChangeDue = 0.00;
				$scope.totalAmountTendered = 0.00;
				$scope.amountTendered = 0.00;
				$scope.currentPayments = [];
				$scope.paymentMode;
				$scope.previousLineItems = [];
				$scope.previousPayments = [];
				$scope.currentPayments = [];
				$scope.adjustmentReason = '';
				$scope.lineItems = [];
				$scope.isProcessPayment = false;
				$scope.isPostBill = false;
				$scope.isAdjustBill = false;
				$scope.isSaveBill = false;
				$scope.isPrintBill = false;
				$scope.roundingItem = '';

				//load rounding item if any..
				CashierBillRestfulService.getRoundingItem(function (roundingItem) {
					$scope.roundingItem = roundingItem;
				});

				if ($scope.entity !== undefined && $scope.entity.patient !== undefined) {
					$scope.uuid = self.getUuid();
					$scope.selectExistingPatient = true;
					$scope.patient = $scope.entity.patient.display.split(' - ')[1];
					self.searchPatients(1);

					self.addExistingLineItems();
					$scope.STATUS = $scope.entity.status;

					//load bill
					CashierBillRestfulService.loadBill(module_name, $scope.uuid, self.onLoadBillSuccessful);
				} else {
					$scope.selectedPatient = '';
					$scope.selectExistingPatient = false;
				}

				$scope.visit = '';
				$scope.endVisit = self.endVisit;
				$scope.paymentModeAttributes = [];
				if ($scope.STATUS === 'PENDING') {
					self.addLineItem();
				}

				if ($scope.STATUS !== 'ADJUSTED') {
					self.getPaymentModes();
				}

				$scope.loadPaymentModeAttributes = self.loadPaymentModeAttributes;
				$scope.searchStockOperationItems = self.searchStockOperationItems;
				$scope.selectStockOperationItem = self.selectStockOperationItem;
				$scope.changeItemQuantity = self.changeItemQuantity;
				$scope.removeLineItem = self.removeLineItem;
				$scope.formatItemPrice = CashierBillFunctions.formatItemPrice;
				$scope.attributes = [];
				$scope.processPayment = self.processPayment;
				$scope.postBill = self.postBill;
				$scope.postAndPrintBill = self.postAndPrintBill;
				$scope.postPayment = self.postPayment;
				$scope.adjustBill = self.adjustBill;
				$scope.saveBill = self.saveBill;
				$scope.printBill = self.printBill;
			};

		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
			// @Override
		self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
				$scope.submitted = false;
				CashierBillRestfulService.setBaseUrl(module_name);

				// validate patient
				if ($scope.selectedPatient === '') {
					$scope.submitted = true;
					emr.errorAlert("openhmis.inventory.operations.required.patient");
					return false;
				} else {
					$scope.entity.patient = $scope.selectedPatient.uuid;
				}

				// validate line items
				var validatedLineItems = [];
				if (!$scope.isAdjustBill) {
					if (CashierBillFunctions.validateLineItems($scope.lineItems, validatedLineItems)) {
						$scope.entity.lineItems = validatedLineItems;

						// check for previous line items
						if (!$scope.isSaveBill) {
							CashierBillFunctions.validateLineItems($scope.previousLineItems, $scope.entity.lineItems);
						}
					} else {
						emr.errorAlert("openhmis.cashier.bill.chooseItemErrorMessage");
						$scope.submitted = true;
						return false;
					}
				}

				// validate payments
				$scope.entity.payments = [];
				if ($scope.isProcessPayment) {
					var payment = CashierBillFunctions.createPayment(
						$scope.paymentModeAttributes, $scope.attributes,
						$scope.amountTendered, $scope.paymentMode.uuid);
					if (!payment) {
						$scope.submitted = true;
						return false;
					} else {
						$scope.entity.payments.push(payment);
						$scope.STATUS = 'POSTED';
					}
				}

				CashierBillFunctions.populatePayments($scope.currentPayments, $scope.entity.payments);
				CashierBillFunctions.populatePayments($scope.previousPayments, $scope.entity.payments);

				if ($scope.isAdjustBill) {
					$scope.entity.adjustmentReason = $scope.adjustmentReason;
					$scope.entity.lineItems = [];
					delete $scope.entity['status'];
				} else if ($scope.isPostBill) {
					$scope.entity.status = 'POSTED';
				} else {
					$scope.entity.status = $scope.STATUS;
				}

				if ($scope.uuid !== undefined) {
					$scope.entity.billAdjusted = $scope.uuid;
				}

				if($scope.cashPoint !== undefined){
					$scope.entity.cashPoint = $scope.cashPoint.uuid;
				}

				return true;
			}

		self.getPaymentModes = self.getPaymentModes || function () {
				CashierBillRestfulService.getPaymentModes(module_name, self.onLoadPaymentModesSuccessful);
			}

		self.loadPaymentModeAttributes = self.loadPaymentModeAttributes || function (uuid) {
				CashierBillRestfulService.loadPaymentModeAttributes(module_name, uuid, self.onLoadPaymentModeAttributesSuccessful);
			}

		self.searchPatients = self.searchPatients || function (currentPage) {
				if ($scope.patient !== undefined) {
					$scope.currentPage = $scope.currentPage || currentPage;
					$scope.patients = CommonsRestfulFunctions.searchPatients(
						module_name, $scope.patient, $scope.currentPage,
						$scope.limit, $scope);
				}
			}

		self.selectPatient = self.selectPatient || function (patient) {
				$scope.selectedPatient = patient;
				CommonsRestfulFunctions.loadVisit(module_name, patient.uuid, $scope);
			}

		self.changePatient = self.changePatient || function () {
				$scope.selectedPatient = '';
			}

		self.endVisit = self.endVisit || function () {
				CommonsRestfulFunctions.endVisit(module_name,
					$scope.visit.uuid, $scope);
			}

		self.addExistingLineItems = self.addExistingLineItems || function () {
				CashierBillFunctions.populateExistingLineItems($scope.entity.lineItems, $scope.lineItems, $scope);
			}

		self.addLineItem = self.addLineItem || function () {
				var addItem = true;
				for (var i = 0; i < $scope.lineItems.length; i++) {
					var lineItem = $scope.lineItems[i];
					if (!lineItem.selected) {
						addItem = false;
						break;
					}
				}
				if (addItem) {
					var lineItem = new LineItemModel('', 1, '');
					$scope.lineItems.push(lineItem);
				}
			}

		self.removeLineItem = self.removeLineItem || function (lineItem) {
				//only remove selected line items..
				if(lineItem.selected){
					var index = $scope.lineItems.indexOf(lineItem);
					if (index !== -1) {
						$scope.lineItems.splice(index, 1);
					}

					if ($scope.lineItems.length == 0) {
						self.addLineItem();
					}

					self.computeTotalPrice();
				}
			}

		self.searchStockOperationItems = self.searchStockOperationItems || function (search) {
				return CashierBillRestfulService.searchStockOperationItems(search);
			}

		self.selectStockOperationItem = self.selectStockOperationItem || function (stockOperationItem, lineItem) {
				$scope.lineItem = {};
				lineItem.setItemStockQuantity(1);
				if (stockOperationItem !== undefined) {
					lineItem.setItemStock(stockOperationItem);
					lineItem.setItemStockPrice(stockOperationItem.defaultPrice);
					lineItem.setSelected(true);
					lineItem.setTotal($filter('number')(stockOperationItem.defaultPrice.price * lineItem.getItemStockQuantity(), 2))
					$scope.lineItem = lineItem;
					// load item details
					self.loadItemDetails(stockOperationItem.uuid, $scope.lineItem);
					// load next line item
					self.addLineItem();
					self.computeTotalPrice();
				}
			}

		self.loadItemDetails = self.loadItemDetails || function (uuid, lineItem) {
				return CashierBillRestfulService.loadItemDetails(uuid, lineItem);
			}

		self.changeItemQuantity = self.changeItemQuantity || function (lineItem) {
				if (lineItem.itemStockQuantity <= 0) {
					lineItem.setItemStockQuantity(1);
				}
				lineItem.setTotal(lineItem.getItemStockQuantity() * lineItem.getItemStockPrice().price);
				self.computeTotalPrice();
			}

		self.computeTotalPrice = self.computeTotalPrice || function () {
				$scope.totalChangeDue = 0;
				$scope.totalAmountDue = 0;

				// calculate amount for current items.
				if ($scope.STATUS === 'PENDING') {
					$scope.totalPayableAmount =
						CashierBillFunctions.calculateTotalPayableAmount(
							$scope.lineItems, $scope.roundingItem);
				} else {
					$scope.totalPayableAmount =
						CashierBillFunctions.calculateTotalPayableAmount($scope.lineItems);
					if ($scope.STATUS === 'ADJUSTED') {
						$scope.totalPayableAmount = CashierBillFunctions.roundItemPrice(
							$scope.totalPayableAmount, $scope.roundingItem.roundToNearest, $scope.roundingItem.roundingMode);
					}
				}

				// sum amount for previous line items.
				$scope.totalPayableAmount += CashierBillFunctions.calculateTotalPayableAmount($scope.previousLineItems);

				// calculate tendered amount
				$scope.totalAmountTendered = 0;
				if ($scope.currentPayments !== null) {
					for (var i = 0; i < $scope.currentPayments.length; i++) {
						$scope.totalAmountTendered += $scope.currentPayments[i].amountTendered;
					}
				}

				// sum previous payments.
				if ($scope.previousPayments !== null) {
					for (var i = 0; i < $scope.previousPayments.length; i++) {
						$scope.totalAmountTendered += $scope.previousPayments[i].amountTendered;
					}
				}

				// calculate change due
				if ($scope.totalPayableAmount > 0) {
					$scope.totalChangeDue = $scope.totalAmountTendered - $scope.totalPayableAmount;
				} else if($scope.totalAmountTendered > 0){
					$scope.totalChangeDue = $scope.totalAmountTendered;
				}

				// calculate amount due
				if ($scope.totalChangeDue < 0) {
					$scope.totalAmountDue = $scope.totalChangeDue * -1;
					$scope.totalChangeDue = 0;
				} else {
					$scope.totalAmountDue = 0;
				}

				$scope.totalChangeDue = $filter('number')($scope.totalChangeDue, 2);
				$scope.totalAmountDue = $filter('number')($scope.totalAmountDue, 2);
				$scope.totalAmountTendered = $filter('number')($scope.totalAmountTendered, 2);
				$scope.totalPayableAmount = $filter('number')($scope.totalPayableAmount, 2);
			}

		self.setPaymentWarningMessage = self.setPaymentWarningMessage || function () {
				$scope.paymentWarningMessage = $filter('EmrFormat')(
					emr.message("openhmis.cashier.payment.confirm.paymentProcess"), [
						$scope.paymentMode.name, $scope.amountTendered
					]
				);
			}

		self.processPayment = self.processPayment || function () {
				if (EntityFunctions.validateAttributeTypes(
						$scope.paymentModeAttributes, $scope.attributes, [])) {
					$scope.isProcessPayment = true;
					$scope.isPrintBill = false;
					self.setPaymentWarningMessage();
					CashierBillFunctions.paymentWarningDialog($scope);
				} else{
					$scope.submitted = true;
				}
			}

		self.postPayment = self.postPayment || function () {
				if ($scope.uuid !== undefined && $scope.isProcessPayment) {
					var payment = CashierBillFunctions.createPayment(
						$scope.paymentModeAttributes, $scope.attributes,
						$scope.amountTendered, $scope.paymentMode.uuid);
					if (!payment) {
						$scope.submitted = true;
						return false;
					} else {
						$scope.entity = payment;
						CashierBillRestfulService.processPayment(
							module_name, $scope.uuid, payment, function () {
								$window.location.reload();
							}
						);
					}
				}
			}

		self.postBill = self.postBill || function () {
				$scope.isPostBill = true;
				$scope.isPrintBill = false;
				$scope.saveOrUpdate();
			}

		self.postAndPrintBill = self.postAndPrintBill || function () {
				$scope.isPrintBill = true;
				$scope.isPostBill = true;
				$scope.saveOrUpdate();
			}

		self.adjustBill = self.adjustBill || function () {
				$scope.isAdjustBill = true;
				$scope.isPrintBill = false;

				//check if adjustment reason is required.
				CashierBillRestfulService.checkAdjustmentReasonRequired(function(data){
					if(data !== undefined && data.results === "true"){
						$scope.adjustmentReasonRequired = true;
					}
					else {
						$scope.adjustmentReasonRequired = false;
					}
					CashierBillFunctions.adjustBillWarningDialog($scope);
				});
			}

		self.saveBill = self.saveBill || function () {
				$scope.isSaveBill = true;
				$scope.isPrintBill = false;
				$scope.saveOrUpdate();
			}

		self.printBill = self.printBill || function () {
				CashierBillRestfulService.loadBill(module_name, $scope.uuid, function (data) {
					if (data.id !== undefined) {
						EntityFunctions.printPage(
							'/' + OPENMRS_CONTEXT_PATH + '/module/openhmis/cashier/receipt.form?billId=' + data.id
						);
					}
				});

				$scope.isPrintBill = false;
			}

		//callback
		self.onLoadPaymentModesSuccessful = self.onLoadPaymentModesSuccessful || function (data) {
				$scope.paymentModes = data.results;
				if ($scope.paymentModes !== undefined && $scope.paymentModes.length > 0) {
					$scope.paymentMode = $scope.paymentMode || $scope.paymentModes[0];
					self.loadPaymentModeAttributes($scope.paymentMode.uuid);
				}
			}

		self.onLoadPaymentModeAttributesSuccessful = self.onLoadPaymentModeAttributesSuccessful || function (data) {
				var paymentModeAttributes = data;
				if (paymentModeAttributes !== undefined && paymentModeAttributes.attributeTypes !== undefined) {
					$scope.paymentModeAttributes = paymentModeAttributes.attributeTypes;
				}
			}

		self.onLoadBillSuccessful = self.onLoadBillSuccessful || function (data) {
				$scope.currentPayments = [];
				$scope.previousPayments = [];

				CashierBillRestfulService.getCashier(data.cashier.links[0].uri,
					data.cashier.uuid, function(data){
						$scope.cashier = data.person.display;
					}
				);

				$scope.cashPoint = data.cashPoint;
				var pageTitle = "<h3>";

				if ($scope.STATUS === 'PENDING') {
					pageTitle += emr.message('openhmis.cashier.editBill');
				} else {
					pageTitle += emr.message('openhmis.cashier.viewBill');
				}

				pageTitle += " " + data.display + "</h3>";

				if (data.billAdjusted !== null && data.billAdjusted.display !== null) {
					if ($scope.STATUS !== 'ADJUSTED') {
						// load adjusted bill
						$scope.previousBillTitle =
							emr.message("openhmis.cashier.bill.previousBill") + " (" + data.billAdjusted.display + ")";
						CashierBillRestfulService.loadBill(module_name, data.billAdjusted.uuid, self.onLoadAdjustedBillSuccessful);
					}

					pageTitle += " Adjustment of <a target='_blank' href='entities.page#/" + data.billAdjusted.uuid + "'>";
					pageTitle += " " + data.billAdjusted.display + "</a>";

					if (data.adjustedBy !== null && data.adjustedBy.length > 0) {
						pageTitle += " Adjusted by ";
						for (var i = 0; i < data.adjustedBy.length; i++) {
							var adjustedBy = data.adjustedBy[i];
							pageTitle += "<a target='_blank' href='entities.page#/" + adjustedBy.uuid;
							pageTitle += "'>" + adjustedBy.display + "</a>";
						}
					}
				}

				$scope.pageTitle = $sce.trustAsHtml(pageTitle);
				$scope.currentPayments = data.payments;
				self.computeTotalPrice();
			}

		self.onLoadAdjustedBillSuccessful = self.onLoadAdjustedBillSuccessful || function (data) {
				if ($scope.entity.status === 'PENDING' || $scope.lineItems.length === 0) {
					CashierBillFunctions.populateExistingLineItems(data.lineItems, $scope.previousLineItems, $scope);
				}

				if ($scope.currentPayments.length === 0) {
					$scope.previousPayments = data.payments;
					self.computeTotalPrice();
				}
			}

		self.onChangeEntitySuccessful = self.onChangeEntitySuccessful || function (data) {
				window.location = 'entities.page#/' + data.uuid;
				if ($scope.isPrintBill) {
					$scope.uuid = data.uuid;
					self.printBill();
				}
			}

		/* ENTRY POINT: Instantiate the base controller which loads the page */
		$injector.invoke(base.GenericEntityController, self, {
			$scope: $scope,
			$filter: $filter,
			$stateParams: $stateParams,
			EntityRestFactory: EntityRestFactory,
			GenericMetadataModel: CashierBillModel
		});
	}
})();
