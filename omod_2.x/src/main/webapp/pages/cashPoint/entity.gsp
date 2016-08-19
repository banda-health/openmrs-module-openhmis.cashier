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
            label: "${ ui.message("openhmis.cashier.admin.cashPoints")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/cashPoint/entities.page##/'
        },
        {
            label: "${ ui.message("openhmis.cashier.cashPoint.name")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

    jQuery(".tabs").tabs();

</script>
<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
    ${ui.includeFragment("openhmis.commons", "editEntityHeaderFragment")}

    <fieldset class="format">
        <ul class="table-layout">
            <li class="required">
                <span>${ui.message('general.name')}</span>
            </li>
            <li>
                <input class="form-control" type="text" ng-model="entity.name" style="min-width: 50%;"
                       placeholder="${ui.message('general.name')}" required/>
            </li>
        </ul>
        <ul class="table-layout">
            <li style="vertical-align: top" class="not-required">
                <span>${ui.message('general.description')}</span>
            </li>
            <li>
                <textarea ng-model="entity.description" placeholder="${ui.message('general.description')}" rows="3"
                          cols="40">
                </textarea>
            </li>
        </ul>
        <ul class="table-layout">
            <li class="not-required">
                <span>${ui.message('openhmis.cashier.location.name')}</span>
            </li>
            <li>
                <select class="form-control" ng-model="entity.location"
                        ng-options='location.display for location in locations track by location.uuid'>
                    <option value="" selected="selected"></option>
                </select>
            </li>
        </ul>
        <br/>
    </fieldset>
    <fieldset class="format">
        <span>
            <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="cancel()" />
            <input type="button" class="confirm right" value="${ui.message('general.save')}" ng-click="saveOrUpdate()" />
        </span>
    </fieldset>
</form>

${ui.includeFragment("openhmis.commons", "retireUnretireDeleteFragment")}
