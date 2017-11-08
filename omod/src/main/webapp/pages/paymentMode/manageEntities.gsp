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
            label: "${ ui.message("openhmis.cashier.manage.module")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/cashier/cashierManageModule.page'
        },
        {
            label: "${ ui.message("openhmis.cashier.admin.paymentModes")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>

    <div id="manage-entities-header">
        <span class="h1-substitue-left" style="float:left;">
            ${ ui.message('openhmis.cashier.admin.paymentModes') }
        </span>
        <span style="float:right;">
            <a class="button confirm" ui-sref="new">
                <i class="icon-plus"></i>
                ${ ui.message('openhmis.cashier.paymentMode.new') }
            </a>
        </span>
    </div>
    <br/><br/><br/>

    <div ng-controller="ManagePaymentModesController">
        <div id="entities">
            <table style="margin-bottom:5px;" class="manage-paymentmodes-table">
                <thead>
                <tr>
                    <th>${ui.message('general.name')}</th>
                    <th>${ui.message('general.description')}</th>
                </tr>
                </thead>
                <tbody>
                <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" ui-sref="edit({uuid: entity.uuid})">
                    <td ng-style="strikeThrough(entity.retired)">{{entity.name}}</td>
                    <td ng-style="strikeThrough(entity.retired)">{{entity.description}}</td>
                </tr>
                </tbody>
            </table>

            ${ui.includeFragment("openhmis.commons", "paginationFragment")}
        </div>
    </div>
</div>
