
package EDPAct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistrationForm extends JFrame implements ActionListener {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JRadioButton maleButton, femaleButton;
    private JComboBox<String> countryBox;
    private JButton submitButton, clearButton;
    private JTextArea outputArea;

    public RegistrationForm() {
        setTitle("User Registration Form");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        // the form details inpyt
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        formPanel.add(genderPanel);

        formPanel.add(new JLabel("Country:"));
        String[] countries = {"Philippines", "USA", "Canada", "Japan", "Australia", "China", "India", "United Kingdom", "Germany", "France",  
"Italy", "South Korea", "Brazil", "Mexico", "Spain", "Russia", "South Africa", "Thailand", "Malaysia",  
"Singapore", "Indonesia", "Vietnam", "Netherlands", "New Zealand", "Argentina", "Saudi Arabia", "United Arab Emirates"
};
        countryBox = new JComboBox<>(countries);
        formPanel.add(countryBox);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");
        submitButton.setPreferredSize(new Dimension(100, 35));
        clearButton.setPreferredSize(new Dimension(100, 35));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Output Dets
        JPanel outputPanel = new JPanel();
        outputPanel.setBorder(BorderFactory.createTitledBorder("Entered Details"));
        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.BOLD, 12));
        outputArea.setBackground(new Color(230, 230, 230));
        outputArea.setMargin(new Insets(5, 5, 5, 5));
        outputPanel.add(outputArea);
        add(outputPanel, BorderLayout.NORTH);

        // EventListeners
        submitButton.addActionListener(this);
        clearButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String gender = maleButton.isSelected() ? "Male" : "Female";
            String country = (String) countryBox.getSelectedItem();

            outputArea.setText("Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Password: " + password + "\n" +
                    "Gender: " + gender + "\n" +
                    "Country: " + country);
        }

        if (e.getSource() == clearButton) {
            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            maleButton.setSelected(false);
            femaleButton.setSelected(false);
            countryBox.setSelectedIndex(0);
            outputArea.setText("");
        }
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
