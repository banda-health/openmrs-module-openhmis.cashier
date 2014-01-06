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
(function() {
curl(
	{
		baseUrl: openhmis.url.resources
	},
	[
		openhmis.url.backboneBase + 'js/lib/jquery',
	],
	function($, openhmis) {
		var testBaseUrl = '/src/test/webapp/resources/js';
		curl(
			[
				'js!' + testBaseUrl + '/model/payment.js',
				'js!' + testBaseUrl + '/model/bill.js',
				'js!' + testBaseUrl + '/view/bill.js'
			],
			function() {
				$(function() {
					window.reporter = new jasmine.HtmlReporter(); jasmine.getEnv().addReporter(reporter);
					jasmine.getEnv().execute();
				});
			}
		);
	}
);
})();