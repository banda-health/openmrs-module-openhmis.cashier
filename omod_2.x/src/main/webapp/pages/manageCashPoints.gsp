<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.cashier.admin.cashPoints") ])
    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-sanitize.min.js")
    ui.includeCss("openhmis.cashier", "bootstrap.css")
    ui.includeCss("openhmis.cashier", "cashpoint_2x.css")
    ui.includeJavascript("openhmis.cashier", "cashPointsController.js")
%>

<script type="text/javascript">
	var breadcrumbs = [
	    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
	    { label: "${ ui.message("openhmis.cashier.page")}" , link: '${ui.pageLink("openhmis.cashier", "cashierLanding")}'},
	    { label: "${ ui.message("openhmis.cashier.manage.module")}", link: 'cashier/cashierManageModule.page' },
	    { label: "${ ui.message("openhmis.cashier.admin.cashPoints")}" }
	];
	
	var jq = jQuery;
	
	jq(document).ready(function () {//supports reseting search phrase to blank
	    jq(".searchinput").keyup(function () {
	        jq(this).next().toggle(Boolean(jq(this).val()));
	    });
	    jq(".searchclear").toggle(Boolean(jq(".searchinput").val()));
	    jq(".searchclear").click(function () {
	    	jq(this).prev().val('').focus();
	    	jq(this).prev().trigger('input');
	        jq(this).hide();
	    });
	});
	
	emr.loadMessages([
		"Location.hierarchy.heading"
	]);
</script>

<div id="cashPoints-body">
	<div id="manage-cashPoints-header">
		<span class="h1-substitue-left" style="float:left;">
			${ ui.message('openhmis.cashier.admin.cashPoints') }
		</span>
		<span style="float:right;">
			<a class="button confirm" href="cashPoint.page" >
				<i class ="icon-plus"></i>
		        ${ ui.message('openhmis.cashier.cashPoint.new') }
		    </a>
		</span>
	</div>
	<br /><br /><br />
	<div id="display-cashPoints" ng-app="cashPointsApp" ng-controller="CashPointsController">
		<div id="cashPoints">
			<div class="btn-group">
				<span style="float:left;">
					<select class="locations-select" style="height: 2.1em;" ng-model="selectedLocation" ng-change="setLocationFilter()">
						<option ng-repeat="location in locations" value="{{location.uuid}}" ng-bind-html="location.display"></option>
					</select>
				</span>
				&nbsp;&nbsp
				<span style="float:right;">
					<input type="text" ng-model="searchByName" class="field-display ui-autocomplete-input form-control searchinput" placeholder="${ ui.message('openhmis.cashier.cashPoint.enterSearchPhrase') }" size="40" autofocus>
					<span id="searchclear" class="searchclear icon-remove-circle"></span>
				</span>
			</div>
			
			<br /><br />
			<table style="margin-bottom:5px;">
				<thead>
					<tr>
						<th>${ ui.message('general.name') }</th>
						<th>${ ui.message('Location.location') }</th>
						<th>${ ui.message('general.description') }</th>
					</tr>
				</thead>
				<tbody>
					<tr class="clickable-tr" ng-repeat="cashPoint in cashPoints | filter:searchByName | orderBy: 'name' | startFrom:currentPage*10 | limitTo:10" ng-click="loadcashPoint(cashPoint.uuid)">
						<td ng-style="strikeThrough(cashPoint.retired)">{{cashPoint.name}}</td>
						<td ng-style="strikeThrough(cashPoint.retired)">{{cashPoint.location.display}}</td>
						<td ng-style="strikeThrough(cashPoint.retired)">{{cashPoint.description}}</td>
					</tr>
				</tbody>
			</table>
			<div id="below-cashPoints-table">
				<span style="float:left;">
					<div id="showing-cashPoints">
						<span>${ ui.message('openhmis.cashier.general.showing') } <b>{{pagingFrom()}}</b> ${ ui.message('openhmis.cashier.general.to') } <b>{{pagingTo()}}</b></span>
						<span> ${ ui.message('openhmis.cashier.general.of') } <b>{{cashPoints.length}}</b> ${ ui.message('openhmis.cashier.general.entries') }</span>
					</div>
				</span>
				<span style="float:right;">
					<div class="cashPoint-pagination">
						<span><a a-disabled="currentPage == 0" ng-click="currentPage=0">${ ui.message('searchWidget.first') }</a>  </span>
						<span><a a-disabled="currentPage == 0" ng-click="currentPage=currentPage-1">${ ui.message('searchWidget.previous') }</a></span>
						<span><a ng-repeat="page in existingPages()" ng-click="loadPageByNumber(page)" a-disabled="disAbleSinglePage(page)">{{page}} </a></span>
						<span><a a-disabled="currentPage == numberOfPages() - 1 || cashPoints.length == 0" ng-click="currentPage=currentPage+1">${ ui.message('searchWidget.next') }</a></span>
						<span><a a-disabled="currentPage == numberOfPages() - 1 || cashPoints.length == 0" ng-click="currentPage=numberOfPages() - 1">${ ui.message('searchWidget.last') }</a></span>
					</div>
				</span>
				<span style="float:center;">
					<div id="includeVoided-cashPoints">
						<span>&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" ng-model="includeRetired" ng-change="triggerIncludeRetiredcashPoints()" /></span>
						<span>${ ui.message('SearchResults.includeRetired') }</span>
					</div>
				</span>
			</div>
		</div>
	</div>
</div>