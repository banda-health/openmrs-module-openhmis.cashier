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
            label: "${ ui.message("openhmis.cashier.admin.cashPoints")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

</script>

<div id="entities-body">
    <br/>

    <div id="manage-entities-header">
        <span class="h1-substitue-left" style="float:left;">
            ${ ui.message('openhmis.cashier.admin.cashPoints') }
        </span>
        <span style="float:right;">
            <a class="button confirm" ui-sref="new">
                <i class="icon-plus"></i>
                ${ ui.message('openhmis.cashier.cashPoint.new') }
            </a>
        </span>
    </div>
    <br/><br/><br/>

    <div>
        <div id="entities">
            <div class="btn-group">
                ${ui.message('openhmis.cashier.location.name')}:
                <ul class="search-area">
                    <li>
                        <select ng-model="location" ng-change="searchCashpoints(currentPage)" style="height:33px;"
                                ng-options='location.display for location in locations track by location.uuid'>
                            <option value="" selected="selected">${ui.message('openhmis.commons.general.any')}</option>
                        </select>
                    </li>
                    <li>
                        ${ ui.includeFragment("openhmis.commons", "searchFragment", [
                                model: "searchField",
                                onChangeEvent: "searchCashpointsByName(1)",
                                class: ["field-display ui-autocomplete-input form-control searchinput"],
                                placeholder: [ui.message("openhmis.cashier.cashPoint.enterSearchPhrase")]
                        ])}
                    </li>
                </ul>
            </div>

            <br/><br/>
            <table style="margin-bottom:5px;" class="manage-cashpoints-table" id="manage-cashpoints-table">
                <thead>
                <tr>
                    <th>${ui.message('general.name')}</th>
                    <th>${ui.message('general.description')}</th>
                    <th>${ui.message('openhmis.cashier.location.name')}</th>
                </tr>
                </thead>
                <tbody>
                <tr class="clickable-tr" dir-paginate="entity in fetchedEntities | itemsPerPage: limit"
                    total-items="totalNumOfResults" current-page="currentPage" ui-sref="edit({uuid: entity.uuid})">
                    <td ng-style="strikeThrough(entity.retired)">{{entity.name}}</td>
                    <td ng-style="strikeThrough(entity.retired)">{{entity.description}}</td>
                    <td ng-style="strikeThrough(entity.retired)">{{entity.location.display}}</td>
                </tr>
                </tbody>
            </table>

            <div ng-show="fetchedEntities.length == 0">
                <span ng-if="searchField !== ''">
                    <br/>
                    ${ui.message('openhmis.commons.general.preSearchMessage')} - <b> {{searchField}} </b> - {{postSearchMessage}}
                </span>
                <span class="not-found" ng-if="searchField === ''">
                    ${ui.message('openhmis.cashier.cashpoint.notFound')}
                </span>
                <br/><br />
                <span>
                    <input type="checkbox" ng-checked="includeRetired" ng-model="includeRetired"
                             ng-change="searchCashpoints(currentPage)" />
                </span>
                <span>
                    ${ui.message('openhmis.commons.general.includeRetired')}
                </span>
            </div>
            ${ui.includeFragment("openhmis.commons", "paginationFragment", [onChange: "searchCashpoints(currentPage)", onPageChange: "searchCashpoints(currentPage)"])}
        </div>
    </div>
</div>
