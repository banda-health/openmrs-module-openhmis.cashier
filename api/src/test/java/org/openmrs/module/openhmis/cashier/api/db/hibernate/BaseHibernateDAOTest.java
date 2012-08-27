package org.openmrs.module.openhmis.cashier.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.openhmis.cashier.api.model.Department;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseHibernateDAOTest extends BaseModuleContextSensitiveTest {	
	private Log log = LogFactory.getLog(BaseHibernateDAO.class);
	private static String TEST_DATABASE_FILE = "db/DepartmentTest.xml";

	@Autowired
	private SessionFactory sessionFactory;
	
	private BaseHibernateDAO<Department> provider;

	@Before
	public void before() {
		try {
			executeDataSet(TEST_DATABASE_FILE);
		} catch (Exception e) {
			Assert.fail("Couldn't load test database from " + TEST_DATABASE_FILE);
		}
		provider = new BaseHibernateDAO<Department>(Department.class, sessionFactory);
	}
	/**
	 * @see BaseHibernateDAO#getAll()
	 * @verifies get all objects of type T from the database.
	 */
	@Test
	public void getAll_shouldGetAllObjectsOfTypeTFromTheDatabase()
			throws Exception {
		List<Department> list = provider.getAll();
		Department department = list.get(0);
		Assert.assertEquals("Test", department.getName());
	}
}