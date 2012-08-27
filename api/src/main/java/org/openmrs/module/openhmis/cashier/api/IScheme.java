package org.openmrs.module.openhmis.cashier.api;

import org.openmrs.module.openhmis.cashier.api.model.Bill;

public interface IScheme {

	/**
	 * A Scheme should use a reference to a bill to determine how much of the
	 * bill it will cover
	 *  
	 * @param bill
	 */
	public void setBill(Bill bill);
	
	/**
	 * Determine how much of the bill will be covered by the scheme
	 * @return Double the portion of the bill covered by the scheme 
	 */
	public Double getCoveredAmount();
}
