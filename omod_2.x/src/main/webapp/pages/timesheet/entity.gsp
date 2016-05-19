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
						<input type="datetime-local" class="form-control" ng-model="entity.clockIn" style="min-width: 50%;"
						       placeholder="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}" required />
					</div>
					<div class="col-sm-1 " ng-show="timesheets != null">
						<input type="button" class="btn-sm"
						       value="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.in')}"
						       ng-click=""/>
					</div>
				</div>
			</li>
		</ul>
		<ul class="table-layout" ng-show="timesheets == null">
			<li class="required">
				<span>${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}</span>
			</li>
			<li>
				<div class="row">
					<div class="col-sm-10 ">
						<input type="date" id="clockOut" name="clockOut" class="form-control" ng-model="entity.clockOut" style="min-width: 50%;"
						       placeholder="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}"
						       required/>
					</div>
					<div class="col-sm-1 ">
						<input type="button" class="btn-sm"
						       value="${ui.message('openhmis.cashier.page.timesheet.box.button.clock.out')}"
						       ng-click=""/>
					</div>
				</div>
			</li>
		</li>
		</ul>
		<br/>
	</fieldset>
	<fieldset class="format">
		<span>
			<input type="button" class="confirm" value="${ui.message('general.save')}" ng-click="saveOrUpdate()"/>
		</span>
	</fieldset>
</form>
<br/>
<hr/>
<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
	<fieldset class="format timesheet">
		<legend>${ui.message('openhmis.cashier.page.reports')}</legend>
		<ul class="table-layout">
			<li class="required">
				<span>${ui.message('openhmis.cashier.page.reports.box.select.shift.date')}</span>
			</li>
			<li>
				<div class="row">
					<div class="col-sm-10 ">
						<input type="datetime-local" class="form-control" ng-model="entity.name" style="min-width: 50%;"
						       placeholder="${ui.message('openhmis.cashier.page.reports.box.timesheets.shift.date')}"
						       required/>
					</div>

					<div class="col-sm-1 ">
						<input type="button" class="btn-sm"
						       value="${ui.message('openhmis.cashier.page.reports.box.generate.report')}"
						       ng-click=""/>
					</div>
				</div>
			</li>
		</li>
		</ul>
		<br/>
	</fieldset>
</form>
<br/>
