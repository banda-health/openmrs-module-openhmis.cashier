package org.openmrs.module.openhmis.cashier.api.model;

import java.util.Set;

/**
 * A class to represent a payment toward a bill
 * @author daniel
 *
 */
public class Payment {

	private Integer paymentId;
	private PaymentMode paymentMode;
	private Set<PaymentAttribute> attributes;
	private Double amount;
	
	
	// Getters & setters
	public Integer getId() {
		return paymentId;
	}	
	public void setId(Integer id) {
		paymentId = id;
	}
	public PaymentMode getMode() {
		return paymentMode;
	}
	public void setMode(PaymentMode mode) {
		this.paymentMode = mode;
	}
	public Set<PaymentAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<PaymentAttribute> attributes) {
		this.attributes = attributes;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
