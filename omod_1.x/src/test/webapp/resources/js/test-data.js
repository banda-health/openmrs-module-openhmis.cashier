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
openhmis.testData = {};
openhmis.testData.JSON = {
	itemCollection: '\
		{"results":[{\
			"uuid":"20aa2858-6642-4b7a-b456-b07621c26538",\
			"name":"Ciprofloxacin",\
			"codes":[\
				{\
					"uuid":"f546f45d-b000-4f9f-be93-1ec29aea0a8b",\
					"description":null,\
					"retired":false,\
					"retireReason":null,\
					"code":"P3",\
					"resourceVersion":"1.8"\
				},\
				{\
					"uuid":"4a37da9f-7e9d-4200-b518-8e1336ebaa96",\
					"description":null,\
					"retired":false,\
					"retireReason":null,\
					"code":"P1",\
					"resourceVersion":"1.8"\
				}\
			],\
			"department":{\
				"uuid":"faf2f364-189c-4959-9428-4f917f52b8de",\
				"name":"Pharmacy"\
			}\
		}]}',
	
	item: '{\
		"uuid":"20aa2858-6642-4b7a-b456-b07621c26538",\
		"name":"Ciprofloxacin",\
		"description":null,\
		"retired":false,\
		"retireReason":null,\
		"codes":[\
			{\
				"uuid":"f546f45d-b000-4f9f-be93-1ec29aea0a8b",\
				"code":"P3"\
			},\
			{\
				"uuid":"4a37da9f-7e9d-4200-b518-8e1336ebaa96",\
				"code":"P1"\
			}\
		],\
		"prices":[\
			{\
				"uuid":"1b81faeb-8380-4400-92d8-176f9282f06c",\
				"name":"AIDS",\
				"retired":false,\
				"price":1000.00\
			},\
			{\
				"uuid":"4c99f971-7d44-4bfc-acff-ad5d395c2e39",\
				"name":"Default",\
				"retired":false,\
				"price":4.40\
			}\
		],\
		"department":{\
			"uuid":"faf2f364-189c-4959-9428-4f917f52b8de",\
			"name":"Pharmacy"\
		},\
		"defaultPrice":{\
			"uuid":"1b81faeb-8380-4400-92d8-176f9282f06c",\
			"price":1000.00\
		},\
		"resourceVersion":"1.8"\
	}',
	
	departmentCollection: '{\
		"results":[\
			{\
				"uuid":"faf2f364-189c-4959-9428-4f917f52b8de",\
				"name":"Pharmacy",\
				"retired":false\
			},\
			{\
				"uuid":"f53f0242-b9df-4b56-8734-4e1c31903f96",\
				"name":"Lab",\
				"retired":false\
			}\
		],\
		"length":2\
	}',
	
	bill: '{\
		"uuid":"e92857a9-2086-489c-ade6-b2eca0c4645a",\
		"display":"P1-1303180018-7",\
		"voided":false,\
		"voidReason":null,\
		"adjustedBy":[],\
		"billAdjusted":{\
			"uuid":"1603821c-2597-4ffd-b23d-ed979625bc59",\
			"display":"P1-1303180017-9"\
		},\
		"cashPoint":{\
			"uuid":"fae1359d-e7e0-4a1b-a3a5-5745789f8769",\
			"name":"Main Cash Desk",\
			"retired":false\
		},\
		"cashier":{\
			"uuid":"de1ea9d8-3f14-4f09-aa19-46d2f87cb0f3",\
			"display":"4-2 - Daniel Shorten",\
			"links":[{"uri":"http://localhost:8080/openmrs/ws/rest/v1/provider/de1ea9d8-3f14-4f09-aa19-46d2f87cb0f3","rel":"self"}]\
		},\
		"lineItems":[{\
			"uuid":"96766e28-ab36-428d-ac3a-63fae59f3966",\
			"display":"BillLineItem",\
			"voided":false,\
			"voidReason":null,\
			"item":{\
				"uuid":"368a0146-007c-4e1e-9dfb-fa04aac1d675",\
				"name":"Ciprofloxacin 500mg Tab",\
				"description":null,\
				"retired":false,\
				"retireReason":null,\
				"codes":[{\
					"uuid":"475c541b-5bc4-11e2-a081-b4b52f5b1c99",\
					"retired":false,\
					"code":"ST2012099"\
				}],\
				"prices":[{\
					"uuid":"47653d75-5bc4-11e2-a081-b4b52f5b1c99",\
					"name":"",\
					"retired":false,\
					"price":8.00\
				}],\
				"department":{\
					"uuid":"f135fe8f-8725-4b7f-bbc7-f4e104f69dcc",\
					"name":"Pharmacy",\
					"retired":false\
				},\
				"defaultPrice":{\
					"uuid":"47653d75-5bc4-11e2-a081-b4b52f5b1c99",\
					"name":"",\
					"retired":false,\
					"price":8.00\
				},\
				"resourceVersion":"1.8"\
			},\
			"quantity":30,\
			"price":8.00,\
			"lineItemOrder":0,\
			"resourceVersion":"1.8"\
		}],\
		"patient":{\
			"uuid":"e14250a5-4e90-4bbd-8fdf-e81fa50e1e53",\
			"display":"3438937 - John Smith",\
			"links":[{"uri":"http://localhost:8080/openmrs/ws/rest/v1/patient/92f19f02-c506-4364-8791-b8c3f6e41a87","rel":"self"}]\
		},\
		"payments":[{\
			"uuid":"8f3e963f-08b0-41a2-ac54-d36b04031d9d",\
			"instanceType":{\
				"uuid":"f4a7bf8a-d583-43bc-884b-72bd35617b0d",\
				"name":"M-pesa",\
				"retired":false\
			},\
			"attributes":[{\
				"uuid":"151d44a7-173c-4563-aac2-3bf608ac5e90",\
				"display":"Account Number: 9595",\
				"voided":false,\
				"voidReason":null,\
				"attributeType":{\
					"uuid":"8a0390c3-d6e6-4de0-bc2e-44fc87beb9f5",\
					"name":"Account Number",\
					"retired":false\
				},"value":"9595",\
				"valueName":"9595",\
				"order":0,\
				"resourceVersion":"1.8"\
			}],\
			"amount":240.00,\
			"amountTendered":240.00,\
			"dateCreated":1363679328000,\
			"voided":false,\
			"resourceVersion":"1.8"\
		}],\
		"receiptNumber":"P1-1303180018-7",\
		"status":"PENDING",\
		"resourceVersion":"1.8"\
	}',
	
	payment: '{\
		"amount":100,\
		"amountTendered":100,\
		"instanceType":"9b7c3730-b9e5-4e2b-afc4-82076b0e531c",\
		"attributes":[\
			{\
				"attributeType":"85f493d0-fdda-4d33-bbcc-f30d786f1668",\
				"value":"6101"\
			},\
			{\
				"attributeType":"5090715a-b44f-4324-ad05-51bc6382dfa9",\
				"value":"654"\
			}\
		]\
	}'
}