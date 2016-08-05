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
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemPrice;

/**
 * Model class that represents a list of {@link BillLineItem}s and {@link Payment}s created by a cashier for a patient.
 */
public class Bill extends BaseOpenmrsData {
	public static final long serialVersionUID = 0L;

	private Integer billId;
	private String receiptNumber;
	private Provider cashier;
	private Patient patient;
	private CashPoint cashPoint;
	private Bill billAdjusted;
	private BillStatus status;
	private List<BillLineItem> lineItems;
	private Set<Payment> payments;
	private Set<Bill> adjustedBy;
	private Boolean receiptPrinted = false;
	private String adjustmentReason;

	public String getAdjustmentReason() {
		return adjustmentReason;
	}

	public void setAdjustmentReason(String adjustmentReason) {
		this.adjustmentReason = adjustmentReason;
	}

	public Boolean isReceiptPrinted() {
		return receiptPrinted;
	}

	public void setReceiptPrinted(Boolean receiptPrinted) {
		this.receiptPrinted = receiptPrinted;
	}

	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;

		if (lineItems != null) {
			for (BillLineItem line : lineItems) {
				if (line != null && !line.getVoided()) {
					total = total.add(line.getTotal());
				}
			}
		}

		return total;
	}

	public BigDecimal getTotalPayments() {
		BigDecimal total = BigDecimal.ZERO;

		if (payments != null) {
			for (Payment payment : payments) {
				if (payment != null && !payment.getVoided()) {
					total = total.add(payment.getAmount());
				}
			}
		}

		return total;
	}

	public BigDecimal getAmountPaid() {
		BigDecimal total = getTotal();
		BigDecimal totalPayments = getTotalPayments();

		return total.min(totalPayments);
	}

	@Override
	public Integer getId() {
		return billId;
	}

	@Override
	public void setId(Integer id) {
		billId = id;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String number) {
		this.receiptNumber = number;
	}

	public Provider getCashier() {
		return cashier;
	}

	public void setCashier(Provider cashier) {
		this.cashier = cashier;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public CashPoint getCashPoint() {
		return cashPoint;
	}

	public void setCashPoint(CashPoint cashPoint) {
		this.cashPoint = cashPoint;
	}

	public Bill getBillAdjusted() {
		return billAdjusted;
	}

	public void setBillAdjusted(Bill billAdjusted) {
		this.billAdjusted = billAdjusted;

		if (billAdjusted != null) {
			billAdjusted.setStatus(BillStatus.ADJUSTED);
		}
	}

	public BillStatus getStatus() {
		return status;
	}

	public void setStatus(BillStatus status) {
		this.status = status;
	}

	public List<BillLineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<BillLineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public BillLineItem addLineItem(Item item, ItemPrice price, int quantity) {
		if (item == null) {
			throw new NullPointerException("The item to add must be defined.");
		}
		if (price == null) {
			throw new NullPointerException("The item price must be defined.");
		}

		return addLineItem(item, price.getPrice(), price.getName(), quantity);
	}

	public BillLineItem addLineItem(Item item, BigDecimal price, String priceName, int quantity) {
		if (item == null) {
			throw new IllegalArgumentException("The item to add must be defined.");
		}
		if (price == null) {
			throw new IllegalArgumentException("The item price must be defined.");
		}

		BillLineItem lineItem = new BillLineItem();
		lineItem.setBill(this);
		lineItem.setItem(item);
		lineItem.setPrice(price);
		lineItem.setPriceName(priceName);
		lineItem.setQuantity(quantity);

		addLineItem(lineItem);

		return lineItem;
	}

	public void addLineItem(BillLineItem item) {
		if (item == null) {
			throw new NullPointerException("The list item to add must be defined.");
		}

		if (this.lineItems == null) {
			this.lineItems = new ArrayList<BillLineItem>();
		}

		this.lineItems.add(item);
		item.setBill(this);
	}

	public void removeLineItem(BillLineItem item) {
		if (item != null) {
			if (this.lineItems != null) {
				this.lineItems.remove(item);
			}
		}
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Payment addPayment(PaymentMode mode, Set<PaymentAttribute> attributes, BigDecimal amount,
	        BigDecimal amountTendered) {
		if (mode == null) {
			throw new NullPointerException("The payment mode must be defined.");
		}
		if (amount == null) {
			throw new NullPointerException(("The payment amount must be defined."));
		}

		Payment payment = new Payment();
		payment.setInstanceType(mode);
		payment.setAmount(amount);
		payment.setAmountTendered(amountTendered);

		if (attributes != null && attributes.size() > 0) {
			payment.setAttributes(attributes);

			for (PaymentAttribute attribute : attributes) {
				attribute.setOwner(payment);
			}
		}

		addPayment(payment);

		return payment;
	}

	public void addPayment(Payment payment) {
		if (payment == null) {
			throw new NullPointerException("The payment to add must be defined.");
		}

		if (this.payments == null) {
			this.payments = new HashSet<Payment>();
		}

		this.payments.add(payment);
		payment.setBill(this);

		this.checkPaidAndUpdateStatus();
	}

	public boolean checkPaidAndUpdateStatus() {
		if (this.getPayments().size() > 0) {
			if (this.status == BillStatus.PENDING || this.status == BillStatus.POSTED) {
				if (getTotalPayments().compareTo(getTotal()) >= 0) {
					this.setStatus(BillStatus.PAID);
					return true;
				} else if (this.status == BillStatus.PENDING) {
					this.status = BillStatus.POSTED;
				}
			}
		}
		return false;
	}

	public void removePayment(Payment payment) {
		if (payment != null && this.payments != null) {
			this.payments.remove(payment);
		}
	}

	public Set<Bill> getAdjustedBy() {
		return adjustedBy;
	}

	public void setAdjustedBy(Set<Bill> adjustedBy) {
		this.adjustedBy = adjustedBy;
	}

	public void addAdjustedBy(Bill adjustedBill) {
		checkAuthorizedToAdjust();
		if (adjustedBill == null) {
			throw new NullPointerException("The adjusted bill to add must be defined.");
		}

		if (this.adjustedBy == null) {
			this.adjustedBy = new HashSet<Bill>();
		}

		adjustedBill.setBillAdjusted(this);
		this.adjustedBy.add(adjustedBill);
	}

	public void removeAdjustedBy(Bill adjustedBill) {
		if (adjustedBill != null && this.adjustedBy != null) {
			this.adjustedBy.remove(adjustedBill);
		}
	}

	private void checkAuthorizedToAdjust() {
		if (!Context.hasPrivilege(PrivilegeConstants.ADJUST_BILLS)) {
			throw new AccessControlException("Access denied to adjust bill.");
		}
	}

	public void recalculateLineItemOrder() {
		int orderCounter = 0;
		for (BillLineItem lineItem : this.getLineItems()) {
			lineItem.setLineItemOrder(orderCounter++);
		}
	}

	public String getLastUpdated() {
		SimpleDateFormat ft = Context.getDateTimeFormat();
		String changedStr = (this.getDateChanged() != null) ? ft.format(this.getDateChanged()) : null;
		String createdStr = (this.getDateCreated() != null) ? ft.format(this.getDateCreated()) : "";
		String dateString = (changedStr != null) ? changedStr : createdStr;

		return dateString;
	}
}
