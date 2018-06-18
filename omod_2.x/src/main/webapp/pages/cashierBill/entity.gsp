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
			label: "${ ui.message("openhmis.cashier.bill")}"
		}
	];
	
	jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
	jQuery(".tabs").tabs();
</script>

<div ng-show="!fullyLoaded" style="margin:200px;">
	<span>${ui.message("openhmis.commons.general.loadingPage")}</span>
	<br/>
	<span style="margin:100px;">
		<img src="${ui.resourceLink("uicommons", "images/spinner.gif")}"/>
	</span>
</div>
<form novalidate ng-show="fullyLoaded" name="entityForm" class="entity-form" ng-class="{'submitted': submitted}" style="font-size:inherit">

    <span style="float:right;" ng-show="uuid !== undefined">
        <a class="button confirm" href="/${ ui.contextPath() }/openhmis.cashier/cashierBill/entities.page#/">
            <i class="icon-plus"></i>
            ${ui.message('openhmis.cashier.addBill')}
        </a>
    </span>

    <span ng-show="uuid === undefined">
        <h3>${ui.message('openhmis.cashier.addBill')}</h3>
    </span>

    <span ng-show="uuid !== undefined">
        <h3>
            <span ng-show="STATUS === 'PENDING'">${ui.message('openhmis.cashier.editBill')} {{adjustedBill.display}}</span>
            <span ng-show="STATUS !== 'PENDING'">${ui.message('openhmis.cashier.viewBill')} {{adjustedBill.display}}</span>
        </h3>

        <ul class='page-title'>
            <li ng-show="adjustedBill.billAdjusted !== null">
                <b>${ui.message("openhmis.cashier.adjustmentOf")}:</b>
                <a href='entities.page#/{{adjustedBill.billAdjusted.uuid}}'>
                    {{adjustedBill.billAdjusted.display || adjustedBill.billAdjusted.uuid}}
                </a>
            </li>
            <li ng-show="adjustedBill.adjustedBy !== null && adjustedBill.adjustedBy.length > 0">
                <b>${ui.message("openhmis.cashier.adjustedBy")}:</b>
                <span ng-repeat="adjustedBy in adjustedBill.adjustedBy">
                    <a href='entities.page#/{{adjustedBy.uuid}}'>
                        {{adjustedBy.display || adjustedBy.uuid}}
                    </a>
                </span>
            </li>
            <li ng-show="adjustedBill.adjustmentReason !== null && adjustedBill.adjustmentReason !== ''">
                <b>${ui.message("openhmis.cashier.adjustedReason")}:</b>
                {{adjustedBill.adjustmentReason}}
            </li>
        </ul>
        <span ng-show="(adjustedBill.billAdjusted !== null && adjustedBill.billAdjusted.display !== null) || (adjustedBill.adjustedBy !== null && adjustedBill.adjustedBy.length > 0)">
            <br />
        </span>

    </span>

    <ul class="bill-info">
        <li ng-show="cashier !== undefined">
            <b>${ui.message('openhmis.cashier.cashier.name')}:</b> {{cashier}}
        </li>
        <li ng-show="dateCreated !== ''">
            <b>${ui.message('openhmis.cashier.date')}:</b> {{dateCreated | date: 'yyyy-MM-dd hh:mm'}}
        </li>
        <li ng-show="cashPoint !== undefined && (STATUS !== 'PENDING' || cashPoints.length === 0)">
            <b>${ui.message('openhmis.cashier.cashPoint.name')}:</b> {{cashPoint.name}}
        </li>
    </ul>

    <span ng-show="cashPoints.length > 0 && uuid === undefined">
        <ul class="bill-info" >
            <li><b>${ui.message('openhmis.cashier.cashPoint.name')}:</b></li>
            <li>
                <select ng-model="cashPoint" class="form-control right-justify"
                        ng-options="cashPoint.name for cashPoint in cashPoints">
                    <option value="" ng-if="false"></option>
                </select>
            </li>
        </ul>
    </span>

    <fieldset class="content createBill">
        ${ui.includeFragment("openhmis.commons", "patientSearchFragment", [
                showPatientDetails: "selectedPatient != ''",
                showPatientSearchBox: "selectedPatient == ''",
                changePatient: "STATUS === 'PENDING'"
        ])}

        <fieldset class="nested" ng-show="previousLineItems.length > 0">
            <legend class="previousBillTitle">{{previousBillTitle}}</legend>
            <table class="line-item table-height previous-bill">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                        <th>${ui.message('openhmis.cashier.item.price')}</th>
                        <th>${ui.message('openhmis.cashier.item.total')}</th>
                    </tr>
                </thead>
                <tr ng-repeat="lineItem in previousLineItems">
                    <td>{{lineItem.item.name}}</td>
                    <td class="right-justify">{{lineItem.itemQuantity}}</td>
                    <td class="right-justify">{{lineItem.itemPrice.price  | number : 2}}</td>
                    <td class="right-justify">{{lineItem.total  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="nested" ng-show="STATUS !== 'PENDING'">
            <legend>${ui.message('openhmis.cashier.bill.lineItemsPlural')}</legend>
            <span ng-show="lineItems.length === 0">
                ${ui.message('openhmis.cashier.bill.noLineItems')}
                <br />
            </span>
            <table class="line-item previous-bill" ng-show="lineItems.length > 0">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                        <th>${ui.message('openhmis.cashier.item.price')}</th>
                        <th>${ui.message('openhmis.cashier.item.total')}</th>
                    </tr>
                </thead>
                <tr ng-repeat="lineItem in lineItems">
                    <td>{{lineItem.item.name}}</td>
                    <td class="right-justify">{{lineItem.itemQuantity}}</td>
                    <td class="right-justify">{{lineItem.itemPrice.price  | number : 2}}</td>
                    <td class="right-justify">{{lineItem.total  | number : 2}}</td>
                </tr>
            </table><br />
            <div class="detail-section-border-top">
	            <div class="row">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.item.total')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalPayableAmount}}</strong></div>
	            </div>
	            <div class="row" ng-show="totalAmountTendered != 0">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalAmountTendered}}</strong></div>
	            </div>
	            <div class="row" ng-hide="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}
		            ${ui.message('openhmis.cashier.bill.due')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalAmountDue}}</strong></div>
	            </div>
	            <div class="row" ng-show="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.bill.changeDue')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalChangeDue}}</strong></div>
	            </div>
            </div>
        </fieldset>

        <fieldset class="nested" ng-show="STATUS === 'PENDING'">
            <legend>${ui.message('openhmis.cashier.bill.lineItemsPlural')}</legend>
            <table class="line-item">
                <thead>
                    <tr>
                        <th></th>
                        <th>${ui.message('openhmis.inventory.item.name')}</th>
                        <th>${ui.message('openhmis.inventory.item.quantity')}</th>
                        <th>${ui.message('openhmis.cashier.item.price')}</th>
                        <th>${ui.message('openhmis.cashier.item.total')}</th>
                    </tr>
                </thead>
                <tr ng-repeat="lineItem in lineItems">
                    <td class="item-actions">
                        <input type="image" src="/openmrs/images/trash.gif"
                               tabindex="-1" ng-show="lineItem.selected"
                               title="${ui.message('openhmis.cashier.item.removeTitle')}"
                               class="remove" ng-click="removeLineItem(lineItem)">
                    </td>
                    <td ng-class="{'not-valid': lineItem.invalidEntry === true}">
                        ${ ui.includeFragment("openhmis.commons", "searchFragment", [
                                typeahead: ["billItem.name for billItem in searchItems(\$viewValue)"],
                                model: "lineItem.item",
                                typeaheadOnSelect: "selectItem(\$item, lineItem, \$index)",
                                typeaheadEditable: "true",
                                class: ["form-control autocomplete-search input-sm"],
                                showRemoveIcon: "false",
                                ngEnterEvent: "addLineItem()",
                                placeholder: [ui.message('openhmis.commons.general.enterItemSearch')],
                        ])}
                    </td>
                    <td>
                        <input id="quantity-{{\$index}}" class="form-control input-sm right-justify" type="number" ng-model="lineItem.itemQuantity"
                               ng-change="changeItemQuantity(lineItem)" ng-enter="changeItemQuantity(lineItem)" />
                    </td>
                    <td>
                        <select ng-model="lineItem.itemPrice" class="form-control input-sm right-justify" style="width:150px"
                                ng-options="formatItemPrice(price) for price in lineItem.prices track by price.uuid"
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
	            <div class="row">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.item.total')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalPayableAmount}}</strong></div>
	            </div>
	            <div class="row" ng-show="totalAmountTendered != 0">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalAmountTendered}}</strong></div>
	            </div>
	            <div class="row" ng-hide="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}
		            ${ui.message('openhmis.cashier.bill.due')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalAmountDue}}</strong></div>
	            </div>
	            <div class="row" ng-show="STATUS === 'PAID' || (STATUS !== 'PAID' && totalChangeDue != 0)">
		            <div class="col-md-8"></div>
		            <div class="col-md-2 "><strong>${ui.message('openhmis.cashier.bill.changeDue')}:</strong></div>
		            <div class="col-md-2 right-justify"><strong>{{totalChangeDue}}</strong></div>
	            </div>
            </div>
        </fieldset>

        <fieldset class="nested" ng-show="previousPayments.length > 0">
            <legend class="paymentTitle">${ui.message('openhmis.cashier.bill.previousPayments')}</legend>
            <table class="line-item table-height previous-payment">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.date')}</th>
                        <th>${ui.message('openhmis.cashier.paymentMode.name')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.details')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</th>
                    </tr>
                </thead>
                <tr ng-repeat="previousPayment in previousPayments">
                    <td>{{previousPayment.dateCreated | date: 'dd-MM-yyyy'}}</td>
                    <td>{{previousPayment.instanceType.name}}</td>
                    <td>
                        <span ng-repeat="attribute in previousPayment.attributes">
                            {{attribute.attributeType.name}}:{{attribute.value.display || attribute.value}} <br/>
                        </span>
                    </td>
                    <td class="right-justify">{{previousPayment.amountTendered  | number : 2}}</td>
                    <td class="right-justify">{{previousPayment.amount  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="nested" ng-show="currentPayments.length > 0">
            <legend class="paymentTitle">${ui.message('openhmis.cashier.bill.currentPayments')}</legend>
            <table class="line-item table-height previous-payment">
                <thead>
                    <tr>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.date')}</th>
                        <th>${ui.message('openhmis.cashier.paymentMode.name')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.details')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.tendered')}</th>
                        <th>${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</th>
                    </tr>
                </thead>
                <tr ng-repeat="currentPayment in currentPayments">
                    <td>{{currentPayment.dateCreated | date: 'dd-MM-yyyy'}}</td>
                    <td>{{currentPayment.instanceType.name}}</td>
                    <td>
                        <span ng-repeat="attribute in currentPayment.attributes">
                            {{attribute.attributeType.name}}:
                            <span ng-show="attribute.value.display !== undefined">{{attribute.value.display}}</span>
                            <span ng-hide="attribute.value.display !== undefined">{{attribute.value}}</span>

                            <br />
                        </span>
                    </td>
                    <td class="right-justify">{{currentPayment.amountTendered | number : 2}}</td>
                    <td class="right-justify">{{currentPayment.amount  | number : 2}}</td>
                </tr>
            </table>
        </fieldset>

        <fieldset class="paymentMode" ng-show="STATUS !== 'PAID' && STATUS !== 'ADJUSTED' || (totalChangeDue < 0 && STATUS !== 'PENDING')">
            <legend>${ ui.message("openhmis.cashier.paymentPlural")}</legend>
            <ul class="table-layout">
                <li class="not-required">${ui.message('openhmis.cashier.bill.selectMode')}</li>
                <li>
                    <select class="form-control" ng-model="paymentMode" ng-options="paymentMode.name for paymentMode in paymentModes"
                            ng-change="loadPaymentModeAttributes(paymentMode.uuid)">
                        <option value="" ng-if="false"></option>
                    </select>
                </li>
            </ul>
            <ul class="table-layout">
                <li class="required">${ui.message('openhmis.cashier.payment.detailsTitle.amount')}</li>
                <li>
                    <input class="form-control" type="number" ng-model="amountTendered" required ng-enter="processPayment()" />
                </li>
            </ul>

            ${ui.includeFragment("openhmis.cashier", "paymentModeFragment")}

            <ul class="table-layout">
                <li>
                    <input class="btn gray-button" type="button" value="${ui.message('openhmis.cashier.bill.processPayment')}"
                           ng-click="processPayment()" ng-disabled="amountTendered === 0 || (uuid === undefined && amountTendered <= 0) || amountTendered === undefined || processing === true" />
                </li>
            </ul>
        </fieldset>

        <span class="actions" ng-show="STATUS === 'PENDING'">
            <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="cancel()" />
            <input type="button" ng-disabled="processing === true" class="confirm right" value="${ui.message('openhmis.cashier.bill.postAndPrint')}" ng-click="postAndPrintBill()" />
            <input type="button" ng-disabled="processing === true" class="confirm btn gray-button right" value="${ui.message('openhmis.cashier.bill.saveBill')}" ng-click="saveBill()" />
            <input type="button" ng-disabled="processing === true" class="confirm btn gray-button right" value="${ui.message('openhmis.cashier.bill.postBill')}" ng-click="postBill()" />
        </span>

        <span class="actions" ng-show="STATUS === 'POSTED' || STATUS === 'PAID'">
            <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="cancel()" />
            <input type="button" ng-disabled="processing === true" class="confirm btn gray-button right" value="${ui.message('openhmis.cashier.bill.printReceipt')}" ng-click="printBill()" />
            <input type="button" ng-hide="ALLOW_BILL_ADJUSTMENT === false" ng-disabled="processing === true" class="confirm btn gray-button right" value="${ui.message('openhmis.cashier.bill.adjustBill')}" ng-click="adjustBill()" />
        </span>

        <div id="payment-warning-dialog" class="dialog hide-dialog">
            <div class="dialog-header">
                <span>
                    <i class="icon-warning-sign"></i>
                    <h3></h3>
                </span>
                <i class="icon-remove cancel show-cursor align-right" ng-click="closeThisDialog()"></i>
            </div>
            <div class="dialog-content form">
                <span>{{paymentWarningMessage}}</span>
                <br /><br />
                <div class="ngdialog-buttons detail-section-border-top">
                    <br />
                    <input type="button" class="cancel" value="${ui.message('general.cancel')}" ng-click="closeThisDialog('Cancel')" />
                    <input id="confirmPayment" type="button" class="confirm right" value="Confirm"  ng-click="confirm('OK')" />
                </div>
            </div>
        </div>
		
		<div id="adjust-bill-warning-dialog" class="dialog hide-dialog">
			<div class="dialog-header">
				<span>
					<i class="icon-warning-sign"></i>
					
					<h3>
						${ui.message('openhmis.cashier.bill.adjustBill')}
					</h3>
				</span>
				<i class="icon-remove cancel show-cursor align-right" ng-click="closeThisDialog()"></i>
			</div>
			
			<div class="dialog-content form">
				<span><b>${ui.message('openhmis.cashier.adjustedReasonPrompt')}</b></span>
				<br/><br/><br/>
				<span ng-show="adjustmentReasonRequired">
					${ui.message('openhmis.cashier.adjustedReason')}:
				</span>
				<span ng-show="adjustmentReasonRequired">
					<input type="text" ng-model="adjustmentReason" required/>
					<br/><br/>
				</span>
				
				<div class="ngdialog-buttons detail-section-border-top">
					<br/>
					<input type="button" class="cancel" value="${ui.message('general.cancel')}"
					       ng-click="closeThisDialog('Cancel')"/>
					<input type="button" class="confirm right" value="Confirm" ng-click="confirm('OK')"
					       ng-disabled="adjustmentReasonRequired && (adjustmentReason === '' || adjustmentReason === undefined)"/>
				</div>
			</div>
		</div>
	</fieldset>
</form>
