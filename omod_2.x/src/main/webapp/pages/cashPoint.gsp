<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.cashier.cashPoint.name") ])
    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-sanitize.min.js")
    ui.includeCss("openhmis.cashier", "bootstrap.css")
    ui.includeCss("openhmis.cashier", "cashpoint_2x.css")
    ui.includeJavascript("openhmis.cashier", "cashPointsController.js")
%>

<script type="text/javascript">
	var jq = jQuery;
	var breadcrumbs = [
	    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
	    { label: '${ ui.message("openhmis.cashier.page")}' , link: '${ui.pageLink("openhmis.cashier", "cashierLanding")}'},
	    { label: '${ ui.message("openhmis.cashier.manage.module")}', link: 'cashier/cashierManageModule.page' },
	    { label: '${ ui.message("openhmis.cashier.admin.cashPoints")}', link: 'manageCashPoints.page' },
	    { label: '${ ui.message("openhmis.cashier.cashPoint.name")}' }
	];
	
	emr.loadMessages([
		"Location.hierarchy.heading",
		"general.edit",
		"general.new",
		"openhmis.cashier.cashPoint.created.success",
		"openhmis.cashier.cashPoint.updated.success",
		"openhmis.cashier.cashPoint.retired.success",
		"openhmis.cashier.cashPoint.unretired.success",
		"openhmis.cashier.cashPoint.confirm.delete",
		"openhmis.cashier.cashPoint.deleted.success",
		"openhmis.cashier.cashPoint.name.required",
		"openhmis.cashier.cashPoint.location.required",
		"openhmis.cashier.cashPoint.error.duplicate",
		"openhmis.cashier.cashPoint.retireReason.required",
		"openhmis.cashier.cashPoint.error.notFound",
		"openhmis.cashier.cashPoint.retire",
		"openhmis.cashier.cashPoint.unretire"
	]);
	
</script>


<form id="current-cashPoint" ng-app="cashPointsApp" ng-controller="CashPointsController" novalidate >
	<h1>{{h2SubString}} ${ ui.message('openhmis.cashier.cashPoint.name') }</h1>
	
	<input type="hidden" ng-model="cashPoint.uuid" />
		
	<h3>${ ui.message('general.name') }</h3>
	<input type="text" ng-model="cashPoint.name" style="min-width: 50%;" placeholder="${ ui.message('general.name') }" required autofocus/>
	<p class="checkRequired" ng-hide="nameIsRequiredMsg == '' || nameIsRequiredMsg == undefined">{{nameIsRequiredMsg}}</p>	
	<h3>${ ui.message('general.description') }</h3>
	<input type="text" ng-model="cashPoint.description" size="80" placeholder="${ ui.message('general.description') }" />
	<h3>${ ui.message('Location.title') }</h3>
	<select id="stockRoom-location" class="locations-select" ng-model="cashPoint.location">
		<option ng-repeat="location in locations" value="{{location.uuid}}" ng-bind-html="location.display" ng-selected="cashPoint.location == location.uuid"></option>
	</select>
	<p class="checkRequired" ng-hide="locationIsRequiredMsg == '' || locationIsRequiredMsg == undefined">{{locationIsRequiredMsg}}</p>	
	<br />
	
	<p>
		<span><input type="button" class="cancel" value="${ ui.message('general.cancel') }" ng-click="cancel()" /></span>
		<span><input type="button" class="confirm right" value="${ ui.message('general.save') }"  ng-disabled="cashPoint.name == '' || cashPoint.name == undefined || cashPoint.location == 'All Locations'" ng-click="saveOrUpdate()" /></span>
	</p>
	<br />
	
	<h3 ng-hide="cashPoint.uuid == ''">{{retireOrUnretire}} ${ ui.message('openhmis.cashier.cashPoint.name') }</h3>
	<p ng-hide="cashPoint.uuid == ''">
		<span ng-show="cashPoint.retired">${ ui.message('openhmis.cashier.cashPoint.retired.reason') } <b>{{cashPoint.retireReason}}</b><br /></span>
		<span ng-hide="cashPoint.retired"><input type="text" placeholder="${ ui.message('general.retireReason') }" style="min-width: 50%;" ng-model="cashPoint.retireReason" ng-disabled="cashPoint.retired" /></span>
		<input type="button" ng-disabled="cashPoint.uuid == '' || cashPoint.retireReason == '' || cashPoint.retireReason == null" class="cancel" value="{{retireOrUnretire}}" ng-click="retireOrUnretireFunction()" />
	</p>
	<p class="checkRequired" ng-hide="cashPoint.retireReason != '' || retireReasonIsRequiredMsg == '' || retireReasonIsRequiredMsg == undefined">{{retireReasonIsRequiredMsg}}</p>
	
	<h3 ng-hide="cashPoint.uuid == ''">${ ui.message('openhmis.cashier.cashPoint.delete') }</h3>
	<p>
		<input type="button" ng-hide="cashPoint.uuid == ''" class="cancel" value="${ ui.message('general.purge') }" ng-click="purge()"/>
	</p>
	
</form>