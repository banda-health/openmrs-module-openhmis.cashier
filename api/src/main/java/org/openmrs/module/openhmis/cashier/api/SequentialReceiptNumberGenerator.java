/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.cashier.api;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel;
import org.openmrs.patient.impl.LuhnIdentifierValidator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SequentialReceiptNumberGenerator implements IReceiptNumberGenerator {
	private static final Log log = LogFactory.getLog(ReceiptNumberGeneratorFactory.class);

	public enum GroupingType {
		NONE(0),
		CASHIER(1),
		CASH_POINT(2),
		CASHIER_AND_CASH_POINT(3);

		private int value;

		private GroupingType(int value) {
			this.value = value;
		}
	}

	public enum SequenceType {
		COUNTER(0),
		DATE_COUNTER(1),
		DATE_TIME_COUNTER(2);

		private int value;

		private SequenceType(int value) {
			this.value = value;
		}
	}

	private ISequentialReceiptNumberGeneratorService service;
	private SequentialReceiptNumberGeneratorModel model;
	private LuhnIdentifierValidator checkDigitGenerator;
	private boolean loaded = false;

	public SequentialReceiptNumberGenerator() {
		service = Context.getService(ISequentialReceiptNumberGeneratorService.class);
		checkDigitGenerator = new LuhnIdentifierValidator();
	}

	@Override
	public String getName() {
		return "Sequential Receipt Number Generator";
	}

	@Override
	public String getDescription() {
		return "This receipt number generator provides support for grouped sequential receipt numbers.";
	}

	@Override
	public String getConfigurationPage() {
		throw new NotImplementedException();
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Loads the generator settings from the database.
	 */
	@Override
	public void load() {
		// Load the first (and what should be the only) model record from the service
		List<SequentialReceiptNumberGeneratorModel> records = service.getAll();

		if (records.size() > 0) {
			log.debug("At least one receipt number generator model found.  Using defined generator settings.");
			model = records.get(0);
		} else if (model == null) {
			log.debug("No sequential receipt number generator model found.  Creating a new model with the default settings.");

			// No settings have been saved so create a new model and just use the defaults
			model = new SequentialReceiptNumberGeneratorModel();
		}

		loaded = true;
	}

	/**
	 * Generates a sequential receipt number for the specified bill.
	 * @param bill The bill to generate a new receipt number for.
	 * @return The receipt number.
	 * @should Create a new receipt number by grouping type
	 * @should Create a new receipt number by sequence type
	 * @should Create a new receipt number using the specified separator
	 * @should Generate and set the correct check digit
	 * @should Update sequence table with the new sequence by group
	 * @should Throw NullPointerException if bill is null.
	 */
	@Override
	public String generateNumber(Bill bill) {
		if (bill == null) {
			throw new NullPointerException("The bill must be defined.");
		}

		log.debug("Generating receipt number for bill " + bill.getUuid() + "...");

		String grouping = createGrouping(bill);
		String sequence = getSequence(grouping);

		String number = buildReceiptNumber(grouping, sequence);
		log.debug("Generated receipt number '" + number + "' for bill " + bill.getUuid() + ".");

		return number;
	}

	public String generateCheckDigit(String number) {
		// Remove the separator from the number
		String numberWithoutSep = number;
		if (!model.getSeparator().equals("")) {
			numberWithoutSep = numberWithoutSep.replace(model.getSeparator(), "");
		}

		// Generate the check digit
		numberWithoutSep = checkDigitGenerator.getValidIdentifier(numberWithoutSep);

		// Get the check digit from the end of the returned identifier
		return numberWithoutSep.substring(numberWithoutSep.length() - 1);
	}

	private String createGrouping(Bill bill) {
		String result = "";

		switch (model.getGroupingType()) {
			case CASHIER:
				result = model.getCashierPrefix() + bill.getCashier().getId();
				break;
			case CASH_POINT:
				result = model.getCashPointPrefix() + bill.getCashPoint().getId();
				break;
			case CASHIER_AND_CASH_POINT:
				result = model.getCashierPrefix() + bill.getCashier().getId() + model.getSeparator() +
						model.getCashPointPrefix() + bill.getCashPoint().getId();
				break;
		}

		return result;
	}

	private String getSequence(String grouping) {
		// Do not include the separator when getting the next sequence
		String groupingWithoutSep = grouping;
		if (!model.getSeparator().equals("")) {
			groupingWithoutSep = groupingWithoutSep.replace(model.getSeparator(), "");
		}

		int sequenceNumber = service.reserveNextSequence(groupingWithoutSep);

		SimpleDateFormat format;
		String sequence = String.format("%0" + model.getSequencePadding() + "d", sequenceNumber);
		switch (model.getSequenceType()) {
			case DATE_COUNTER:
				format = new SimpleDateFormat("yyMMdd");
				sequence = format.format(new Date()) + sequence;

				break;
			case DATE_TIME_COUNTER:
				format = new SimpleDateFormat("yyMMddHHmmss");
				sequence = format.format(new Date()) + sequence;

				break;
		}

		return sequence;
	}

	private String buildReceiptNumber(String grouping, String sequence) {
		String number;
		if (StringUtils.isEmpty(grouping)) {
			number = sequence;
		} else {
			number = grouping + model.getSeparator() + sequence;
		}

		if (model.isIncludeCheckDigit()) {
			number = number + model.getSeparator() + generateCheckDigit(number);
		}

		return number;
	}
}
