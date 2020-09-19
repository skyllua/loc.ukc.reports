package loc.ukc.reports;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WeeksReport {
    private String startDate;
    private String endDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
//    private String directory = "src/main/resources/";
    private File file = new File(getClass().getClassLoader().getResource("DB_connection.properties").getFile());
    private String directory = file.getParent() + "/";

    public WeeksReport(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void getReport() {
//        startDate = sdf.format(startDate);
//        endDate = sdf.format(startDate);

        long startTime = System.currentTimeMillis();
        Excel excel = new Excel(directory + "Статистика по е-зверненням за " + startDate +  " - " + endDate + ".xlsx");

        createReport(excel, "kmu", startDate, endDate);
        createReport(excel, "portal", startDate, endDate);

        try {
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long finishTime = System.currentTimeMillis();
        System.out.println("\nIt takes " + (finishTime - startTime)/1000f + " seconds");
    }

    public Date strToDate(String date) throws ParseException {
        return sdf.parse(date);
    }

    public Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);

        return c.getTime();
    }

    public void createReport(Excel book, String source, String startDate, String endDate) {
        ElectronicRequests requests = new ElectronicRequests(source, startDate, endDate);

        Map<String, Integer> electronicRequests = sortByValue(requests.getElectronicRequests());
        Map<String, Integer> electronicRequestsByCityes = sortByValue(requests.getElectronicRequestsByCityes());

        book.createSheet(source.toUpperCase());
        book.write(1, 0, "Проблемне питання").bold().center();
        book.write(1, 1, "Кількість звернень").bold().center().wrapText(true);
        book.write(1, 2, "Відсоток").bold().center();

        int i = 2;
        int count = getCountRequests(electronicRequests);
        for (Map.Entry<String, Integer> pair : electronicRequests.entrySet()) {
            float percent = (pair.getValue()*100f/count);
            book.write(i, 0, pair.getKey());
            book.write(i, 1, pair.getValue()).center();
            book.write(i, 2, new DecimalFormat("#0.00").format(percent) + " %").center();
            ++i;
        }

        book.write(i, 1, count).bold().center();

        book.write(1, 4, "Область").bold().center();
        book.write(1, 5, "Кількість звернень").bold().center().wrapText(true);
        count = getCountRequests(electronicRequestsByCityes);
        i = 2;
        for (Map.Entry<String, Integer> pair : electronicRequestsByCityes.entrySet()) {
            book.write(i, 4, pair.getKey());
            book.write(i, 5, pair.getValue()).center();
            ++i;
        }

        book.write(i, 5, count).bold().center();

        book.autoSize(0);
        book.autoSize(4);

        book.setColumnWidth(1, 10*256);
        book.setColumnWidth(5, 10*256);
    }

    public int getCountRequests(Map<String, Integer>  map) {
        int count = 0;

        for (Map.Entry<String, Integer> pair : map.entrySet()) {
            count += pair.getValue();
        }

        return count;
    }

    public Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
