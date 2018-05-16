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

	var base = angular.module('app.genericEntityController');
	base.controller("CashierBillController", CashierBillController);
	CashierBillController.$inject = ['$window', '$stateParams', '$injector', '$scope', 'CookiesService',
		'$filter', 'EntityRestFactory', 'CashierBillModel', 'CashierBillRestfulService',
		'CommonsRestfulFunctions', 'PaginationService', 'LineItemModel',
		'CashierBillFunctions', 'EntityFunctions', '$timeout'];

	function CashierBillController($window, $stateParams, $injector, $scope, CookiesService, $filter,
	                               EntityRestFactory, CashierBillModel,
	                               CashierBillRestfulService, CommonsRestfulFunctions,
	                               PaginationService, LineItemModel, CashierBillFunctions,
	                               EntityFunctions, $timeout) {
		var self = this;
		var entity_name_message_key = "openhmis.cashier.bill";
		var REST_ENTITY_NAME = "bill";
		var PRINT_RECEIPT_URL = ROOT_URL + 'module/openhmis/cashier/receipt.form?billId=';
		var BILL_URL = ROOT_URL + 'openhmis.cashier/cashierBill/entities.page#/';
		var TIMESHEET_URL = ROOT_URL + 'openhmis.cashier/timesheet/entities.page#/?redirectUrl=' + BILL_URL;
		var ENTITIES_URL = 'entities.page#/';

		// @Override
		self.setRequiredInitParameters = self.setRequiredInitParameters || function() {
				self.bindBaseParameters(CASHIER_MODULE_NAME, REST_ENTITY_NAME,
					entity_name_message_key, CASHIER_LANDING_PAGE_URL);
				self.checkPrivileges(PRIVILEGE_CREATE_BILL);
			};

		/**
		 * Initializes and binds any required variable and/or function specific to entity.page
		 * @type {Function}
		 */
		// @Override
		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
			|| function() {
				// check init settings and privileges.
				self.checkInitSettingsAndPrivileges();

				// initialize variables and functions.
				$scope.billAdjustedUuid = '';
				$scope.totalNumOfResults = 0;
				$scope.limit = CookiesService.get('limit') || 5;
				$scope.currentPage = CookiesService.get('currentPage') || 1;
				$scope.pagingFrom = PaginationService.pagingFrom;
				$scope.pagingTo = PaginationService.pagingTo;
				$scope.patient;
				$scope.patients = [];
				$scope.searchPatients = self.searchPatients;
				$scope.searchPaymentModePatients = self.searchPaymentModePatients;
				$scope.searchPaymentModePerson = self.searchPaymentModePerson;
				$scope.selectPatient = self.selectPatient;
				$scope.changePatient = self.changePatient;
				$scope.fullyLoaded = false;
				$scope.submitted = false;
				$scope.loadItemDetails = self.loadItemDetails;
				$scope.computeTotalPrice = self.computeTotalPrice;
				$scope.STATUS = 'PENDING';
				$scope.totalPayableAmount = 0.00;
				$scope.totalAmountDue = 0.00;
				$scope.totalChangeDue = 0.00;
				$scope.totalAmountTendered = 0.00;
				$scope.amountTendered;
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
				$scope.paymentModeAttributesData = [];
				$scope.dateCreated = '';

				//load rounding item if any..
				CashierBillRestfulService.getRoundingItem(function(roundingItem) {
					$scope.roundingItem = roundingItem;
				});

				if ($scope.entity !== undefined && $scope.entity.patient !== undefined) {
					$scope.uuid = self.getUuid();
					$scope.selectExistingPatient = true;
					$scope.patient = $scope.entity.patient.display.split(' - ')[0];
					self.searchPatients(1);

					self.addExistingLineItems();
					$scope.STATUS = $scope.entity.status;

					//load bill
					CashierBillRestfulService.loadBill(CASHIER_MODULE_NAME, $scope.uuid, self.onLoadBillSuccessful);
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
				$scope.searchItems = self.searchItems;
				$scope.selectItem = self.selectItem;
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

				// Very hacky and may not work well for slower machines.
				// Need to find a better way to capture an event after all components have rendered.
				$timeout(function() {
					$scope.fullyLoaded = true;
				}, 1700);
			};

		/**
		 * All post-submit validations are done here.
		 * @return boolean
		 */
		// @Override
		self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function() {
				$scope.submitted = false;
				$scope.processing = false;
				CashierBillRestfulService.setBaseUrl(CASHIER_MODULE_NAME);

				// validate patient
				if ($scope.selectedPatient === '') {
					$scope.submitted = true;
					emr.errorAlert("openhmis.commons.general.requirePatient");
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
						$scope.submitted = true;
						return false;
					}
				}

				// validate payments
				$scope.entity.payments = [];
				if ($scope.isProcessPayment) {
					var payment = CashierBillFunctions.createPayment(
						$scope.paymentModeAttributes, $scope.attributes,
						$scope.amountTendered, $scope.totalAmountDue, $scope.paymentMode.uuid);
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
					$scope.entity.billAdjusted = $scope.uuid;
					delete $scope.entity['status'];
				} else if ($scope.isPostBill) {
					$scope.entity.status = 'POSTED';
				} else if ($scope.isSaveBill) {
					$scope.entity.status = 'PENDING';
					$scope.entity.payments = [];
				} else {
					$scope.entity.status = $scope.STATUS;
				}

				// check if a bill is being adjusted.
				if (!$scope.isAdjustBill && $scope.uuid !== undefined) {
					if ($scope.billAdjustedUuid !== undefined && $scope.billAdjustedUuid !== '') {
						$scope.entity.billAdjusted = $scope.billAdjustedUuid;
					}
					$scope.entity.uuid = $scope.uuid;
				}

				// set cashpoint
				if ($scope.cashPoint !== undefined && $scope.cashPoint !== "") {
					$scope.entity.cashPoint = $scope.cashPoint.uuid;
				} else if ($scope.cashPoints.length > 0) {
					$scope.entity.cashPoint = $scope.cashPoints[0].uuid;
				}

				$scope.processing = true;
				return true;
			}

		self.checkInitSettingsAndPrivileges = self.checkInitSettingsAndPrivileges || function() {
				// check if user has privileges to adjust a bill.
				if (self.getUuid() !== undefined) {
					self.checkPrivileges(PRIVILEGE_ADJUST_BILL, self.onHasPrivilegesCallback);

					//check if the "allow bill adjustment" setting is set.
					CashierBillRestfulService.checkAllowBillAdjustment(function(data) {
						if (data !== undefined && data.results === "false") {
							$scope.ALLOW_BILL_ADJUSTMENT = false;
						}
					});
				}

				//check if timesheet is required
				$scope.cashPoints = [];
				CashierBillRestfulService.getTimesheet(function(data) {
					if (data !== undefined) {
						$scope.cashier = data.cashier;
						$scope.cashPoint = data.cashPoint;
						if (data.isTimeSheetRequired === true
							&& (data.cashier === undefined || data.cashPoint === undefined)) {
							// redirect to timesheet page.
							$window.location.href = TIMESHEET_URL;
						}

						if ($scope.cashPoint === undefined) {
							CashierBillRestfulService.getCashPoints(CASHIER_MODULE_NAME, function(data) {
								$scope.cashPoints = data.results;
							});
						}

						if ($scope.cashier === undefined) {
							CommonsRestfulFunctions.getSession(CASHIER_MODULE_NAME, function(data) {
								$scope.cashier = data.currentProvider.person.display;
							});
						}
					}
				});

				// check "autofill payment amount" setting.
				CashierBillRestfulService.checkAutofillPaymentAmount(function(data) {
					if (data !== undefined && data.results === "true") {
						$scope.checkAutofillPaymentAmount = true;
					} else {
						$scope.checkAutofillPaymentAmount = false;
					}
				});
			}

		self.getPaymentModes = self.getPaymentModes || function() {
				CashierBillRestfulService.getPaymentModes(CASHIER_MODULE_NAME, self.onLoadPaymentModesSuccessful);
			}

		self.loadPaymentModeAttributes = self.loadPaymentModeAttributes || function(uuid) {
			if (uuid) {
				CashierBillRestfulService.loadPaymentModeAttributes(CASHIER_MODULE_NAME, uuid, self.onLoadPaymentModeAttributesSuccessful);
			}
		};

		self.searchPaymentModePatients = self.searchPaymentModePatients || function(q) {
				return CashierBillRestfulService.searchPerson(q, 'patient');
			}

		self.searchPaymentModePerson = self.searchPaymentModePerson || function(q) {
				return CashierBillRestfulService.searchPerson(q, 'person');
			}

		self.searchPatients = self.searchPatients || function(currentPage) {
				if ($scope.patient !== undefined) {
					$scope.currentPage = $scope.currentPage || currentPage;
					$scope.patients = CommonsRestfulFunctions.searchPatients(
						CASHIER_MODULE_NAME, $scope.patient, $scope.currentPage,
						$scope.limit, $scope);
				}
			}

		self.selectPatient = self.selectPatient || function(patient) {
				$scope.selectedPatient = patient;
				CommonsRestfulFunctions.loadVisit(CASHIER_MODULE_NAME, patient.uuid, $scope);
			}

		self.changePatient = self.changePatient || function() {
				$scope.selectedPatient = '';
			}

		self.endVisit = self.endVisit || function() {
				CommonsRestfulFunctions.endVisit(CASHIER_MODULE_NAME,
					$scope.visit.uuid, $scope);
			}

		self.addExistingLineItems = self.addExistingLineItems || function() {
				CashierBillFunctions.populateExistingLineItems($scope.entity.lineItems, $scope.lineItems, $scope);
			}

		self.computeTotalPrice = self.computeTotalPrice || function() {
				return CashierBillFunctions.computeTotalPrice($scope);
			}

		self.addLineItem = self.addLineItem || function() {
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

		self.removeLineItem = self.removeLineItem || function(lineItem) {
				//only remove selected line items..
				if (lineItem.selected) {
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

		self.searchItems = self.searchItems || function(search) {
				return CashierBillRestfulService.searchItems(search);
			}

		self.selectItem = self.selectItem || function(selectedItem, lineItem, index) {
				$scope.lineItem = {};
				if (selectedItem !== undefined) {
					lineItem.setInvalidEntry(false);
					lineItem.setItem(selectedItem);
					lineItem.setItemPrice(selectedItem.defaultPrice);
					lineItem.setSelected(true);
					lineItem.setTotal(selectedItem.defaultPrice.price * lineItem.getItemQuantity());
					$scope.lineItem = lineItem;
					// load item details
					self.loadItemDetails(selectedItem.uuid, $scope.lineItem);
					// load next line item
					self.addLineItem();
					self.computeTotalPrice();
				}

				EntityFunctions.focusOnElement('quantity-' + index);
			}

		self.getConcepts = self.getConcepts || function(uuid) {
				if (uuid !== undefined) {
					CashierBillRestfulService.getConcepts(uuid, function(data) {
						//return data.results;
						$scope.concepts = $scope.concepts || data;
					});
				}
			}

		self.loadItemDetails = self.loadItemDetails || function(uuid, lineItem) {
				return CashierBillRestfulService.loadItemDetails(uuid, lineItem);
			}

		self.changeItemQuantity = self.changeItemQuantity || function(lineItem) {
				lineItem.setTotal(lineItem.getItemQuantity() * lineItem.getItemPrice().price);
				self.computeTotalPrice();
			}

		self.setPaymentWarningMessage = self.setPaymentWarningMessage || function() {
				$scope.paymentWarningMessage = $filter('EmrFormat')(
					emr.message("openhmis.cashier.payment.confirm.paymentProcess"), [
						$scope.paymentMode.name, $scope.amountTendered
					]
				);
			}

		self.processPayment = self.processPayment || function() {
				if ($scope.amountTendered === 0) {
					emr.errorAlert("openhmis.cashier.payment.error.amountRequired");
					$scope.submitted = true;
				} else if (EntityFunctions.validateAttributeTypes(
						$scope.paymentModeAttributes, $scope.attributes, [])) {
					$scope.isProcessPayment = true;
					$scope.isPrintBill = false;
					self.setPaymentWarningMessage();
					CashierBillFunctions.paymentWarningDialog($scope);
					EntityFunctions.focusOnElement("confirmPayment");
				} else {
					$scope.submitted = true;
				}
			}

		self.postPayment = self.postPayment || function() {
				if ($scope.uuid !== undefined && $scope.isProcessPayment && $scope.STATUS !== 'PENDING') {
					$scope.processing = true;
					var payment = CashierBillFunctions.createPayment(
						$scope.paymentModeAttributes, $scope.attributes,
						$scope.amountTendered, $scope.totalAmountDue, $scope.paymentMode.uuid);
					if (!payment) {
						$scope.submitted = true;
						return false;
					} else {
						$scope.entity = payment;
						CashierBillRestfulService.processPayment(
							CASHIER_MODULE_NAME, $scope.uuid, payment, function() {
								$window.location.reload();
							}
						);
					}
				} else {
					$scope.saveOrUpdate();
				}
			}

		self.postBill = self.postBill || function() {
				$scope.isPostBill = true;
				$scope.isPrintBill = false;
				$scope.saveOrUpdate();
			}

		self.postAndPrintBill = self.postAndPrintBill || function() {
				$scope.isPrintBill = true;
				$scope.isPostBill = true;
				$scope.saveOrUpdate();
			}

		self.adjustBill = self.adjustBill || function() {
				$scope.isAdjustBill = true;
				$scope.isPrintBill = false;
				//check if adjustment reason is required.
				CashierBillRestfulService.checkAdjustmentReasonRequired(function(data) {
					if (data !== undefined && data.results === "true") {
						$scope.adjustmentReasonRequired = true;
					} else {
						$scope.adjustmentReasonRequired = false;
					}
					CashierBillFunctions.adjustBillWarningDialog($scope);
				});
			}

		self.saveBill = self.saveBill || function() {
				$scope.isSaveBill = true;
				$scope.isPrintBill = false;
				$scope.saveOrUpdate();
			}

		self.printBill = self.printBill || function() {
				CashierBillRestfulService.loadBill(CASHIER_MODULE_NAME, $scope.uuid, function(data) {
					if (data.id !== undefined) {
						EntityFunctions.printPage(
							PRINT_RECEIPT_URL + data.id
						);
					}
				});

				$scope.isPrintBill = false;
			}

		//callback
		self.onLoadPaymentModesSuccessful = self.onLoadPaymentModesSuccessful || function(data) {
				$scope.paymentModes = data.results;
				if ($scope.paymentModes !== undefined && $scope.paymentModes.length > 0) {
					$scope.paymentMode = $scope.paymentMode || $scope.paymentModes[0];
					self.loadPaymentModeAttributes($scope.paymentMode.uuid);
				}
			}

		self.onLoadPaymentModeAttributesSuccessful = self.onLoadPaymentModeAttributesSuccessful || function(data) {
				var paymentModeAttributes = data;
				$scope.paymentModeAttributesData = [];
				if (paymentModeAttributes !== undefined && paymentModeAttributes.attributeTypes !== undefined) {
					$scope.paymentModeAttributes = paymentModeAttributes.attributeTypes;
					CommonsRestfulFunctions.populateFieldAttributesData(ROOT_URL,
						$scope.paymentModeAttributesData, $scope.paymentModeAttributes);
				}
			}

		self.onLoadBillSuccessful = self.onLoadBillSuccessful || function(data) {
				$scope.currentPayments = [];
				$scope.previousPayments = [];
				$scope.adjustedBill = data;
				CashierBillRestfulService.getCashier(data.cashier.links[0].uri,
					data.cashier.uuid, function(data) {
						$scope.cashier = data.person.display;
					}
				);

				$scope.cashPoint = data.cashPoint;
				if (data.billAdjusted !== null) {
					$scope.billAdjustedUuid = data.billAdjusted.uuid;
					if ($scope.STATUS !== 'ADJUSTED') {
						// load adjusted bill
						$scope.previousBillTitle =
							emr.message("openhmis.cashier.bill.previousBill");
						if (data.billAdjusted.display !== null) {
							$scope.previousBillTitle += " (" + data.billAdjusted.display + ") "
						}
						CashierBillRestfulService.loadBill(CASHIER_MODULE_NAME, data.billAdjusted.uuid, self.onLoadAdjustedBillSuccessful);
					}
				}

				$scope.dateCreated = data.dateCreated;
				if ($scope.STATUS === 'PENDING' && $scope.uuid !== undefined){
					$scope.currentPayments = CashierBillFunctions.updatePaymentTenderedAmount(data.payments);
				} else {
					$scope.currentPayments = data.payments;
				}

				self.computeTotalPrice();
			}

		self.onLoadAdjustedBillSuccessful = self.onLoadAdjustedBillSuccessful || function(data) {
				if ($scope.entity.status === 'PENDING' || $scope.lineItems.length === 0) {
					CashierBillFunctions.populateExistingLineItems(data.lineItems, $scope.previousLineItems, $scope);
				}

				if ($scope.currentPayments.length === 0) {
					$scope.previousPayments = data.payments;
					self.computeTotalPrice();
				}
			}

		self.onChangeEntitySuccessful = self.onChangeEntitySuccessful || function(data) {
				if ($scope.uuid === data.uuid) {
					$window.location.reload();
				} else {
					$window.location.href = ENTITIES_URL + data.uuid;
				}

				if ($scope.isPrintBill) {
					$scope.uuid = data.uuid;
					self.printBill();
				}
			}

		self.onHasPrivilegesCallback = self.onHasPrivilegesCallback || function(hasPrivileges){
				if(!hasPrivileges){
					$scope.ALLOW_BILL_ADJUSTMENT = false;
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
