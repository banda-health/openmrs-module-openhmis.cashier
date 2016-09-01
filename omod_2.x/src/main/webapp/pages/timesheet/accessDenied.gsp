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

<div>
	<div>
		<table class="header-title">
			<span class="h1-substitue-left" style="float:left;">
				${ui.message('openhmis.cashier.general.error')}
			</span>
		</table>
	</div>
	<br/>

	<div>
		<p>
			${ui.message('openhmis.cashier.timesheet.entry.error.notProvider')}
		</p>
	</div>
	<br/>
	<br/>
</div>
