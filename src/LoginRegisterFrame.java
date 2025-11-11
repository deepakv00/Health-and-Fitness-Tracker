/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author deepak shetty
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegisterFrame extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/user_auth";
    private static final String USER = "root";
    private static final String PASSWORD = "Killimanjaro99$";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginRegisterFrame() {
        setTitle("Login or Register");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authenticateUser(username, password)) {
            openFitnessTrackerFrame(username);
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Please check your credentials.");
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (registerUser(username, password)) {
            openFitnessTrackerFrame(username);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please choose a different username.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            // Handle the exception according to your application's needs
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Handle the exception according to your application's needs
            e.printStackTrace();
        }
        return false;
    }

    private void openFitnessTrackerFrame(String username) {
        FitnessTrackerFrame fitnessTrackerFrame = new FitnessTrackerFrame(username);
        fitnessTrackerFrame.setVisible(true);
        dispose();  // Close the current frame
    }

    public static void main(String[] args) {
        // Run the LoginRegisterFrame as the main entry point
        SwingUtilities.invokeLater(() -> {
            LoginRegisterFrame loginRegisterFrame = new LoginRegisterFrame();
            loginRegisterFrame.setVisible(true);
        });
    }
}
