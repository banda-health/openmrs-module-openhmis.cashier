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

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator;

/**
 * Model class that represents the settings for the {@link SequentialReceiptNumberGenerator}.
 */
public class SequentialReceiptNumberGeneratorModel extends BaseOpenmrsObject {
	public static final long serialVersionUID = 0L;

	public static final String DEFAULT_SEPARATOR = "-";
	public static final String DEFAULT_CASHIER_PREFIX = "P";
	public static final String DEFAULT_CASH_POINT_PREFIX = "CP";
	public static final int DEFAULT_SEQUENCE_PADDING = 4;

	private Integer id;

	private SequentialReceiptNumberGenerator.GroupingType groupingType;
	private SequentialReceiptNumberGenerator.SequenceType sequenceType;
	private String separator;
	private String cashierPrefix;
	private String cashPointPrefix;
	private int sequencePadding;
	private boolean includeCheckDigit;

	public SequentialReceiptNumberGeneratorModel() {
		groupingType = SequentialReceiptNumberGenerator.GroupingType.NONE;
		sequenceType = SequentialReceiptNumberGenerator.SequenceType.COUNTER;
		separator = DEFAULT_SEPARATOR;
		cashierPrefix = DEFAULT_CASHIER_PREFIX;
		cashPointPrefix = DEFAULT_CASH_POINT_PREFIX;
		sequencePadding = DEFAULT_SEQUENCE_PADDING;
		includeCheckDigit = true;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public SequentialReceiptNumberGenerator.GroupingType getGroupingType() {
		return groupingType;
	}

	public void setGroupingType(SequentialReceiptNumberGenerator.GroupingType groupingType) {
		this.groupingType = groupingType;
	}

	public SequentialReceiptNumberGenerator.SequenceType getSequenceType() {
		return sequenceType;
	}

	public void setSequenceType(SequentialReceiptNumberGenerator.SequenceType sequenceType) {
		this.sequenceType = sequenceType;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;

		if (this.separator == null) {
			this.separator = "";
		}
	}

	public String getCashierPrefix() {
		return cashierPrefix;
	}

	public void setCashierPrefix(String cashierPrefix) {
		this.cashierPrefix = cashierPrefix;
	}

	public String getCashPointPrefix() {
		return cashPointPrefix;
	}

	public void setCashPointPrefix(String cashPointPrefix) {
		this.cashPointPrefix = cashPointPrefix;
	}

	public int getSequencePadding() {
		return sequencePadding;
	}

	public void setSequencePadding(int sequencePadding) {
		this.sequencePadding = sequencePadding;

		if (this.sequencePadding <= 0) {
			this.sequencePadding = 1;
		}
	}

	public boolean getIncludeCheckDigit() {
		return includeCheckDigit;
	}

	public void setIncludeCheckDigit(boolean includeCheckDigit) {
		this.includeCheckDigit = includeCheckDigit;
	}
}
