package loc.ukc.reports.controllers;

import loc.ukc.reports.DBConnection;
import loc.ukc.reports.WeeksReport;
import oracle.jdbc.OracleDriver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public String monthsReport() {
        File file = new File(getClass().getClassLoader().getResource("ID_ProblematicIssies.xml").getFile());


        return "monthsReport";
    }
}
