package jdbcdemo;

import java.sql.*;
import java.util.Scanner;

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

    // Method to display all records
    public static void displayRecords(String tableName) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "SELECT * FROM " + tableName;
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
                }
                System.out.println();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println("Query failed: " + e.getMessage());
            }
        }
    }

    // Method to insert a record
    public static void insertRecord(String tableName, String firstName, String lastName, int age, String gender) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "INSERT INTO " + tableName + " (first_name, last_name, age, gender) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setInt(3, age);
                pstmt.setString(4, gender);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " record(s) inserted.");
            } catch (SQLException e) {
                System.out.println("Insertion failed: " + e.getMessage());
            }
        }
    }

    // Method to update a record based on ID
    public static void updateRecord(String tableName, int id, String firstName, String lastName, int age, String gender) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "UPDATE " + tableName + " SET first_name = ?, last_name = ?, age = ?, gender = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setInt(3, age);
                pstmt.setString(4, gender);
                pstmt.setInt(5, id);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " record(s) updated.");
            } catch (SQLException e) {
                System.out.println("Update failed: " + e.getMessage());
            }
        }
    }

    // Method to delete a record based on ID
    public static void deleteRecord(String tableName, int id) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " record(s) deleted.");
            } catch (SQLException e) {
                System.out.println("Deletion failed: " + e.getMessage());
            }
        }
    }

    // Method to delete all records (truncate table)
    public static void deleteAllRecords(String tableName) {
        Connection connection = connect();
        if (connection != null) {
            String sql = "TRUNCATE TABLE " + tableName;
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
                System.out.println("All records deleted.");
            } catch (SQLException e) {
                System.out.println("Truncate failed: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String tableName = "employees";
        System.out.println("\nWelcome to the employee management system!");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Display all records");
            System.out.println("2. Insert a record");
            System.out.println("3. Update a record");
            System.out.println("4. Delete a record");
            System.out.println("5. Delete all records");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayRecords(tableName);
                    break;
                case 2:
                    System.out.print("Enter first name: ");
                    String firstName = scanner.next();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.next();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    System.out.print("Enter gender: ");
                    String gender = scanner.next();
                    insertRecord(tableName, firstName, lastName, age, gender);
                    break;
                case 3:
                    System.out.print("Enter ID to update: ");
                    int updateId = scanner.nextInt();
                    System.out.print("Enter new first name: ");
                    firstName = scanner.next();
                    System.out.print("Enter new last name: ");
                    lastName = scanner.next();
                    System.out.print("Enter new age: ");
                    age = scanner.nextInt();
                    System.out.print("Enter new gender: ");
                    gender = scanner.next();
                    updateRecord(tableName, updateId, firstName, lastName, age, gender);
                    break;
                case 4:
                    System.out.print("Enter ID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteRecord(tableName, deleteId);
                    break;
                case 5:
                    deleteAllRecords(tableName);
                    break;
                case 6:
                	System.out.println("Thankyou for using this management system!");
                    System.out.println("Exiting database.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
