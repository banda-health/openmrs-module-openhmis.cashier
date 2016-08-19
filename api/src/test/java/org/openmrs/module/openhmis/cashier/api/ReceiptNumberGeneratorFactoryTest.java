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
package org.openmrs.module.openhmis.cashier.api;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.cashier.ModuleSettings;
import org.openmrs.module.openhmis.cashier.api.test.AnotherTestReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.test.InvalidReceiptNumberGenerator;
import org.openmrs.module.openhmis.cashier.api.test.TestReceiptNumberGenerator;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class ReceiptNumberGeneratorFactoryTest {
	protected static final int TEST_GENERATOR_CLASSES = 2;

	protected AdministrationService administrationService;

	@Before
	public void before() {
		administrationService = mock(AdministrationService.class);

		mockStatic(Context.class);
		when(Context.getAdministrationService())
		        .thenReturn(administrationService);
	}

	@After
	public void after() {
		ReceiptNumberGeneratorFactory.reset();
	}

	/**
	 * @verifies Return the currently defined receipt number generator
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test
	public void getGenerator_shouldReturnTheCurrentlyDefinedReceiptNumberGenerator() throws Exception {
		// Configure system generator
		when(administrationService.getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR))
		        .thenReturn(TestReceiptNumberGenerator.class.getName());

		// Get the generator from the factory
		IReceiptNumberGenerator generator = ReceiptNumberGeneratorFactory.getGenerator();

		// Ensure that the correct generator was returned
		Assert.assertNotNull(generator);
		Assert.assertEquals(TestReceiptNumberGenerator.class, generator.getClass());

		// Get the generator again
		generator = ReceiptNumberGeneratorFactory.getGenerator();
		Assert.assertNotNull(generator);
		Assert.assertEquals(TestReceiptNumberGenerator.class, generator.getClass());

		// Ensure that the admin service was only called once
		verify(administrationService, times(1))
		        .getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR);
	}

	/**
	 * @verifies Load the generator if it has not been loaded.
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test
	public void getGenerator_shouldLoadTheGeneratorIfItHasNotBeenLoaded() throws Exception {
		when(administrationService.getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR))
		        .thenReturn(TestReceiptNumberGenerator.class.getName());

		TestReceiptNumberGenerator generator = (TestReceiptNumberGenerator)ReceiptNumberGeneratorFactory.getGenerator();

		Assert.assertNotNull(generator);
		Assert.assertEquals(true, generator.isLoaded());
		Assert.assertEquals(1, generator.getLoadedCount());
	}

	/**
	 * @verifies not load the generator if it has been loaded.
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test
	public void getGenerator_shouldNotLoadTheGeneratorIfItHasBeenLoaded() throws Exception {
		TestReceiptNumberGenerator generator = new TestReceiptNumberGenerator();
		generator.load();

		Assert.assertEquals(true, generator.isLoaded());
		Assert.assertEquals(1, generator.getLoadedCount());

		ReceiptNumberGeneratorFactory.setGenerator(generator);
		generator = (TestReceiptNumberGenerator)ReceiptNumberGeneratorFactory.getGenerator();

		Assert.assertEquals(true, generator.isLoaded());
		Assert.assertEquals(1, generator.getLoadedCount());
	}

	/**
	 * @verifies Return null if no generator has been defined
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test
	public void getGenerator_shouldReturnNullIfNoGeneratorHasBeenDefined() throws Exception {
		when(administrationService.getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR))
		        .thenReturn(null);

		IReceiptNumberGenerator generator = ReceiptNumberGeneratorFactory.getGenerator();
		Assert.assertNull(generator);
	}

	/**
	 * @verifies Throw APIException if generator class cannot be found
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test(expected = APIException.class)
	public void getGenerator_shouldThrowAPIExceptionIfGeneratorClassCannotBeFound() throws Exception {
		when(administrationService.getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR))
		        .thenReturn("org.openmrs.module.openhmis.cashier.NotAValidClass");

		ReceiptNumberGeneratorFactory.getGenerator();
	}

	/**
	 * @verifies Throw APIException if generator class cannot be instantiated
	 * @see ReceiptNumberGeneratorFactory#getGenerator()
	 */
	@Test(expected = APIException.class)
	public void getGenerator_shouldThrowAPIExceptionIfGeneratorClassCannotBeInstantiated() throws Exception {
		when(administrationService.getGlobalProperty(ModuleSettings.SYSTEM_RECEIPT_NUMBER_GENERATOR))
		        .thenReturn(InvalidReceiptNumberGenerator.class.getName());

		ReceiptNumberGeneratorFactory.getGenerator();
	}

	/**
	 * @verifies Locate all classes that implement IReceiptNumberGenerator
	 * @see ReceiptNumberGeneratorFactory#locateGenerators()
	 */
	@Test
	public void locateGenerators_shouldLocateAllClassesThatImplementIReceiptNumberGenerator() throws Exception {
		IReceiptNumberGenerator[] generators = ReceiptNumberGeneratorFactory.locateGenerators();

		Assert.assertNotNull(generators);
		Assert.assertTrue(generators.length >= 2);

		boolean foundTest1 = false, foundTest2 = false;
		for (IReceiptNumberGenerator generator : generators) {
			if (generator.getClass().equals(TestReceiptNumberGenerator.class)) {
				foundTest1 = true;
			} else if (generator.getClass().equals(AnotherTestReceiptNumberGenerator.class)) {
				foundTest2 = true;
			}

			if (foundTest1 && foundTest2) {
				break;
			}
		}

		Assert.assertTrue("Both of the two test receipt number generators were not located.", foundTest1 && foundTest2);
	}

	/**
	 * @verifies Not throw an exception if the class instantiation fails
	 * @see ReceiptNumberGeneratorFactory#locateGenerators()
	 */
	@Test
	public void locateGenerators_shouldNotThrowAnExceptionIfTheClassInstantiationFails() throws Exception {
		IReceiptNumberGenerator[] generators = ReceiptNumberGeneratorFactory.locateGenerators();

		Assert.assertNotNull(generators);
		Assert.assertTrue(generators.length >= 2);

		boolean foundInvalidGenerator = false;
		for (IReceiptNumberGenerator generator : generators) {
			if (generator.getClass().equals(InvalidReceiptNumberGenerator.class)) {
				foundInvalidGenerator = true;
				break;
			}
		}

		Assert.assertFalse("The invalid generator was unexpectedly located.", foundInvalidGenerator);
	}

	/**
	 * @verifies Use the existing instance for the currently defined generator
	 * @see ReceiptNumberGeneratorFactory#locateGenerators()
	 */
	@Test
	public void locateGenerators_shouldUseTheExistingInstanceForTheCurrentlyDefinedGenerator() throws Exception {
		IReceiptNumberGenerator generator = new TestReceiptNumberGenerator();
		ReceiptNumberGeneratorFactory.setGenerator(generator);

		IReceiptNumberGenerator[] generators = ReceiptNumberGeneratorFactory.locateGenerators();

		Assert.assertNotNull(generators);
		Assert.assertTrue(generators.length >= 2);

		boolean foundGeneratorInstance = false;
		for (IReceiptNumberGenerator gen : generators) {
			if (gen.getClass().equals(TestReceiptNumberGenerator.class)) {
				foundGeneratorInstance = (gen == generator);

				break;
			}
		}

		Assert.assertTrue("A new generator instance was created when the existing should have been reused.",
		    foundGeneratorInstance);
	}

	/**
	 * @verifies Set the receipt number generator for the system
	 * @see ReceiptNumberGeneratorFactory#setGenerator(IReceiptNumberGenerator)
	 */
	@Test
	public void setGenerator_shouldSetTheReceiptNumberGeneratorForTheSystem() throws Exception {
		// Set the generator
		IReceiptNumberGenerator generator = new TestReceiptNumberGenerator();
		ReceiptNumberGeneratorFactory.setGenerator(generator);

		generator = ReceiptNumberGeneratorFactory.getGenerator();

		// Ensure that the correct generator was returned
		Assert.assertNotNull(generator);
		Assert.assertEquals(TestReceiptNumberGenerator.class, generator.getClass());

		IReceiptNumberGenerator generator2 = new AnotherTestReceiptNumberGenerator();
		ReceiptNumberGeneratorFactory.setGenerator(generator2);

		generator = ReceiptNumberGeneratorFactory.getGenerator();

		Assert.assertNotNull(generator);
		Assert.assertEquals(AnotherTestReceiptNumberGenerator.class, generator.getClass());

	}

	/**
	 * @verifies Remove the current generator if set to null
	 * @see ReceiptNumberGeneratorFactory#setGenerator(IReceiptNumberGenerator)
	 */
	@Test
	public void setGenerator_shouldRemoveTheCurrentGeneratorIfSetToNull() throws Exception {
		// Set the generator
		IReceiptNumberGenerator generator = new TestReceiptNumberGenerator();
		ReceiptNumberGeneratorFactory.setGenerator(generator);

		generator = ReceiptNumberGeneratorFactory.getGenerator();
		Assert.assertNotNull(generator);

		ReceiptNumberGeneratorFactory.setGenerator(null);

		generator = ReceiptNumberGeneratorFactory.getGenerator();
		Assert.assertNull(generator);
	}

}
