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
			label: "${ ui.message("openhmis.cashier.page.timesheet.box.title")}"
		}
	];

	jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

	jQuery(".tabs").tabs();

</script>

<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
	<fieldset class="format timesheet">
		<legend>${ui.message('openhmis.cashier.page.timesheet.box.title')}</legend>
		<ul class="table-layout">
			<li class="required">
				<span>${ui.message('openhmis.cashier.cashpoints')}</span>
			</li>
			<li>
				<div class="row">
					<div class="col-sm-10">
						<select class="form-control" ng-model="entity.cashPoint"
						        ng-options='cashpoint.name for cashpoint in cashpoints track by cashpoint.uuid'>
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
						<input type="datetime" class="form-control" ng-model="clockIn" style="min-width: 50%;"
						       placeholder="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}"/>
					</div>

					<div class="col-sm-1 " ng-show="timesheets == null || showClockOutSection.length != 0">
						<input type="button" class="btn-sm"
						       value="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}"
						       ng-click="loadClockInTime()"/>
					</div>
				</div>
			</li>
		</ul>
		<ul class="table-layout" ng-show="timesheets != null && showClockOutSection.length == 0">
			<li class="required">
				<span>${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}</span>
			</li>
			<li>
				<div class="row">
					<div class="col-sm-10 ">
						<input type="datetime" class="form-control" ng-model="clockOut" style="min-width: 50%;"
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
			<input type="submit" class="confirm" value="${ui.message('general.save')}" ng-click="saveOrUpdate()"/>
		</span>
	</fieldset>
</form>
<br/>
<hr/>

<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
	<fieldset class="format timesheet">
		<legend>${ui.message('openhmis.cashier.page.reports')}</legend>
		<ul class="table-layout">
			<li>
				${ui.includeFragment("uicommons", "field/datetimepicker",
						[id           : 'shiftDate',
						 label        : 'Select Shift Date',
						 required     : 'required',
						 formFieldName: 'shiftDatepicker',
						 useTime      : false,
						 name         : 'shiftDate'
						])}
			</li>
			<li>
				<br/>
				<input type="submit" class="btn-sm"
				       value="${ui.message('openhmis.cashier.page.reports.box.generate.report')}"
				       ng-click="generateReport()"/>
			</li>
		</ul>
		<br/>
		<ul ng-show="selectedDatesTimesheet != null ">
			<h6>${ui.message('openhmis.cashier.page.reports.box.timesheets.shift.date')}</h6>
			<li ng-repeat="timesheet in selectedDatesTimesheet">
				<input name="timesheetId" ng-model="seletedTimesheet" ng-change="selectedTimesheet('{{timesheet.id}}')" ng-value="timesheet" id="{{timesheet.id}}"
				       type="radio"> {{timesheet.display}}
			</li>
		</ul>
		<br/>
	</fieldset>
</form>
<br/>
