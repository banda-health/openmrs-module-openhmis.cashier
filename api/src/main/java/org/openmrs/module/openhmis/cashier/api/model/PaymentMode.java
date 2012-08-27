package org.openmrs.module.openhmis.cashier.api.model;

import java.util.Set;

import org.openmrs.BaseOpenmrsMetadata;

/**
 * Class to represent a mode of payment (e.g., cash, check, credit card)
 * @author daniel
 *
 */
public class PaymentMode extends BaseOpenmrsMetadata {
	private Integer paymentModeId;
	private String name;
	private String description;
	private Set<PaymentModeAttribute> attributes;
	
	// Getters & setters
	@Override
	public Integer getId() {
		return paymentModeId;
	}
	@Override
	public void setId(Integer id) {
		paymentModeId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<PaymentModeAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<PaymentModeAttribute> attributes) {
		this.attributes = attributes;
	}
}
