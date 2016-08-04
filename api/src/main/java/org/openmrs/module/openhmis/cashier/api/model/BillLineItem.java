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
 */
package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemPrice;

/**
 * A LineItem represents a line on a {@link Bill} which will bill some quantity of a particular {@link Item}.
 */
public class BillLineItem extends BaseOpenmrsData {
	public static final long serialVersionUID = 0L;

	private int billLineItemId;
	private Bill bill;
	private Item item;
	private BigDecimal price;
	private String priceName;
	private ItemPrice itemPrice;
	private Integer quantity;
	private Integer lineItemOrder;

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
		return price.multiply(BigDecimal.valueOf(quantity));
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPriceName() {
		return priceName;
	}

	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}

	public ItemPrice getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(ItemPrice itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getLineItemOrder() {
		return lineItemOrder;
	}

	public void setLineItemOrder(Integer lineItemOrder) {
		this.lineItemOrder = lineItemOrder;
	}
}
