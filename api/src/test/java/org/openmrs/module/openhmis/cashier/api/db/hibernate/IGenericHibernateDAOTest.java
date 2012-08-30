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
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.BaseOpenmrsObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class IGenericHibernateDAOTest<E extends BaseOpenmrsObject> {
	private Class entityClass;

	@Autowired
	protected SessionFactory sessionFactory;
	protected IGenericHibernateDAO<E> dao;

	protected abstract IGenericHibernateDAO<E> newDao();
	protected abstract E newEntity();

	@Before
	public void before() {
		dao = newDao();
	}
	
	/**
	 * @verifies insert a new item into the database
	 * @see IGenericHibernateDAO#save(E)
	 */
	@Test
	public abstract void save_shouldInsertANewItemIntoTheDatabase() throws Exception;

	/**
	 * @verifies update an existing item in the database
	 * @see IGenericHibernateDAO#save(E)
	 */
	@Test
	public abstract void save_shouldUpdateAnExistingItemInTheDatabase() throws Exception;

	/**
	 * @verifies return a new item with the generated id
	 * @see IGenericHibernateDAO#save(E)
	 */

	@Test
	public abstract void save_shouldReturnANewItemWithTheGeneratedId() throws Exception;
	/**
	 * @verifies not throw an exception if the item is not in the database
	 * @see IGenericHibernateDAO#delete(E)
	 */

	@Test
	public abstract void delete_shouldNotThrowAnExceptionIfTheItemIsNotInTheDatabase() throws Exception;

	/**
	 * @verifies return the entity with the specified id
	 * @see IGenericHibernateDAO#selectSingle(java.io.Serializable)
	 */

	@Test
	public abstract void selectSingle_shouldReturnTheEntityWithTheSpecifiedId() throws Exception;

	/**
	 * @verifies return the entity that meets the criteria
	 * @see IGenericHibernateDAO#selectSingle(org.hibernate.Criteria)
	 */
	@Test
	public abstract  void selectSingle_shouldReturnTheEntityThatMeetsTheCriteria() throws Exception;

	/**
	 * @verifies return null if no entity can be found
	 * @see IGenericHibernateDAO#selectSingle(org.hibernate.Criteria)
	 */
	@Test
	public abstract void selectSingle_shouldReturnNullIfNoEntityCanBeFound() throws Exception;

	/**
	 * @verifies return the first entity if multiple entities are found
	 * @see IGenericHibernateDAO#selectSingle(org.hibernate.Criteria)
	 */
	@Test
	public abstract void selectSingle_shouldReturnTheFirstEntityIfMultipleEntitiesAreFound() throws Exception;

	/**
	 * @verifies return a list of all the entities
	 * @see IGenericHibernateDAO#select()
	 */
	@Test
	public abstract void select_shouldReturnAListOfAllTheEntities() throws Exception;

	/**
	 * @verifies return a list of all entities that meet the criteria
	 * @see IGenericHibernateDAO#select(org.hibernate.Criteria)
	 */
	@Test
	public abstract void select_shouldReturnAListOfAllEntitiesThatMeetTheCriteria() throws Exception;

	/**
	 * @verifies return an empty list when no entities are found
	 * @see IGenericHibernateDAO#select(org.hibernate.Criteria)
	 */
	@Test
	public abstract void select_shouldReturnAnEmptyListWhenNoEntitiesAreFound() throws Exception;

	/**
	 * @verifies return a new criteria for the model type.
	 * @see IGenericHibernateDAO#createCriteria()
	 */
	@Test
	public void createCriteria_shouldReturnANewCriteriaForTheModelType() throws Exception {
		Criteria criteria = dao.createCriteria();

		Assert.assertNotNull(criteria);
	}

	/**
	 * @verifies throw an IllegalArgumentException if the entity is null
	 * @see IGenericHibernateDAO#save(E)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void save_shouldThrowAnIllegalArgumentExceptionIfTheEntityIsNull() throws Exception	{
		dao.save(null);
	}


	/**
	 * @verifies throw an IllegalArgumentException if the entity is null
	 * @see IGenericHibernateDAO#delete(E)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_shouldThrowAnIllegalArgumentExceptionIfTheEntityIsNull() throws Exception {
		dao.delete(null);
	}

	/**
	 * @verifies delete the item from the database
	 * @see IGenericHibernateDAO#delete(E)
	 */
	@Test
	public void delete_shouldDeleteTheItemFromTheDatabase() throws Exception {
		List<E> entites = dao.select();
		Assert.assertNotNull(entites);

		int count = entites.size();
		Assert.assertTrue(count > 0);

		dao.delete(entites.get(0));

		entites = dao.select();
		Assert.assertNotNull(entites);
		Assert.assertEquals(count - 1, entites.size());
	}

	/**
	 * @verifies throw an IllegalArgumentException if the id is null
	 * @see IGenericHibernateDAO#selectSingle(java.io.Serializable)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selectSingle_shouldThrowAnIllegalArgumentExceptionIfTheIdIsNull() throws Exception {
		Serializable s = null;
		dao.selectSingle(s);
	}

	/**
	 * @verifies return null if an entity with the id can not be found
	 * @see IGenericHibernateDAO#selectSingle(java.io.Serializable)
	 */
	@Test
	public void selectSingle_shouldReturnNullIfAnEntityWithTheIdCanNotBeFound() throws Exception {
		int invalidId = -1;

		E entity = dao.selectSingle(invalidId);

		Assert.assertNull(entity);
	}

	/**
	 * @verifies throw an IllegalArgumentException if the criteria is null
	 * @see IGenericHibernateDAO#selectSingle(org.hibernate.Criteria)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selectSingle_shouldThrowAnIllegalArgumentExceptionIfTheCriteriaIsNull() throws Exception {
		Criteria c = null;
		dao.selectSingle(c);
	}

	/**
	 * @verifies return an empty list if there are no entities in the database
	 * @see IGenericHibernateDAO#select()
	 */
	@Test
	public void select_shouldReturnAnEmptyListIfThereAreNoEntitiesInTheDatabase() throws Exception {
		List<E> entities = dao.select();
		for (E entity : entities) {
			dao.delete(entity);
		}

		entities = dao.select();
		Assert.assertNotNull(entities);
		Assert.assertEquals(0, entities.size());
	}

	/**
	 * @verifies throw an IllegalArgumentException if the criteria is null
	 * @see IGenericHibernateDAO#select(org.hibernate.Criteria)
	 */
	@Test(expected = IllegalArgumentException.class)
	public void select_shouldThrowAnIllegalArgumentExceptionIfTheCriteriaIsNull() throws Exception {
		Criteria c =  null;
		dao.select(c);
	}

	protected Class getEntityClass() {
		if (entityClass == null) {
			ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();

			entityClass = (Class) parameterizedType.getActualTypeArguments()[0];
		}

		return entityClass;
	}
}
