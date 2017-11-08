<script type="text/javascript">
	var breadcrumbs = [
		{
			icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'
		},
		{
			label: "${ ui.message("openhmis.cashier.page")}",
			link: '${ui.pageLink("openhmis.cashier", "cashierLanding")}'
		},
		{
			label: "${ ui.message("openhmis.cashier.admin.task.dashboard")}",
			link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/cashier/cashierTasksDashboard.page'
		},
		{
			label: "${ ui.message("openhmis.cashier.page.timesheet.box.title")}"
		}
	];
	
	jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
	
	jQuery(".tabs").tabs();

</script>

<div ng-show="accessDenied == false">
	<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
		<table class="header-title">
			<span class="h1-substitue-left" style="float:left;">
				${ui.message('openhmis.cashier.page.timesheet.box.title')}
			</span>
			<span style="float:right;">
				<a id="shiftReportButton" class="btn btn-grey" ui-sref="new"
				   ng-click="generateCashierShiftReport('generaterCashierReport')">
					<i class="icon-download"></i> ${ui.message('openhmis.cashier.page.reports.box.generate.cashier.shift.report.download.button')}
				</a>
			</span>
		</table>
		<br/>
		<fieldset class="format">
			<ul class="table-layout">
				<li class="required">
					<span>${ui.message('openhmis.cashier.cashpoints')}</span>
				</li>
				<li>
					<div class="row">
						<div class="col-sm-10">
							<select class="form-control" ng-required="true"
							        ng-model="entity.cashPoint" id="cashpointDropdown"
							        ng-options='cashpoint.name for cashpoint in cashpoints track by cashpoint.uuid'>
								<option value="">${ui.message('openhmis.cashier.selectCashpoint')}</option>
							</select>
						</div>
					</div>
				</li>
			</ul>
			<ul class="table-layout">
				<li style="vertical-align: top" class="required">
					<span>${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}</span>
				</li>
				<li>
					<div class="row">
						<div class="col-sm-10">
							<input readonly type="datetime"
							       class="form-control required" ng-model="clockIn" style="min-width: 50%;"
							       placeholder="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}"/>
						</div>
						
						<div class="col-sm-1 " ng-show="timesheets == null || showClockOutSection == false">
							<input type="button" class="btn-sm"
							       value="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}"
							       ng-click="loadClockInTime()"/>
						</div>
					</div>
				</li>
			</ul>
			<ul class="table-layout" ng-show="timesheets != null && showClockOutSection == true">
				<li class="required">
					<span>${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}</span>
				</li>
				<li>
					<div class="row">
						<div class="col-sm-10 ">
							<input type="datetime" class="form-control"
							       ng-model="clockOut" style="min-width: 50%;"
							       placeholder="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}"/>
						</div>
						
						<div class="col-sm-1 ">
							<input type="button" class="btn-sm"
							       value="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}"
							       ng-click="loadClockOutTime()"/>
						</div>
					</div>
				</li>
			</ul>
			<ul class="table-layout">
				<li>
					<input type="text" name="cashier" class="form-control hidden" ng-model="entity.cashier"/>
					<input type="text" name="uuid" class="form-control hidden" ng-model="entity.uuid"/>
					<input type="text" name="id" class="form-control hidden" ng-model="entity.id"/>
				</li>
			</ul>
			<br/>
		</fieldset>
		<fieldset class="format">
			<span>
				<input type="button" class="cancel" value="{{messageLabels['general.cancel']}}" ng-click="cancel()"/>
				<input type="submit" class="confirm right"
				       value="${ui.message('general.save')}" ng-click="saveOrUpdate()"/>
			</span>
		</fieldset>
	</form>
	<br/>
	<hr/>
	
	<div id="generaterCashierReport" class="dialog" style="display:none;">
		<div class="dialog-header">
			<span>
				<i class="icon-download"></i>
				
				<h3>${ui.message('openhmis.cashier.page.reports.box.generate.cashier.shift.report.popup.header')}</h3>
			</span>
			<i class="icon-remove cancel" style="float:right; cursor: pointer;" ng-click="closeThisDialog()"></i>
		</div>
		
		<div class="dialog-content form">
			<div class="row">
				<div class="col-md-4 required">
					<br/>
					<span>Select Shift Date</span>
				</div>
				
				<div class="col-md-8">
					${ui.includeFragment("uicommons", "field/datetimepicker",
							[id           : 'shiftDate',
							 label        : '',
							 required     : 'required',
							 formFieldName: 'shiftDatepicker',
							 useTime      : false,
							 name         : 'shiftDate'
							])}
				</div>
			</div>
			<br/>
			
			<div class="row detail-section-border-top" ng-show="showTimesheetRow == false && showSelectShiftDate == false">
				<br/>
				
				<p>${ui.message('openhmis.cashier.page.reports.box.timesheets.shift.date.error')}&nbsp;{{selectedReportDate | date: "EEEE, MMMM d, y"}}</p>
			</div>
			
			<div class="row detail-section-border-top" ng-show="showTimesheetRow == true">
				<br/>
				<h6>${ui.message('openhmis.cashier.page.reports.box.timesheets.shift.date')}</h6>
				<ul><li ng-repeat="timesheet in selectedDatesTimesheet">
					<input name="timesheetId" ng-model="seletedTimesheet"
					       ng-change="selectedTimesheet('{{timesheet.id}}')" ng-value="timesheet"
					       id="{{timesheet.id}}"
					       type="radio"> {{timesheet.display}}
				</li></ul>
			</div>
			
			<div class="row ngdialog-buttons detail-section-border-top">
				<br/>
				<input type="button" class="cancel" value="{{messageLabels['general.cancel']}}"
				       ng-click="closeThisDialog('Cancel')"/>
				<input ng-disabled="timesheetId == null" type="submit" class="confirm right"
				       value="${ui.message('openhmis.cashier.page.reports.box.generate.report')}"
				       ng-click="generateReport()"/>
			</div>
		</div>
	</div>
</div>
