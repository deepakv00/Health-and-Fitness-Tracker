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

public class FitnessTrackerFrame extends JFrame {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/user_auth";
    private static final String USER = "root";
    private static final String PASSWORD = "Killimanjaro99$";

    private String username;
    private JLabel stepsLabel;
    private JLabel caloriesLabel;
    private JLabel waterIntakeLabel;
    private JLabel heartbeatLabel;
    private JLabel sleepLabel;
    private JTextField stepsInput;
    private JTextField caloriesInput;
    private JTextField waterIntakeInput;
    private JTextField heartbeatInput;
    private JTextField sleepInput;
    private JButton logDataButton;

    public FitnessTrackerFrame(String username) {
        this.username = username;

        setTitle("Fitness Tracker - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font inputFont = new Font("Arial", Font.PLAIN, 14);

        stepsLabel = new JLabel("Total Steps: ");
        stepsLabel.setFont(labelFont);
        caloriesLabel = new JLabel("Total Calories: ");
        caloriesLabel.setFont(labelFont);
        waterIntakeLabel = new JLabel("Total Water Intake: ");
        waterIntakeLabel.setFont(labelFont);
        heartbeatLabel = new JLabel("Average Heartbeat: ");
        heartbeatLabel.setFont(labelFont);
        sleepLabel = new JLabel("Total Sleep Hours: ");
        sleepLabel.setFont(labelFont);

        stepsInput = new JTextField(10);
        stepsInput.setFont(inputFont);
        caloriesInput = new JTextField(10);
        caloriesInput.setFont(inputFont);
        waterIntakeInput = new JTextField(10);
        waterIntakeInput.setFont(inputFont);
        heartbeatInput = new JTextField(10);
        heartbeatInput.setFont(inputFont);
        sleepInput = new JTextField(10);
        sleepInput.setFont(inputFont);

        logDataButton = new JButton("Log Data");
        logDataButton.setFont(labelFont);
        logDataButton.setBackground(new Color(70, 130, 180)); // Steel blue button color
        logDataButton.setForeground(Color.WHITE); // White text color
        logDataButton.setFocusPainted(false); // Remove button border on focus
        logDataButton.setBorderPainted(false); // Remove button border
        logDataButton.setOpaque(true); // Make button opaque for background color to show
        logDataButton.setPreferredSize(new Dimension(120, 40)); // Set button size
        logDataButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        // Align labels and input fields in a table-like format
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(stepsLabel, gbc);
        gbc.gridy++;
        panel.add(caloriesLabel, gbc);
        gbc.gridy++;
        panel.add(waterIntakeLabel, gbc);
        gbc.gridy++;
        panel.add(heartbeatLabel, gbc);
        gbc.gridy++;
        panel.add(sleepLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(stepsInput, gbc);
        gbc.gridy++;
        panel.add(caloriesInput, gbc);
        gbc.gridy++;
        panel.add(waterIntakeInput, gbc);
        gbc.gridy++;
        panel.add(heartbeatInput, gbc);
        gbc.gridy++;
        panel.add(sleepInput, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(logDataButton, gbc);

        add(panel);

        logDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logData();
            }
        });
    }

    private void logData() {
    try {
        int steps = Integer.parseInt(stepsInput.getText());
        int calories = Integer.parseInt(caloriesInput.getText());
        int waterIntake = Integer.parseInt(waterIntakeInput.getText());
        int heartbeat = Integer.parseInt(heartbeatInput.getText());
        int sleepHours = Integer.parseInt(sleepInput.getText());

        // Log data in the respective tables
        logDataInTable("user_steps", "steps", steps);
        logDataInTable("user_calories", "calories", calories);
        logDataInTable("user_water_intake", "water_intake", waterIntake);
        logDataInTable("user_heartbeat", "heartbeat", heartbeat);
        logDataInTable("user_sleep", "sleep_hours", sleepHours);

        // Update the labels
        stepsLabel.setText("Total Steps: " + getTotalFromDatabase("user_steps", "steps"));
        caloriesLabel.setText("Total Calories: " + getTotalFromDatabase("user_calories", "calories"));
        waterIntakeLabel.setText("Total Water Intake: " + getTotalFromDatabase("user_water_intake", "water_intake"));
        heartbeatLabel.setText("Average Heartbeat: " + getAverageFromDatabase("user_heartbeat", "heartbeat"));
        sleepLabel.setText("Total Sleep Hours: " + getTotalFromDatabase("user_sleep", "sleep_hours"));

        // Check goals
        checkGoals(steps, calories, waterIntake, heartbeat, sleepHours);

        // Clear input fields
        stepsInput.setText("");
        caloriesInput.setText("");
        waterIntakeInput.setText("");
        heartbeatInput.setText("");
        sleepInput.setText("");
    } catch (NumberFormatException ex) {
        // Handle invalid input (non-integer)
        JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.");
    }
}

private void checkGoals(int steps, int calories, int waterIntake, int heartbeat, int sleepHours) {
    StringBuilder message = new StringBuilder("<html><body style='width: 300px;'>");

    // Check steps goal
    if (steps >= 8000) {
        message.append("<b>✔ Steps:</b> Congratulations! You've reached your goal of 8000 steps.<br>");
    } else {
        message.append("<b>❌ Steps:</b> You didn't achieve the goal of 8000 steps. Keep moving!<br>");
    }

    // Check calories goal (assuming a goal of burning 500 calories)
    if (calories >= 500) {
        message.append("<b>✔ Calories:</b> Congratulations! You've burned 500 calories or more.<br>");
    } else {
        message.append("<b>❌ Calories:</b> You didn't achieve the goal of burning 500 calories. Watch your diet!<br>");
    }

    // Check water intake goal (assuming a goal of drinking 2 liters)
    if (waterIntake >= 2000) {
        message.append("<b>✔ Water Intake:</b> Congratulations! You've consumed at least 2 liters of water.<br>");
    } else {
        message.append("<b>❌ Water Intake:</b> You didn't achieve the goal of consuming 2 liters of water. Stay hydrated!<br>");
    }

    // Check sleep goal (assuming a goal of 7 hours)
    if (sleepHours >= 7) {
        message.append("<b>✔ Sleep:</b> Congratulations! You've slept for 7 hours or more.<br>");
    } else {
        message.append("<b>❌ Sleep:</b> You didn't achieve the goal of sleeping for 7 hours. Get some rest!<br>");
    }

    // Check heartbeat goal (assuming a maximum healthy heartbeat of 100)
    if (heartbeat <= 100) {
        message.append("<b>✔ Heartbeat:</b> Your heartbeat is within the healthy range. Keep it up!<br>");
    } else {
        message.append("<b>❌ Heartbeat:</b> Your heartbeat is above the healthy range. Take care of your health!<br>");
    }

    message.append("</body></html>");

    JOptionPane.showMessageDialog(this, message.toString(), "Goal Feedback", JOptionPane.INFORMATION_MESSAGE);
}



    private int getTotalFromDatabase(String tableName, String columnName) {
        String selectSQL = "SELECT COALESCE(SUM(" + columnName + "), 0) as total FROM " + tableName + " WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getAverageFromDatabase(String tableName, String columnName) {
        String selectSQL = "SELECT COALESCE(AVG(" + columnName + "), 0) as average FROM " + tableName + " WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("average");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void logDataInTable(String tableName, String columnName, int value) {
        String insertSQL = "INSERT INTO " + tableName + " (username, " + columnName + ") VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, value);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FitnessTrackerFrame("SampleUser").setVisible(true);
        });
    }
}