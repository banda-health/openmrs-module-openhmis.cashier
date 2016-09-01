<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.cashier.page.reports") ])

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-sanitize.min.js")

    ui.includeCss("openhmis.commons", "bootstrap.css")
    ui.includeCss("openhmis.commons", "entities2x.css")
    ui.includeCss("openhmis.cashier", "entity.css")

    ui.includeJavascript("uicommons", "angular-ui/ui-bootstrap-tpls-0.11.2.min.js")
    ui.includeJavascript("uicommons", "angular-common.js")

    ui.includeJavascript("openhmis.cashier", "bootstrap-dropdown.js")
%>

<input id="reportUrl" type="hidden" value="${ reportUrl }" />

<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        {
            label: "${ ui.message("openhmis.cashier.page")}" ,
            link: '${ui.pageLink("openhmis.cashier", "cashierLanding")}'
        },
        {
            label: "${ ui.message("openhmis.cashier.admin.task.dashboard")}",
            link: '/' + OPENMRS_CONTEXT_PATH + '/openhmis.cashier/cashier/cashierTasksDashboard.page'
        },
        {
            label: "${ ui.message("openhmis.cashier.page.reports")}"
        }
    ];

    jQuery('#breadcrumbs').html(emr.generateBreadcrumbHtml(breadcrumbs));
</script>

<div id="reportPage">
    <h2>${ ui.message("openhmis.cashier.page.reports")}</h2>
    <br />
    <div class="row">
        <div class="col-xs-1">
            <strong>${ui.message("openhmis.cashier.report.name")}:</strong>
        </div>
        <div class="col-xs-4">
            <select id="reportDropdown" class="form-control" onchange="selectReport()">
                    <option value="-1">${ui.message("openhmis.cashier.report.selectDefault")}</option>
                    <% reports.each { %>
                    <option value="${it.reportId}">${it.name}</option>
                    <% } %>
            </select>
        </div>
    </div>
    <br />
    <div class="row">
        <div class="col-xs-12">
            <div class="parameterEntry noReportSelected" >${ui.message("openhmis.cashier.report.noReportSelectedMessage")}</div>
            <div class="dateRange parameterEntry" style="display: none">
                <ul class="table-layout">
                    <li class="required">
                        ${ui.includeFragment("uicommons", "field/datetimepicker",
                                [id           : 'startDate',
                                 label        : 'Begin Date',
                                 required     : 'required',
                                 formFieldName: 'startDate',
                                 useTime      : false,
                                 name         : 'startDate'
                                ])}
                    </li>
                    <li class="required">
                        ${ui.includeFragment("uicommons", "field/datetimepicker",
                                [id           : 'endDate',
                                 label        : 'End Date',
                                 required     : 'required',
                                 formFieldName: 'endDate',
                                 useTime      : false,
                                 name         : 'endDate'
                                ])}
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <br />
    <div class="detail-section-border-top">
        <br/>
        <input type="button" class="cancel" value="${ui.message("general.cancel")}" onClick="cancel()" />
        <div class="right btn-group">
            <button type="button" class="btn confirm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="false">
                ${ui.message('openhmis.cashier.report.generate')} <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="#" onclick="generateReport()">${ui.message('openhmis.cashier.report.generate.pdf')}</a></li>
                <li><a href="#" onclick="generateReport(true)">${ui.message('openhmis.cashier.report.generate.excel')}</a></li>
            </ul>

            <!--<a class="button" href="#" onClick="generateReport(true)">
                ${ui.message('openhmis.cashier.report.generate.excel')}
            </a>
            &nbsp;
            <input type="button" class="confirm right" value="${ui.message('openhmis.cashier.report.generate')}"
                   onClick="generateReport()" />
            <p style="text-align: center;">
                <small>
                    <a href="#" onClick="generateReport(true)">${ui.message('openhmis.cashier.report.generate.excel')}</a>
                </small>
            </p>-->
        </div>
    </div>
</div>

<script type="text/javascript">
    function selectReport() {
        var selectedOption = jQuery("#reportDropdown").val();

        // Hide any visible parameter entry divs
        jQuery(".parameterEntry").hide();

        if (selectedOption >= 0) {
            // All reports currently only have date range parameters so just show that
            jQuery(".dateRange").show();
        } else {
            jQuery(".noReportSelected").show();
        }
    }

    function generateReport(excel) {
        // The uicommons DateTime picker will create an input with the specified ID and "-field" for each date control
        var beginDate = jQuery("#startDate-field").val();
        var endDate = jQuery("#endDate-field").val();

        if (!beginDate || !endDate) {
            emr.errorAlert('${ ui.message('openhmis.cashier.report.error.beginAndEndDate')}');

            return false;
        }

        // Get the dates into the expected format (dd-MM-yyyy)
        beginDate = formatDate(beginDate);
        endDate = formatDate(endDate);

        var reportId = jQuery("#reportDropdown").val();
        if (reportId < 0) {
            emr.errorAlert('${ ui.message('openhmis.cashier.report.error.selectReport')}');

            return false;
        }

        return printReport(excel, reportId, "beginDate=" + beginDate + "&endDate=" + endDate);
    }

    function printReport(excel, reportId, parameters) {
        var reportUrl = jQuery("#reportUrl").val();
        var url = "/" + OPENMRS_CONTEXT_PATH + reportUrl + ".form?reportId=" + reportId  + "&" + parameters;

        if (excel) {
            url += "&format=excel";
        }
        window.open(url, "pdfDownload");

        return false;
    }

    function formatDate(dateStr) {
        var dt = new Date(dateStr);

        return dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
    }

    function cancel() {
        window.location = '${ui.pageLink("openhmis.cashier", "cashierLanding")}';
    }
</script>
