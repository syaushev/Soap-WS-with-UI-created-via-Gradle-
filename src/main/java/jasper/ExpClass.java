package jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.*;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExpClass {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExpClass.class);
    private Map<String, JasperReport> cashMap = new HashMap<>();
    private JasperReport jasperReport;

    public void ExportJasper(Format formatParameter, WebResponse resp, Map<String, Object> map, JRBeanCollectionDataSource jrBeanCollectionDataSource, String nameOfReport) {


        try {

            if (cashMap.containsKey(nameOfReport)) {
                jasperReport = cashMap.get(nameOfReport);
            } else {
                jasperReport = JasperCompileManager.compileReport(WebApplication.get().getServletContext().getRealPath("/WEB-INF/") + nameOfReport);
                cashMap.put(nameOfReport, jasperReport);
            }
            OutputStream outputStream = resp.getOutputStream();


            switch (formatParameter) {
                case pdf:
                    byte[] bytestream = JasperRunManager.runReportToPdf(jasperReport, map, jrBeanCollectionDataSource);

                    resp.setContentType("application/pdf");
                    resp.setContentLength(bytestream.length);
                    outputStream.write(bytestream, 0, bytestream.length);
                    outputStream.close();
                    break;
                case html:

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);

                    HtmlExporter exporter = new HtmlExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                    exporter.exportReport();

                    break;
            }
        } catch (IOException | JRException e) {

            log.warn("Something goes wrong during ExportJasper method operations");
        }

    }
}
