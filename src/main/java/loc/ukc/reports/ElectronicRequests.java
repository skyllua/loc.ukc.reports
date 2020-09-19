package loc.ukc.reports;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ElectronicRequests {
    private Map<String, Integer> electronicRequests;
    private Map<String, Integer> electronicRequestsByCityes;
    private ArrayList<Issue> issues;

    private String source;
    private String startDate;
    private String endDate;

    public ElectronicRequests(String source, String startDate, String endDate) {
        this.source = source;
        this.startDate = startDate;
        this.endDate = endDate;

        electronicRequests = new HashMap<>();
        electronicRequestsByCityes = new HashMap<>();
        issues = new ArrayList<>();

        readProblematicIssueFromFile();
        for (Issue issue : issues) {
            electronicRequests.put(issue.getName(), 0);
        }
    }

    private void readProblematicIssueFromFile() {
        try {
//            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/ID_ProblematicIssies.xml"));
            File file = new File(getClass().getClassLoader().getResource("ID_ProblematicIssies.xml").getFile());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            issues.clear();

            while (reader.ready()) {
                String line = reader.readLine();
                String question = line.substring(line.indexOf("<issue>")+7, line.indexOf("</issue>"));
                int lvl = Integer.parseInt(line.substring(line.indexOf("<lvl>")+5, line.indexOf("</lvl>")));
                String[] list = line.substring(line.indexOf("<list>")+6, line.indexOf("</list>")).split(", ");
                int[] intList = new int[list.length];

                for (int i = 0; i < list.length; i++) intList[i] = Integer.parseInt(list[i]);

                issues.add(new Issue(question, lvl, intList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getElectronicRequests() {
        ArrayList<ProblematicIssues> selectedInfo = new ArrayList<>();

        if (DBConnection.getConnection() == null) new DBConnection();

        try {
            PreparedStatement statement =  DBConnection.getConnection().prepareStatement(
                    "SELECT level_1_type_id as \"ID_PR_1\", level_2_type_id as \"ID_PR_2\", COUNT(*) \"COUNT\" " +
                            "FROM ucc_crm.interaction_request_report " +
                            "WHERE source_id = ? AND (start_date BETWEEN ? and ?)  " +
                            "GROUP BY level_1_type_id, level_2_type_id");

            statement.setString(1, source);
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                selectedInfo.add(new ProblematicIssues(result.getInt("ID_PR_1"), result.getInt("ID_PR_2"), result.getInt("COUNT")));
            }

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (selectedInfo.size() > 0) {
            for (ProblematicIssues problematicIssues : selectedInfo) {
                String name = "";
                boolean isFound = false;
                for (Issue issue : issues) {
                    for (int id : issue.getId()) {
                        if ((problematicIssues.getIdPR_lvl_1() == id && issue.getLevel() == 1) || (problematicIssues.getIdPR_lvl_2() == id && issue.getLevel() == 2)) {
                            isFound = true;
                            name = issue.getName();

                        }
                        if (isFound) break;
                    }
//                    if (isFound) break;
                }
                if (electronicRequests.containsKey(name)) {
                    electronicRequests.put(name, electronicRequests.get(name) + problematicIssues.getCount());
                }
            }
        }

        return electronicRequests;
    }

    public Map<String, Integer> getElectronicRequestsByCityes() {
        if (DBConnection.getConnection() == null) new DBConnection();

        try {
            PreparedStatement statement =  DBConnection.getConnection().prepareStatement(
                    "SELECT SUBSTR(Physical_address, INSTR(Physical_address,',')+2, INSTR(Physical_address,',', 1, 2)-INSTR(Physical_address,',')-2) as \"CITY\" " +
                            "FROM ucc_crm.interaction_request_report " +
                            "WHERE source_id = ? AND (start_date BETWEEN ? and ?)");

            statement.setString(1, source);
            statement.setString(2, startDate);
            statement.setString(3, endDate);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String city = result.getString("CITY");
                if (electronicRequestsByCityes.containsKey(city)) {
                    electronicRequestsByCityes.put(city, electronicRequestsByCityes.get(city) + 1);
                } else
                    electronicRequestsByCityes.put(city, 1);
            }

            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return electronicRequestsByCityes;
    }

}
