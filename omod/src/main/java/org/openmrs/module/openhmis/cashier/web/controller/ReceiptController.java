package org.openmrs.module.openhmis.cashier.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.jasperreport.JasperReport;
import org.openmrs.module.jasperreport.JasperReportConstants;
import org.openmrs.module.jasperreport.JasperUtil;
import org.openmrs.module.jasperreport.ReportGenerator;
import org.openmrs.module.openhmis.cashier.api.IBillService;
import org.openmrs.module.openhmis.cashier.api.model.Bill;
import org.openmrs.module.openhmis.cashier.web.CashierWebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = CashierWebConstants.RECEIPT)
public class ReceiptController {
	@RequestMapping(method=RequestMethod.GET)
	public String get(@RequestParam(value = "receiptNumber", required = false) String receiptNumber, HttpServletResponse response) throws IOException {
		if (receiptNumber != null) {
			String fileName = receiptNumber.replaceAll("\\W", "") + ".pdf";
			File file = new File(
				JasperUtil.getReportDirPath() + File.separator
				+ JasperReportConstants.GENERATED_REPORT_DIR_NAME + File.separator
				+ fileName);
			if (file.exists())
				return "redirect:" + CashierWebConstants.REPORT_DOWNLOAD_URL + "?reportName=" + fileName;
			else {
				IBillService service = Context.getService(IBillService.class);
				Bill bill = service.getBillByReceiptNumber(receiptNumber);
				if (bill == null) {
					response.sendError(404, "Could not find bill with receipt number \""+receiptNumber+"\""); return null;
				}
				AdministrationService adminService = Context.getAdministrationService();
				Integer reportId;
				String reportName;
				try {
					reportId = Integer.parseInt(adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_ID_PROPERTY));
					reportName = adminService.getGlobalProperty(CashierWebConstants.RECEIPT_REPORT_NAME_PROPERTY);
				} catch (Exception e) {
					response.sendError(500, "Configuration error: need to specify global options for default report ID and name."); return null;
				}
				JasperReport report = new JasperReport(reportId);
				report.setFileName(reportName);
				report.setName(receiptNumber);
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("billId", bill.getId());
				try {
					ReportGenerator.generate(report, params, false, true);
				} catch (IOException e) {
					response.sendError(500, "Error generating report for receipt \""+receiptNumber+"\""); return null;
				}
				return "redirect:" + CashierWebConstants.REPORT_DOWNLOAD_URL + "?reportName=" + fileName;
			}
		}
		response.sendError(404);
		return null;
	}
}
