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

public class GoalsSettingFrame extends JFrame {

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
    private JButton setGoalsButton;

    public GoalsSettingFrame(String username) {
        this.username = username;

        setTitle("Set Goals - " + username);
        setSize(400, 300);
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

    stepsLabel = new JLabel("Steps Goal: ");
    stepsLabel.setFont(labelFont);
    caloriesLabel = new JLabel("Calories Goal: ");
    caloriesLabel.setFont(labelFont);
    waterIntakeLabel = new JLabel("Water Intake Goal: ");
    waterIntakeLabel.setFont(labelFont);
    heartbeatLabel = new JLabel("Heartbeat Goal: ");
    heartbeatLabel.setFont(labelFont);
    sleepLabel = new JLabel("Sleep Goal: ");
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

    setGoalsButton = new JButton("Set Goals");
    setGoalsButton.setFont(labelFont);
    setGoalsButton.setBackground(new Color(70, 130, 180)); // Steel blue button color
    setGoalsButton.setForeground(Color.WHITE); // White text color
    setGoalsButton.setFocusPainted(false); // Remove button border on focus
    setGoalsButton.setBorderPainted(false); // Remove button border
    setGoalsButton.setOpaque(true); // Make button opaque for background color to show
    setGoalsButton.setPreferredSize(new Dimension(120, 40)); // Set button size
    setGoalsButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

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
    panel.add(setGoalsButton, gbc);

    add(panel);

    setGoalsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setGoals();
        }
    });
}

    private void setGoals() {
        try {
            int stepsGoalValue = Integer.parseInt(stepsInput.getText());
            int caloriesGoalValue = Integer.parseInt(caloriesInput.getText());
            int waterIntakeGoalValue = Integer.parseInt(waterIntakeInput.getText());
            int heartbeatGoalValue = Integer.parseInt(heartbeatInput.getText());
            int sleepGoalValue = Integer.parseInt(sleepInput.getText());

            saveGoalsToDatabase(stepsGoalValue, caloriesGoalValue, waterIntakeGoalValue, heartbeatGoalValue, sleepGoalValue);

            // Close the current frame
            dispose();

            // Open the fitness tracker frame
            FitnessTrackerFrame fitnessTrackerFrame = new FitnessTrackerFrame(username);
            fitnessTrackerFrame.setVisible(true);
        } catch (NumberFormatException ex) {
            // Handle invalid input (non-integer)
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all goals.");
        }
    }

    private void saveGoalsToDatabase(int stepsGoal, int caloriesGoal, int waterIntakeGoal, int heartbeatGoal, int sleepGoal) {
        String insertSQL = "INSERT INTO user_goals (username, steps_goal, calories_goal, water_intake_goal, heartbeat_goal, sleep_goal) VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE steps_goal = VALUES(steps_goal), calories_goal = VALUES(calories_goal), " +
                "water_intake_goal = VALUES(water_intake_goal), heartbeat_goal = VALUES(heartbeat_goal), sleep_goal = VALUES(sleep_goal)";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, stepsGoal);
            preparedStatement.setInt(3, caloriesGoal);
            preparedStatement.setInt(4, waterIntakeGoal);
            preparedStatement.setInt(5, heartbeatGoal);
            preparedStatement.setInt(6, sleepGoal);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GoalsSettingFrame("SampleUser").setVisible(true);
        });
    }
}
