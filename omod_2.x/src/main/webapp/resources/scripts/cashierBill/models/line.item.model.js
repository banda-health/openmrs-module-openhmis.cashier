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

	var baseModel = angular.module('app.lineItemModel', []);

	function LineItemModel() {

		function LineItemModel(item, itemQuantity, itemPrice) {
			this.item = item;
			this.itemQuantity = itemQuantity;
			this.selected = false;
			this.itemPrice = itemPrice;
			this.total = '';
			this.prices = [];
			this.invalidEntry = false;
		}

		LineItemModel.prototype = {

			getItem: function() {
				return this.item;
			},

			setItem: function(item) {
				this.item = item;
			},

			getItemQuantity: function() {
				return this.itemQuantity;
			},

			setItemQuantity: function(itemQuantity) {
				this.itemQuantity = itemQuantity;
			},

			getItemPrice: function() {
				return this.itemPrice;
			},

			setItemPrice: function(itemPrice) {
				this.itemPrice = itemPrice;
			},

			setSelected: function(selected) {
				this.selected = selected;
			},

			isSelected: function() {
				return this.selected;
			},

			getTotal: function() {
				return this.total;
			},

			setTotal: function(total) {
				this.total = total;
			},

			getPrices: function() {
				return this.prices;
			},

			setPrices: function(prices) {
				this.prices = prices;
			},

			setInvalidEntry: function(invalidEntry) {
				this.invalidEntry = invalidEntry;
			},

			isInvalidEntry: function() {
				return this.invalidEntry;
			}
		};

		return LineItemModel;
	}

	baseModel.factory("LineItemModel", LineItemModel);
	LineItemModel.$inject = [];
})();
