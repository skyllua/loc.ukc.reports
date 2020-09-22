package loc.ukc.reports.controllers;

import loc.ukc.reports.WeeksReport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class MainController {

    @GetMapping("weeksReport")
    public String weeksReport(HttpServletRequest request) throws ParseException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            SimpleDateFormat htmlDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat oracleDF = new SimpleDateFormat("dd.MM.yyyy");
            WeeksReport report = new WeeksReport(oracleDF.format(htmlDF.parse(startDate)), oracleDF.format(htmlDF.parse(endDate)));
            report.getReport();

            return "complete";
        }

        return "weeksReport";
    }

    @GetMapping("monthsReport")
    public String monthsReport(HttpServletResponse response) throws IOException {

        return "monthsReport";
    }
}
