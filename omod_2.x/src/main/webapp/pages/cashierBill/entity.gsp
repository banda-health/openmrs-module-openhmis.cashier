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
            label: "${ ui.message("openhmis.cashier.bill")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));

    jQuery(".tabs").tabs();

</script>
<form name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">
    <span ng-show="uuid === undefined">
        <h3>${ui.message('openhmis.cashier.addBill')}</h3>
    </span>

    <span ng-show="uuid !== undefined">
        <span ng-bind-html="pageTitle"></span>
    </span>

    <div style="float:right;">
        <span ng-show="cashier !== undefined">
            <b>${ui.message('openhmis.cashier.cashier.name')}:</b> {{cashier}} |
        </span>
        <span ng-show="cashPoint !== undefined">
            <b>${ui.message('openhmis.cashier.cashPoint.name')}:</b> {{cashPoint.name}}
        </span>

    </div>

    <span ng-show="cashPoints.length > 0 && uuid === undefined">
        <ul class="table-layout" style="margin: 10px 0px;">
            <li style="width:100px;margin:0px 0px;"><b>${ui.message('openhmis.cashier.cashPoint.name')}:</b></li>
            <li style="width:100px;vertical-align: baseline;">
                <select ng-model="cashPoint" class="right-justify" style="width:150px;height:31px;"
                        ng-options="cashPoint.name for cashPoint in cashPoints">
                    <option value="" ng-if="false"></option>
                </select>
            </li>
        </ul>
    </span>

    <fieldset class="operation">
        ${ui.includeFragment("openhmis.commons", "patientSearchFragment", [
                showPatientDetails: "selectedPatient != ''",
                showPatientSearchBox: "selectedPatient == ''",
                changePatient: "STATUS === 'PENDING'"
        ])}

        <fieldset class="nested" ng-show="previousPayments.length > 0">
            <legend style="width:22%">${ui.message('openhmis.cashier.bill.previousPayments')}</legend>
            <table class="item-stock table-height">
                <tr>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.date')}</th>
                    <th>${ui.message('openhmis.cashier.paymentMode.name')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.details')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</th>
                </tr>
                <tr ng-repeat="previousPayment in previousPayments">
                    <td style="width:20%;">{{previousPayment.dateCreated | date: 'dd-MM-yyyy'}}</td>
                    <td style="width:25%">{{previousPayment.instanceType.name}}</td>
                    <td  style="width:30%;font-size:0.9em;">
                        <span ng-repeat="attribute in previousPayment.attributes">
                            {{attribute.attributeType.name}}:{{attribute.value}}
                        </span>
                    </td>
                    <td class="right-justify" style="width:15%;">{{previousPayment.amountTendered  | number : 2}}</td>
                    <td class="right-justify" style="width:15%;">{{previousPayment.amount  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="nested" ng-show="currentPayments.length > 0">
            <legend style="width:22%">${ui.message('openhmis.cashier.bill.currentPayments')}</legend>
            <table class="item-stock table-height">
                <tr>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.date')}</th>
                    <th>${ui.message('openhmis.cashier.paymentMode.name')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.details')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}</th>
                    <th>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</th>
                </tr>
                <tr ng-repeat="currentPayment in currentPayments">
                    <td style="width:20%;">{{currentPayment.dateCreated | date: 'dd-MM-yyyy'}}</td>
                    <td style="width:25%">{{currentPayment.instanceType.name}}</td>
                    <td style="width:30%;font-size:0.9em;">
                        <span ng-repeat="attribute in currentPayment.attributes">
                            {{attribute.attributeType.name}}:{{attribute.value}}
                        </span>
                    </td>
                    <td class="right-justify" style="width:15%;">{{currentPayment.amountTendered | number : 2}}</td>
                    <td class="right-justify" style="width:15%;">{{currentPayment.amount  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="nested" ng-show="previousLineItems.length > 0">
            <legend style="width:40%">{{previousBillTitle}}</legend>
            <table class="item-stock table-height">
                <tr>
                    <th>${ui.message('openhmis.inventory.item.name')}</th>
                    <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                    <th>${ui.message('openhmis.cashier.item.price')}</th>
                    <th>${ui.message('openhmis.cashier.item.total')}</th>
                </tr>
                <tr ng-repeat="lineItem in previousLineItems">
                    <td style="width:60%;">{{lineItem.itemStock.name}}</td>
                    <td style="width:10%" class="right-justify">{{lineItem.itemStockQuantity}}</td>
                    <td style="width:15%" class="right-justify">{{lineItem.itemStockPrice.price  | number : 2}}</td>
                    <td style="width:15%" class="right-justify">{{lineItem.total  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="nested" ng-show="STATUS !== 'PENDING'">
            <legend>${ui.message('openhmis.cashier.bill.operationLineItems')}</legend>
            <span ng-show="lineItems.length === 0" style="margin:250px;">
                ${ui.message('openhmis.cashier.bill.noLineItems')}
                <br />
            </span>
            <table class="item-stock table-height" ng-show="lineItems.length > 0">
                <tr>
                    <th>${ui.message('openhmis.inventory.item.name')}</th>
                    <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                    <th>${ui.message('openhmis.cashier.item.price')}</th>
                    <th>${ui.message('openhmis.cashier.item.total')}</th>
                </tr>
                <tr ng-repeat="lineItem in lineItems">
                    <td style="width:60%;">{{lineItem.itemStock.name}}</td>
                    <td style="width:10%" class="right-justify">{{lineItem.itemStockQuantity}}</td>
                    <td style="width:15%" class="right-justify">{{lineItem.itemStockPrice.price  | number : 2}}</td>
                    <td style="width:15%" class="right-justify">{{lineItem.total  | number : 2}}</td>
                </tr>
            </table><br />
            <div class="detail-section-border-top">
                <table class="amount-details">
                    <tr>
                        <td></td>
                        <td>${ui.message('openhmis.cashier.item.total')}:</td>
                        <td class="right-justify">{{totalPayableAmount}}</td>
                    </tr>
                    <tr ng-show="totalAmountTendered != 0">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}:</td>
                        <td class="right-justify">{{totalAmountTendered}}</td>
                    </tr>
                    <tr ng-hide="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.payment.detailsTitle.amount')} ${ui.message('openhmis.cashier.bill.due')}:</td>
                        <td class="right-justify">{{totalAmountDue}}</td>
                    </tr>
                    <tr ng-show="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.bill.changeDue')}:</td>
                        <td class="right-justify">{{totalChangeDue}}</td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset class="nested" ng-show="STATUS === 'PENDING'">
            <legend>${ui.message('openhmis.cashier.bill.operationLineItems')}</legend>
            <table class="item-stock">
                <tr>
                    <th></th>
                    <th>${ui.message('openhmis.inventory.item.name')}</th>
                    <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                    <th>${ui.message('openhmis.cashier.item.price')}</th>
                    <th>${ui.message('openhmis.cashier.item.total')}</th>
                </tr>
                <tr ng-repeat="lineItem in lineItems">
                    <td class="item-actions">
                        <input type="image" src="/openmrs/images/trash.gif"
                               tabindex="-1" ng-show="lineItem.selected"
                               title="${ui.message('openhmis.inventory.operations.removeItemStock')}"
                               class="remove" ng-click="removeLineItem(lineItem)">
                    </td>
                    <td>
                        ${ ui.includeFragment("openhmis.commons", "searchFragment", [
                                typeahead: ["stockOperationItem.name for stockOperationItem in searchStockOperationItems(\$viewValue)"],
                                model: "lineItem.itemStock",
                                typeaheadOnSelect: "selectStockOperationItem(\$item, lineItem)",
                                typeaheadEditable: "true",
                                class: ["form-control autocomplete-search"],
                                showRemoveIcon: "false",
                                ngEnterEvent: "addLineItem()",
                                placeholder: [ui.message('openhmis.inventory.item.enterItemSearch')],
                        ])}
                    </td>
                    <td>
                        <input class="right-justify" type="number" ng-model="lineItem.itemStockQuantity"
                               style="width:50px" ng-change="changeItemQuantity(lineItem)" ng-enter="changeItemQuantity(lineItem)" />
                    </td>
                    <td>
                        <select ng-model="lineItem.itemStockPrice" class="right-justify" style="width:150px"
                                ng-options="formatItemPrice(price) for price in lineItem.prices"
                                ng-change="changeItemQuantity(lineItem)">
                            <option value="" ng-if="false"></option>
                        </select>
                    </td>
                    <td class="right-justify">
                        {{lineItem.total | number : 2}}
                    </td>
                </tr>
            </table><br />

            <div class="detail-section-border-top">
                <table class="amount-details">
                    <tr>
                        <td></td>
                        <td>${ui.message('openhmis.cashier.item.total')}:</td>
                        <td class="right-justify">{{totalPayableAmount}}</td>
                    </tr>
                    <tr ng-show="totalAmountTendered != 0">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}:</td>
                        <td class="right-justify">{{totalAmountTendered}}</td>
                    </tr>
                    <tr ng-hide="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.payment.detailsTitle.amount')} ${ui.message('openhmis.cashier.bill.due')}:</td>
                        <td class="right-justify">{{totalAmountDue}}</td>
                    </tr>
                    <tr ng-show="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
                        <td></td>
                        <td>${ui.message('openhmis.cashier.bill.changeDue')}:</td>
                        <td class="right-justify">{{totalChangeDue}}</td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset class="paymentMode" ng-show="STATUS !== 'PAID' && STATUS !== 'ADJUSTED' || (totalChangeDue < 0 && STATUS !== 'PENDING')">
            <legend>${ ui.message("openhmis.cashier.paymentPlural")}</legend>
            <ul class="table-layout">
                <li class="not-required">${ui.message('openhmis.cashier.bill.selectMode')}</li>
                <li>
                    <select ng-model="paymentMode" ng-options="paymentMode.name for paymentMode in paymentModes"
                            ng-change="loadPaymentModeAttributes(paymentMode.uuid)">
                        <option value="" ng-if="false"></option>
                    </select>
                </li>
            </ul>
            <ul class="table-layout">
                <li class="required">${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</li>
                <li>
                    <input type="number" ng-model="amountTendered" required />
                </li>
            </ul>
            <ul class="table-layout" ng-repeat="paymentModeAttribute in paymentModeAttributes">
                <li class="required" ng-if="paymentModeAttribute.required">
                    <span>{{paymentModeAttribute.name}}</span>
                </li>
                <li class="not-required" ng-if="!paymentModeAttribute.required">
                    <span>{{paymentModeAttribute.name}}</span>
                </li>
                <li>
                    <span>
                        <input ng-if="paymentModeAttribute.required" required
                               ng-model="attributes[paymentModeAttribute.uuid].value" />
                        <input ng-if="!paymentModeAttribute.required"
                               ng-model="attributes[paymentModeAttribute.uuid].value" />
                    </span>
                </li>
            </ul>
            <ul class="table-layout">
                <li>
                    <input class="btn gray-button" type="button" value="${ui.message('openhmis.cashier.bill.processPayment')}"
                           ng-click="processPayment()" ng-disabled="amountTendered <= 0 || amountTendered === undefined" />
                </li>
            </ul>
        </fieldset>

        <fieldset class="actions" ng-show="STATUS === 'PENDING'">
            <span>
                <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="cancel()" />
                <input type="button" class="confirm gray-button right" value="${ui.message('openhmis.cashier.bill.saveBill')}" ng-click="saveBill()" />
                <input type="button" class="confirm right" value="${ui.message('openhmis.cashier.bill.postBill')}" ng-click="postBill()" />
                <input type="button" class="confirm right" value="${ui.message('openhmis.cashier.bill.postAndPrint')}" ng-click="postAndPrintBill()" />
            </span>
        </fieldset>

        <fieldset class="actions" ng-show="STATUS === 'POSTED' || STATUS === 'PAID'">
            <span>
                <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="cancel()" />
                <input type="button" class="confirm right" value="${ui.message('openhmis.cashier.bill.printReceipt')}" ng-click="printBill()" />
                <input type="button" class="confirm gray-button right" value="${ui.message('openhmis.cashier.bill.adjustBill')}" ng-click="adjustBill()" />
            </span>
        </fieldset>

        <div id="payment-warning-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <span>
                    <i class="icon-warning-sign"></i>
                    <h3></h3>
                </span>
                <i class="icon-remove cancel show-cursor"  style="float:right;" ng-click="closeThisDialog()"></i>
            </div>
            <div class="dialog-content form">
                <span>{{paymentWarningMessage}}</span>
                <br /><br />
                <div class="ngdialog-buttons detail-section-border-top">
                    <br />
                    <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="closeThisDialog('Cancel')" />
                    <input type="button" class="confirm right" value="Confirm"  ng-click="confirm('OK')" />
                </div>
            </div>
        </div>

        <div id="adjust-bill-warning-dialog" class="dialog" style="display:none;">
            <div class="dialog-header">
                <span>
                    <i class="icon-warning-sign"></i>
                    <h3></h3>
                </span>
                <i class="icon-remove cancel show-cursor" style="float:right;" ng-click="closeThisDialog()"></i>
            </div>
            <div class="dialog-content form">
                <span><b>${ui.message('openhmis.cashier.adjustedReasonPrompt')}</b></span>
                <br /><br /><br />
                <span ng-show="adjustmentReasonRequired">
                    ${ui.message('openhmis.cashier.adjustedReason')}:
                </span>
                <span ng-show="adjustmentReasonRequired">
                    <input type="text" ng-model="adjustmentReason" />
                    <br /><br />
                </span>
                <div class="ngdialog-buttons detail-section-border-top">
                    <br />
                    <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="closeThisDialog('Cancel')" />
                    <input type="button" class="confirm right" value="Confirm"  ng-click="confirm('OK')" />
                </div>
            </div>
        </div>
    </fieldset>
</form>
