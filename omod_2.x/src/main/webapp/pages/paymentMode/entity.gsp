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
            label: "${ ui.message("openhmis.cashier.admin.paymentModes")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/paymentMode/entities.page#/'
        },
        {
            label: "${ ui.message("openhmis.cashier.paymentMode.edit")}"
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
            <li class="required">
                <span>${ui.message('openhmis.cashier.paymentMode.sortOrder')}</span>
            </li>
            <li>
                <input class="form-control" type="number" ng-model="entity.sortOrder" style="min-width: 50%;"
                       placeholder="${ui.message('openhmis.cashier.paymentMode.sortOrder')}" required/>
            </li>
        </ul>
        <ul class="table-layout">
            <li class="not-required">
                <span>${ui.message('openhmis.cashier.paymentMode.attributeTypes')}</span>
            </li>
            <li>
                <div class="bbf-editor">
                    <div class="bbf-list" name="attributeTypes">
                        <ul class="attributes-layout">
                            <li ng-repeat="attributeType in entity.attributeTypes track by attributeType.uuid || attributeType.id">
                                <a href="" ng-click="removeAttributeType(attributeType)">
                                    <i class="icon-remove"></i>
                                </a>
                                <a href="" ng-click="editAttributeType(attributeType)">{{attributeType.name}}</a>
                            </li>
                        </ul>

                        <div class="bbf-actions">
                            <button type="button" data-action="add" ng-click="addAttributeType()">Add</button>
                        </div>

                        <div id="attribute-types-dialog" class="dialog" style="display:none;">
                            <div class="dialog-header">
                                <span ng-show="addAttributeTypeTitle != ''">
                                    <i class="icon-plus-sign"></i>
                                    <h3>{{addAttributeTypeTitle}}</h3>
                                </span>
                                <span ng-show="editAttributeTypeTitle != ''">
                                    <i class="icon-edit"></i>
                                    <h3>{{editAttributeTypeTitle}}</h3>
                                </span>
                                <i class="icon-remove cancel show-cursor"  style="float:right;" ng-click="closeThisDialog()"></i>
                            </div>

                            <div class="dialog-content form" id="dialog-bottom">
                                <ul class="table-layout dialog-table-layout">
                                    <li class="required">
                                        <span>${ui.message('general.name')}</span>
                                    </li>
                                    <li>
                                        <input type="text" style="min-width: 100%;"
                                               placeholder="" required ng-model="attributeType.name"/>
                                    </li>
                                </ul>
                                <ul class="table-layout dialog-table-layout">
                                    <li class="required">
                                        <span>${ui.message('PersonAttributeType.format')}</span>
                                    </li>
                                    <li>
                                        <select id="formatSelect" class="form-control" style="font-size: 11px" ng-model="attributeType.format"
                                                ng-options="field for field in formatFields track by field">
                                            <option value="0"></option>
                                            <option ng-selected="attributeType.format == field"></option>
                                        </select>
                                    </li>
                                </ul>
                                <ul class="table-layout dialog-table-layout">
                                    <li class="not-required">
                                        <span>${ui.message('PersonAttributeType.foreignKey')}</span>
                                    </li>
                                    <li>
                                        <input type="number" ng-model="attributeType.foreignKey"/>
                                    </li>
                                </ul>
                                <ul class="table-layout dialog-table-layout">
                                    <li class="not-required">
                                        <span>${ui.message('PatientIdentifierType.format')}</span>
                                    </li>
                                    <li>
                                        <input type="text" ng-model="attributeType.regExp"/>
                                    </li>
                                </ul>
                                <ul class="table-layout dialog-table-layout">
                                    <li class="not-required">
                                        <span>${ui.message('FormField.required')}</span>
                                    </li>
                                    <li>
                                        <input type="checkbox" ng-model="attributeType.required"/>
                                    </li>
                                </ul>
                                <br/>

                                <div class="ngdialog-buttons detail-section-border-top">
                                    <br/>
                                    <input type="button" class="cancel" value="${ui.message('general.cancel')}"
                                           ng-click="cancel()"/>
                                    <span>
                                        <input type="button" class="confirm right"
                                               ng-disabled="attributeType.name == '' || attributeType.name == undefined
										       || attributeType.format == undefined || attributeType.format == ''"
                                               value="${ui.message('openhmis.cashier.general.confirm')}"
                                               ng-click="saveOrUpdate()"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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
