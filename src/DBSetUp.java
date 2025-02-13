import org.h2.tools.RunScript;
import org.h2.tools.Server;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DBSetUp {

    private static final String URL = "jdbc:h2:~/DineDashH2_2;AUTO_SERVER=TRUE";

    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public static void initializeDatabase() {
       // startH2Server();
        executeScript("src/scripts/create_db_corrected.sql"); // Creates tables
        executeScript("src/scripts/insert_sql_corrected.sql"); // Seeds data
    }

    private static void startH2Server() {
        try {
            Server server = Server.createTcpServer("-tcpAllowOthers", "-tcp", "-web").start();
            System.out.println("✅ H2 Server started at: " + server.getURL());
        } catch (SQLException e) {
            System.err.println("❌ Failed to start H2 Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeScript(String scriptPath) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             FileReader reader = new FileReader(scriptPath)) {
            RunScript.execute(conn, reader);
            System.out.println("✅ SQL script executed: " + scriptPath);
        } catch (IOException | SQLException e) {
            System.err.println("❌ Error executing script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}
