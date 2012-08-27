package org.openmrs.module.openhmis.cashier.api.model;

/**
 * A class to store the value of a payment mode attribute for a particular
 * payment.
 * @author daniel
 *
 */
public class PaymentAttribute {
	private Integer paymentAttributeId;
	private Payment billPayment;
	private PaymentModeAttribute typeAttribute;
	private String value;

	public Integer getId() {
		return paymentAttributeId;
	}
	public void setId(Integer id) {
		paymentAttributeId = id;
	}

	// Getters & setters
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Payment getBillPayment() {
		return billPayment;
	}
	public void setBillPayment(Payment billPayment) {
		this.billPayment = billPayment;
	}
	public PaymentModeAttribute getTypeAttribute() {
		return typeAttribute;
	}
	public void setTypeAttribute(PaymentModeAttribute typeAttribute) {
		this.typeAttribute = typeAttribute;
	}
}
