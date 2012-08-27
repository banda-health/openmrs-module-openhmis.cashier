package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseOpenmrsMetadata;

/**
 * Metadata class to describe an attribute of a payment mode.  For example, a
 * credit card mode of payment may require a transaction number as an
 * attribute.
 * @author daniel
 *
 */
public class PaymentModeAttribute extends BaseOpenmrsMetadata {
	
	private Integer paymentModeAttributeId;
	private PaymentMode paymentMode;
	private String name;
	private String description;
	private String format;
	private String regExp;
	private Boolean required;
	private Integer order;
	

	// Getters & setters
	public Integer getId() {
		return paymentModeAttributeId;
	}
	public void setId(Integer id) {
		paymentModeAttributeId = id;
	}
	public PaymentMode getPaymentType() {
		return paymentMode;
	}
	public void setPaymentType(PaymentMode paymentType) {
		this.paymentMode = paymentType;
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
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
}
