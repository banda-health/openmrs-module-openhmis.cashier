/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
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
describe("Utility Functions", function() {
	it("should round numbers", function() {
		expect(openhmis.round(2, 5, "MID")).toEqual(0);
		expect(openhmis.round(2.4, 5, "MID")).toEqual(0);
		expect(openhmis.round(2.5, 5, "MID")).toEqual(5);
		expect(openhmis.round(2.12, .25, "MID")).toEqual(2);
		expect(openhmis.round(2.13, .25, "MID")).toEqual(2.25);
		
		expect(openhmis.round(9, 10, "FLOOR")).toEqual(0);
		expect(openhmis.round(5.24, .25, "FLOOR")).toEqual(5);
		
		expect(openhmis.round(21, 20, "CEILING")).toEqual(40);
		expect(openhmis.round(100.6, .5, "CEILING")).toEqual(101);
	});
});