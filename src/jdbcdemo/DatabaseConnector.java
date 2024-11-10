package jdbcdemo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/company"; // Replace with your database name
    private static final String USER = "root"; // Replace with your username
    private static final String PASSWORD = "my_password"; // Replace with your password

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    // Method to display all records from a specific table
    public static void displayRecords(String tableName) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "SELECT * FROM " + tableName; // Query to select all records from the specified table
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                // Get the metadata for the result set to display column names
                int columnCount = resultSet.getMetaData().getColumnCount();

                // Display column names
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
                }
                System.out.println();

                // Display each record
                while (resultSet.next()) {
                	
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println("Query failed: " + e.getMessage());
            } finally {
                try {
                    connection.close(); // Close the connection
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        String tableName = "employees"; // Replace with your actual table name
        displayRecords(tableName); // Calling the method to display records
    }
}
