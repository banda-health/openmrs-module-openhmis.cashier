<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("openhmis.cashier.page.timesheet.box.title") ])

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-sanitize.min.js")


    ui.includeCss("openhmis.commons", "bootstrap.css")
    ui.includeCss("openhmis.commons", "entities2x.css")
    ui.includeCss("openhmis.cashier", "entity.css")

    ui.includeJavascript("uicommons", "angular-ui/ui-bootstrap-tpls-0.11.2.min.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "datetimepicker/bootstrap-datetimepicker.min.js")
    ui.includeCss("uicommons", "datetimepicker.css")

    /* load re-usables/common modules */
    ui.includeFragment("openhmis.commons", "loadReusableModules")

    /* load item modules */
    ui.includeJavascript("openhmis.cashier", "timesheet/models/entity.model.js")
    ui.includeJavascript("openhmis.cashier", "timesheet/services/entity.restful.services.js")
    ui.includeJavascript("openhmis.cashier", "timesheet/controllers/entity.controller.js")
    ui.includeJavascript("openhmis.cashier", "timesheet/services/entity.functions.js")
    ui.includeJavascript("openhmis.cashier", "constants.js")

%>

<script data-main="timesheet/configs/entity.main" src="/${ ui.contextPath() }/moduleResources/uicommons/scripts/require/require.js"></script>

<div id="entitiesApp">
    <div ui-view></div>
</div>
