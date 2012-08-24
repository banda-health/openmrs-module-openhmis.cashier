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
package org.openmrs.module.openhmis.cashier.api.model;

import org.openmrs.BaseCustomizableData;
import org.openmrs.customdatatype.Customizable;

import java.math.BigDecimal;
import java.util.Set;

public class Item extends BaseCustomizableData<ItemAttribute> implements Customizable<ItemAttribute> {
	private Integer itemId;
	private Set<String> codes;
	private Set<BigDecimal> prices;
	private String name;
	private String description;
	private Department department;

	public Item() {
	}

	public Item(Integer itemId) {
		this.itemId = itemId;
	}

	@Override
	public Integer getId() {
		return this.itemId;
	}

	@Override
	public void setId(Integer id) {
		this.itemId = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Set<String> getCodes() {
		return codes;
	}

	public void setCodes(Set<String> codes) {
		this.codes = codes;
	}

	public Set<BigDecimal> getPrices() {
		return prices;
	}

	public void setPrices(Set<BigDecimal> prices) {
		this.prices = prices;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}
