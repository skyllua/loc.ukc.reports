package loc.ukc.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private String DB_URL;
    private String USER;
    private String PASS;
    private String SID;
    private String PORT;

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public DBConnection() {
        Properties prop = new Properties();
        try {
            File file = new File(getClass().getClassLoader().getResource("DB_connection.properties").getFile());
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!prop.isEmpty()) {
            DB_URL = prop.getProperty("url");
            USER = prop.getProperty("user");
            PASS = prop.getProperty("pass");
            SID = prop.getProperty("sid");
            PORT = prop.getProperty("port");

            DB_URL = "jdbc:oracle:thin:@" + DB_URL + ":" + PORT + ":" + SID;

            System.out.println("-- Trying to connect to Oracle DB!");
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("-- Connected to Oracle DB!");

            } catch (Exception e) {
                System.out.println("-- Connection to Database is Failed!");

                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                new DBConnection();
            }
        } else if (connection != null) {
            System.out.println("Properties didn't find!");
        }
    }

}