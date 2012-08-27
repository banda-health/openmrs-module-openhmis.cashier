package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;

/**
 * A LineItem represents a line on a {@link Bill} which will bill some quantity
 * of a particular {@link BillableItem}.
 * 
 * @author daniel
 *
 */
public class BillLineItem {
	private int billLineItemId;
	private Item item;
	private Integer quantity;
	
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
