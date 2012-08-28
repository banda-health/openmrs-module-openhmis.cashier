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

package org.openmrs.module.openhmis.cashier.api.db.hibernate;

import org.hibernate.Criteria;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.api.APIException;

import java.io.Serializable;
import java.util.List;

public interface IGenericHibernateDAO<E extends BaseOpenmrsObject> {
    Criteria createCriteria();

    E save(E entity) throws APIException;
    void delete(E entity) throws APIException;

    E selectSingle(Serializable id) throws APIException;
    E selectSingle(Criteria criteria) throws APIException;

    List<E> select() throws APIException;
    List<E> select(Criteria criteria) throws APIException;
}
