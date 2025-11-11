/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author deepak shetty
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

public class UserAuthenticationApp {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/user_auth";
    private static final String USER = "root";
    private static final String PASSWORD = "Killimanjaro99$";

    public static void main(String[] args) {
        createDatabaseTable();

        SwingUtilities.invokeLater(() -> {
            LoginRegisterFrame loginRegisterFrame = new LoginRegisterFrame();
            loginRegisterFrame.setVisible(true);
        });
    }

    private static void createDatabaseTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) UNIQUE, password VARCHAR(50))";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

