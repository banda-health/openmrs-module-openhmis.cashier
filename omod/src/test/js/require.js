window.ohmisBaseUrl = '/src/main/webapp/resources/js';
curl({ baseUrl: ohmisBaseUrl },
	['openhmis'],
	function(openhmis) { openhmis.baseUrl = ohmisBaseUrl; }
);