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
package org.openmrs.module.openhmis.cashier.web.propertyeditor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.api.IEntityService;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.ParameterizedType;

public class EntityPropertyEditor<E extends OpenmrsObject> extends PropertyEditorSupport {
	private IEntityService<E> service;

	public EntityPropertyEditor(Class<? extends IEntityService<E>> service) {
		this.service = Context.getService(service);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isEmpty(text)) {
			setValue(null);
		} else {
			E entity;
			if (NumberUtils.isNumber(text)) {
				entity = service.getById(Integer.valueOf(text));
			} else {
				entity = service.getByUuid(text);
			}

			setValue(entity);
			if (entity == null) {
				throw new IllegalArgumentException("Entity ('" + getEntityClass().getName() + "') not found: " + text);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getAsText() {
		E entity = (E)getValue();

		if (entity == null) {
			return "";
		} else {
			return entity.getId().toString();
		}
	}

	@SuppressWarnings("unchecked")
	protected Class<E> getEntityClass() {
		ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();

		return (Class) parameterizedType.getActualTypeArguments()[0];
	}
}