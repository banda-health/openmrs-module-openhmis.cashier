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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.openhmis.cashier.api.db.hibernate.IGenericHibernateDAO;

public abstract class IDataServiceTest<S extends IEntityService<E>, E extends BaseOpenmrsData> extends IEntityServiceTest<S, E> {
	/**
	 * @verifies void the entity
	 * @see IDataService#voidEncounter(E, String)
	 */
	@Test
	public void voidEncounter_shouldVoidTheEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException with null reason parameter
	 * @see IDataService#voidEncounter(E, String)
	 */
	@Test
	public void voidEncounter_shouldThrowIllegalArgumentExceptionWithNullReasonParameter() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException with null entity
	 * @see IDataService#voidEncounter(E, String)
	 */
	@Test
	public void voidEncounter_shouldThrowIllegalArgumentExceptionWithNullEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies unvoid the entity
	 * @see IDataService#unvoidEncounter(E)
	 */
	@Test
	public void unvoidEncounter_shouldUnvoidTheEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}

	/**
	 * @verifies throw IllegalArgumentException with null entity
	 * @see IDataService#unvoidEncounter(E)
	 */
	@Test
	public void unvoidEncounter_shouldThrowIllegalArgumentExceptionWithNullEntity() throws Exception {
		//TODO auto-generated
		Assert.fail("Not yet implemented");
	}
}
