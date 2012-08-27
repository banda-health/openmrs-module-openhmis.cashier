package org.openmrs.module.openhmis.cashier.api.model;

import java.math.BigDecimal;
import java.util.Set;

import org.openmrs.BaseOpenmrsMetadata;

/**
 * A class to represent a payment toward a bill
 * @author daniel
 *
 */
public class Payment extends BaseOpenmrsMetadata{

	private Integer paymentId;
	private PaymentMode paymentMode;
	private Set<PaymentAttribute> attributes;
	private BigDecimal amount;
	
	
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
