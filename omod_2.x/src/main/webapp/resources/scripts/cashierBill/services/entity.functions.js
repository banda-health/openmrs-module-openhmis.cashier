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

	var app = angular.module('app.cashierBillFunctionsFactory', []);
	app.service('CashierBillFunctions', CashierBillFunctions);

	CashierBillFunctions.$inject = ['EntityFunctions', 'LineItemModel'];

	function CashierBillFunctions(EntityFunctions, LineItemModel) {
		var service;

		service = {
			addMessageLabels: addMessageLabels,
			formatItemPrice: formatItemPrice,
			paymentWarningDialog: paymentWarningDialog,
			adjustBillWarningDialog: adjustBillWarningDialog,
			validateLineItems: validateLineItems,
			populateExistingLineItems: populateExistingLineItems,
			populatePayments: populatePayments,
			reOrderItemPrices: reOrderItemPrices,
			calculateTotalPayableAmount: calculateTotalPayableAmount,
			roundItemPrice: roundItemPrice,
			createPayment: createPayment,
			updatePaymentTenderedAmount: updatePaymentTenderedAmount,
			computeTotalPrice: computeTotalPrice,
		};

		return service;

		/**
		 * All message labels used in the UI are defined here
		 * @returns {{}}
		 */
		function addMessageLabels() {
			var messages = {};
			return messages;
		}

		function formatItemPrice(itemPrice) {
			var price = itemPrice.price;
			if (price !== undefined) {
				price = price.toFixed(2);
			} else {
				price = '';
			}

			if (itemPrice.name === undefined || itemPrice.name === '' || itemPrice.name === null) {
				return price;
			} else {
				return price + ' (' + itemPrice.name + ')';
			}
		}

		function paymentWarningDialog($scope) {
			var dialog = emr.setupConfirmationDialog({
				selector: '#payment-warning-dialog',
				actions: {
					confirm: function() {
						$scope.isProcessPayment = true;
						$scope.$apply();
						if ($scope.uuid !== undefined) {
							$scope.postPayment();
						} else {
							$scope.saveOrUpdate();
						}
						dialog.close();
					},
					cancel: function() {
						dialog.close();
					}
				}
			});

			dialog.show();

			EntityFunctions.disableBackground();
		}

		function adjustBillWarningDialog($scope) {
			var dialog = emr.setupConfirmationDialog({
				selector: '#adjust-bill-warning-dialog',
				actions: {
					confirm: function() {
						$scope.isAdjustBill = true;
						$scope.$apply();
						$scope.saveOrUpdate();
						dialog.close();
					},
					cancel: function() {
						dialog.close();
					}
				}
			});

			dialog.show();

			EntityFunctions.disableBackground();
		}

		function validateLineItems(lineItems, validatedLineItems) {
			// validate line items
			var count = 0;
			var failed = false;
			for (var i = 0; i < lineItems.length; i++) {
				var lineItem = lineItems[i];
				if (lineItem.selected) {
					if (lineItem.item.name === undefined) {
						var errorMessage =
							emr.message("openhmis.cashier.bill.lineItems.error.invalidItem") + " - " + lineItem.item.toString();
						emr.errorAlert(errorMessage);
						lineItem.invalidEntry = true;
						failed = true;
						continue;
					}
					var requestLineItem = {
						item: lineItem.item.uuid,
						lineItemOrder: count++,
						price: lineItem.itemPrice.price,
						priceName: lineItem.itemPrice.name || "",
						priceUuid: lineItem.itemPrice.uuid,
						quantity: lineItem.itemQuantity
					};
					validatedLineItems.push(requestLineItem);
				} else if (lineItem.item !== "") {
					var errorMessage =
						emr.message("openhmis.cashier.bill.lineItems.error.invalidItem") + " - " + lineItem.item.toString();
					emr.errorAlert(errorMessage);
					lineItem.invalidEntry = true;
					failed = true;
				}
			}

			if (validatedLineItems.length == 0 && !failed) {
				emr.errorAlert("openhmis.cashier.bill.chooseItemErrorMessage");
			} else if (validatedLineItems.length > 0 && !failed) {
				return true;
			}

			return false;
		}

		function populateExistingLineItems(lineItems, populatedLineItems, $scope) {
			for (var i = 0; i < lineItems.length; i++) {
				var lineItem = lineItems[i];
				var itemPrice = {
					name: lineItem.priceName,
					price: lineItem.price,
					uuid: lineItem.priceUuid
				};
				var lineItemModel = new LineItemModel(lineItem.item, lineItem.quantity, itemPrice);
				lineItemModel.setSelected(true);
				lineItemModel.setTotal(lineItem.price * lineItem.quantity);
				populatedLineItems.push(lineItemModel);

				$scope.lineItem = lineItemModel;

				// load item details
				$scope.loadItemDetails(lineItem.item.uuid, lineItemModel);
			}

			$scope.computeTotalPrice();
		}

		function populatePayments(payments, populatedPayments) {
			if (payments !== undefined && payments.length > 0) {
				for (var i = 0; i < payments.length; i++) {
					var payment = {};
					var attributes = [];
					if (payments[i].attributes.length > 0) {
						for (var j = 0; j < payments[i].attributes.length; j++) {
							var attr = {};
							attr.attributeType = payments[i].attributes[j].attributeType.uuid;
							attr.value = payments[i].attributes[j].value;
							attributes.push(attr);
						}
					}
					payment.attributes = attributes;
					payment.amount = payments[i].amount;
					payment.amountTendered = payments[i].amountTendered;
					payment.instanceType = payments[i].instanceType.uuid;
					populatedPayments.push(payment);
				}
			}
		}

		function createPayment(paymentModeAttributes, attributes,
		                       amountTendered, amountDue, paymentModeUuid) {
			var requestPaymentAttributeTypes = [];
			if (EntityFunctions.validateAttributeTypes(
					paymentModeAttributes, attributes, requestPaymentAttributeTypes)) {
				var payment = {};
				payment.attributes = [];
				if (requestPaymentAttributeTypes.length > 0) {
					payment.attributes = requestPaymentAttributeTypes;
				}

				amountTendered = parseFloat(amountTendered);
				amountDue = parseFloat(amountDue);
				if (amountDue - amountTendered < 0) {
					payment.amount = amountDue;
				} else {
					payment.amount = amountTendered;
				}
				payment.amountTendered = amountTendered;
				payment.instanceType = paymentModeUuid;
				return payment;
			}
			return null;
		}

		function updatePaymentTenderedAmount(payments) {
			for (var i = 0; i < payments.length; i++){
				var payment = payments[i];
				payment.amountTendered = payment.amount;
			}

			return payments;
		}

		function reOrderItemPrices(lineItem, itemDetails) {
			var defaultPrice = itemDetails.defaultPrice;
			var index = -1;
			for (var i = 0; i < itemDetails.prices.length; i++) {
				var price = itemDetails.prices[i];
				if (price.uuid === defaultPrice.uuid) {
					index = lineItem.prices.indexOf(price);
				}
			}

			if (index !== -1) {
				lineItem.prices.splice(index, 1);
				lineItem.prices.unshift(defaultPrice);
			}
		}

		function calculateTotalPayableAmount(lineItems, roundingItem) {
			var totalPayableAmount = 0;
			for (var i = 0; i < lineItems.length; i++) {
				var lineItem = lineItems[i];
				if (lineItem.isSelected()) {
					totalPayableAmount += lineItem.getTotal();
				}
			}

			if (totalPayableAmount > 0 && roundingItem !== null && roundingItem !== undefined) {
				totalPayableAmount = roundItemPrice(
					totalPayableAmount, roundingItem.roundToNearest, roundingItem.roundingMode);
			}

			return totalPayableAmount;
		}

		function computeTotalPrice($scope) {
			$scope.totalChangeDue = 0;
			$scope.totalAmountDue = 0;

			// calculate amount for current items.
			if ($scope.STATUS === 'PENDING') {
				$scope.totalPayableAmount = calculateTotalPayableAmount(
					$scope.lineItems, $scope.roundingItem);
			} else {
				$scope.totalPayableAmount = calculateTotalPayableAmount($scope.lineItems);
				if ($scope.STATUS === 'ADJUSTED') {
					$scope.totalPayableAmount = roundItemPrice(
						$scope.totalPayableAmount, $scope.roundingItem.roundToNearest, $scope.roundingItem.roundingMode);
				}
			}

			// sum amount for previous line items.
			$scope.totalPayableAmount += calculateTotalPayableAmount($scope.previousLineItems);

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
			} else if ($scope.totalAmountTendered > 0) {
				$scope.totalChangeDue = $scope.totalAmountTendered;
			}

			// calculate amount due
			if ($scope.totalChangeDue < 0) {
				$scope.totalAmountDue = $scope.totalChangeDue * -1;
				$scope.totalChangeDue = 0;
			} else {
				$scope.totalAmountDue = 0;
			}

			// auto-fill payment amount.
			if ($scope.checkAutofillPaymentAmount && $scope.totalAmountDue !== 0) {
				$scope.amountTendered = $scope.totalAmountDue;
			}

			$scope.totalChangeDue = $scope.totalChangeDue.toFixed(2);
			$scope.totalAmountDue = $scope.totalAmountDue.toFixed(2);
			$scope.totalAmountTendered = $scope.totalAmountTendered.toFixed(2);
			$scope.totalPayableAmount = $scope.totalPayableAmount.toFixed(2);
		}

		function roundItemPrice(val, nearest, mode) {
			if (nearest === 0 || nearest === undefined || mode === undefined) {
				return val;
			}
			var factor = 1 / nearest;
			switch (mode) {
				case 'FLOOR':
					return Math.floor(val * factor) / factor;
				case 'CEILING':
					return Math.ceil(val * factor) / factor;
				default:
					return Math.round(val * factor) / factor;
			}
		}
	}
})();
