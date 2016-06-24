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

	var baseModel = angular.module('app.lineItemModel', []);

	function LineItemModel() {

		function LineItemModel(itemStock, itemStockQuantity, itemStockPrice) {
			this.itemStock = itemStock;
			this.itemStockQuantity = itemStockQuantity;
			this.selected = false;
			this.itemStockPrice = itemStockPrice;
			this.total = '';
			this.prices = [];
		}

		LineItemModel.prototype = {

			getItemStock: function () {
				return this.itemStock;
			},

			setItemStock: function (itemStock) {
				this.itemStock = itemStock;
			},

			getItemStockQuantity: function () {
				return this.itemStockQuantity;
			},

			setItemStockQuantity: function (itemStockQuantity) {
				this.itemStockQuantity = itemStockQuantity;
			},

			getItemStockPrice: function () {
				return this.itemStockPrice;
			},

			setItemStockPrice: function (itemStockPrice) {
				this.itemStockPrice = itemStockPrice;
			},

			setSelected: function (selected) {
				this.selected = selected;
			},

			isSelected: function () {
				return this.selected;
			},

			getTotal: function () {
				return this.total;
			},

			setTotal: function (total) {
				this.total = total;
			},

			getPrices: function () {
				return this.prices;
			},

			setPrices: function (prices) {
				this.prices = prices;
			}
		};

		return LineItemModel;
	}

	baseModel.factory("LineItemModel", LineItemModel);
	LineItemModel.$inject = [];
})();
