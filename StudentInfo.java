package EDPAct;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StudentInfo {
    private JFrame frame;
    private JTextField nameField;
    private JRadioButton maleButton, femaleButton;
    private JCheckBox mathBox, scienceBox, historyBox, artBox;
    private JComboBox<String> yearLevelBox;
    private JSlider gwaSlider;
    private JLabel gwaLabel;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentInfo() {
        frame = new JFrame("Student Information System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem clearItem = new JMenuItem("Clear");
        exitItem.addActionListener(e -> System.exit(0));
        clearItem.addActionListener(e -> clearForm());
        fileMenu.add(exitItem);
        fileMenu.add(clearItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Student Info System | Armaine Villanueva"));
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        //Name JTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        //Gender JRadioButton
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        formPanel.add(genderPanel, gbc);
        
        //Courses JCheckBox
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Courses Enrolled:"), gbc);
        gbc.gridx = 1;
        JPanel coursePanel = new JPanel(new GridLayout(2, 2));
        mathBox = new JCheckBox("Math");
        scienceBox = new JCheckBox("Science");
        historyBox = new JCheckBox("History");
        artBox = new JCheckBox("Art");
        coursePanel.add(mathBox);
        coursePanel.add(scienceBox);
        coursePanel.add(historyBox);
        coursePanel.add(artBox);
        formPanel.add(coursePanel, gbc);

        //YearLevel JComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Year Level:"), gbc);
        gbc.gridx = 1;
        yearLevelBox = new JComboBox<>(new String[]{"Freshman", "Sophomore", "Junior", "Senior"});
        formPanel.add(yearLevelBox, gbc);

        //GWA JSlider
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("GWA:"), gbc);

        gbc.gridx = 1;
        JPanel gwaPanel = new JPanel(null);
        gwaPanel.setPreferredSize(new Dimension(250, 60));

        gwaSlider = new JSlider(0, 4, 2);
        gwaSlider.setBounds(30, 25, 200, 30);

        gwaLabel = new JLabel("2.0");
        gwaLabel.setBounds(10, 25, 30, 20);

        gwaSlider.addChangeListener(e -> {
            int value = gwaSlider.getValue();
            gwaLabel.setText(String.valueOf(value));
        });

        gwaPanel.add(gwaSlider);
        gwaPanel.add(gwaLabel);
        formPanel.add(gwaPanel, gbc);

        //Progress Bar for Data Submission
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Progress:"), gbc);
        gbc.gridx = 1;
        progressBar = new JProgressBar(0, 100);
        formPanel.add(progressBar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this::handleSubmit);
        formPanel.add(submitButton, gbc);

        tabbedPane.addTab("Input Form", formPanel);

        tableModel = new DefaultTableModel(new String[]{"Name", "Gender", "Courses", "Year Level", "GWA"}, 0);
        table = new JTable(tableModel);
        tabbedPane.addTab("View Data", new JScrollPane(table));

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    //submit and error handling.
    private void handleSubmit(ActionEvent e) {
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "Male" : femaleButton.isSelected() ? "Female" : "";
        StringBuilder courses = new StringBuilder();
        if (mathBox.isSelected()) courses.append("Math, ");
        if (scienceBox.isSelected()) courses.append("Science, ");
        if (historyBox.isSelected()) courses.append("History, ");
        if (artBox.isSelected()) courses.append("Art, ");

        String yearLevel = (String) yearLevelBox.getSelectedItem();
        int gwa = gwaSlider.getValue();

        String coursesStr = courses.length() > 0 ? courses.substring(0, courses.length() - 2) : "";

        if (name.isEmpty() || gender.isEmpty() || coursesStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }

        new Thread(() -> {
            for (int i = 0; i <= 100; i += 20) {
                progressBar.setValue(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
            tableModel.addRow(new Object[]{name, gender, coursesStr, yearLevel, gwa});
            JOptionPane.showMessageDialog(frame, "Data Summary:");
            progressBar.setValue(0);
            clearForm();
        }).start();
    }

    //clear Form medthod
    private void clearForm() {
        nameField.setText("");
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
        mathBox.setSelected(false);
        scienceBox.setSelected(false);
        historyBox.setSelected(false);
        artBox.setSelected(false);
        gwaSlider.setValue(2);
        gwaLabel.setText("2.0");
    }

    public static void main(String[] args) {
        new StudentInfo();
    }
}
