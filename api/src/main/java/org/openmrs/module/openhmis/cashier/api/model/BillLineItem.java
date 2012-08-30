/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseOpenmrsData;

import java.math.BigDecimal;

/**
 * A LineItem represents a line on a {@link Bill} which will bill some quantity
 * of a particular {@link Item}.
 * 
 * @author daniel
 *
 */
public class BillLineItem extends BaseOpenmrsData {
	private int billLineItemId;
	private Item item;
	private Integer quantity;

	@Override
	public Integer getId() {
		return billLineItemId;
	}

	@Override
	public void setId(Integer id) {
		billLineItemId = id;
	}

	/**
	 * Get the total price for the line item
	 * @return double the total price for the line item
	 */
	public BigDecimal getTotal() {
		//TODO: Implement Item.getDefaultPrice()?
		return new BigDecimal(0);
	}

	// Getters & setters
	public Item getBillableItem() {
		return item;
	}

	public void setBillableItem(Item billableItem) {
		this.item = billableItem;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public int getBillLineItemId() {
		return billLineItemId;
	}

	public void setBillLineItemId(int billLineItemId) {
		this.billLineItemId = billLineItemId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
