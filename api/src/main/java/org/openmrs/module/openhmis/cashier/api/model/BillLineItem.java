package org.openmrs.module.openhmis.cashier.api.model;

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
	private Double quantity;
	
	/**
	 * Get the total price for the line item
	 * @return double the total price for the line item
	 */
	public Double getTotal() {
		//TODO: Implement Item.getDefaultPrice()?
		return 0.;
	}

	// Getters & setters
	public Item getBillableItem() {
		return item;
	}

	public void setBillableItem(Item billableItem) {
		this.item = billableItem;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
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
