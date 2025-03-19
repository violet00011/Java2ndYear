package it203finalproject;

import javax.swing.*;
import java.time.LocalTime;
import java.awt.*;
import it203finalproject.BackgrounfPanel;
import java.awt.event.ItemEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsEnvironment;

public class PrisonVisitingSystem extends JFrame {

    private JTextField visitorIDField, visitorNameField, inmateIDField, relationshipField, contactNumberField;
    private JTextArea addressArea;
    private JTextField inmateIDFieldInmateTab, inmateNameField;
    private JTextField policeIDField, policeNameField;
    private JCheckBox approveCheckBox, denyCheckBox;
    private JTextArea displayArea;

    public PrisonVisitingSystem() {
        setTitle("Prison Visiting System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setSize(width - 100, height - 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BackgrounfPanel backgroundPanel = new BackgrounfPanel("C:\\Users\\villa\\OneDrive\\Pictures\\Project\\malolos.png");
        setContentPane(backgroundPanel);

        JPanel verticalSpacer = new JPanel();
        verticalSpacer.setPreferredSize(new Dimension(0, 220));
        add(verticalSpacer, BorderLayout.CENTER);

        JPanel prisonInfoPanel = createPrisonInfoPanel();
        add(prisonInfoPanel, BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel visitorPanel = createVisitorTab(); //Tabbed pane for Visitor Information
        tabbedPane.addTab("Visitor Info", visitorPanel);
        JPanel inmatePanel = createInmateTab(); //Tabbed pane for Inmate Information
        tabbedPane.addTab("Inmate Info", inmatePanel);
        JPanel policePanel = createPoliceTab(); //Tabbed pane for Police Information
        tabbedPane.addTab("Police Info", policePanel);

        tabbedPane.setPreferredSize(new Dimension(getHeight(), 280));
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);    }
    

    // Creates a JPanel that contains a label and a component (e.g., text field or button).
    // This helps in organizing UI elements like form fields, ensuring a structured layout.
    private JPanel createPrisonInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255, 5));

        String fontPath = "C:\\Users\\villa\\Downloads\\FONTS\\Origintech.otf";
        Font customFont = null;
        Font prisonNameFont = null;

        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)).deriveFont(25f);
            prisonNameFont = customFont.deriveFont(45f);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        Color sageGreen = Color.decode("#BBFDF4");

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setOpaque(false);

        northPanel.add(Box.createVerticalStrut(20));

        JLabel prisonIDLabel = new JLabel("Prison ID: 3000");
        if (customFont != null) {
            prisonIDLabel.setFont(customFont);
        }
        prisonIDLabel.setForeground(sageGreen);
        prisonIDLabel.setOpaque(false);
        JPanel prisonIDPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        prisonIDPanel.setOpaque(false);
        prisonIDPanel.add(prisonIDLabel);

        northPanel.add(prisonIDPanel);

        panel.add(northPanel, BorderLayout.NORTH);

        JLabel prisonNameLabel = new JLabel("Malolos City Jail");
        if (prisonNameFont != null) {
            prisonNameLabel.setFont(prisonNameFont);
        }
        prisonNameLabel.setForeground(sageGreen);
        prisonNameLabel.setOpaque(false);
        JPanel prisonNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        prisonNamePanel.setOpaque(false);
        prisonNamePanel.add(prisonNameLabel);

        JLabel locationLabel = new JLabel("2nd floor, Provincial Capitol Building, Malolos City, Bulacan, Philippines");
        if (customFont != null) {
            locationLabel.setFont(customFont);
        }
        locationLabel.setForeground(sageGreen);
        locationLabel.setOpaque(false);
        JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        locationPanel.setOpaque(false);
        locationPanel.add(locationLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(prisonNamePanel);
        centerPanel.add(locationPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        JLabel visitInfoLabel = new JLabel("Visits are open from 8:00 to 15:00");
        visitInfoLabel.setForeground(sageGreen);
        visitInfoLabel.setOpaque(false);

        Font visitInfoFont = new Font("Arial", Font.PLAIN, 17);
        visitInfoLabel.setFont(visitInfoFont);

        JPanel visitInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        visitInfoPanel.setOpaque(false);
        visitInfoPanel.add(visitInfoLabel);

        panel.add(visitInfoPanel, BorderLayout.SOUTH);

        return panel;
    }

    
    //generates a GUI panel that organizes and displays input fields for collecting visitor information,
    //using a vertical layout and custom font styling.
    private JPanel createVisitorTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(12));

        JLabel titleLabel = new JLabel("Visitor Information", JLabel.CENTER);
        try {
            Font titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\villa\\Downloads\\FONTS\\Over There.ttf")).deriveFont(12f); 
            titleLabel.setFont(titleFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        titleLabel.setForeground(new Color(0xC43C58));

        panel.add(titleLabel);

        visitorIDField = new JTextField(55);
        visitorNameField = new JTextField(55);
        relationshipField = new JTextField(55);
        addressArea = new JTextArea(1, 50);
        contactNumberField = new JTextField(55);

        
        panel.add(createLabeledField("Visitor Name:", visitorNameField));
        panel.add(createLabeledField("Relationship to Inmate:", relationshipField));
        panel.add(createLabeledField("Address (Baranggay, Municipality, Province):", new JScrollPane(addressArea)));
        panel.add(createLabeledField("Contact Number:", contactNumberField));
        panel.add(createLabeledField("ID Number:", visitorIDField));
        return panel;
    }

    
    //creates a GUI panel for displaying inmate information,
    //with a custom font for the title and labeled input fields.
    private JPanel createInmateTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(20));

        JLabel titleLabel = new JLabel("Inmate Information");
        titleLabel.setFont(loadCustomFont().deriveFont(12));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(176, 69, 33));
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(16));

        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        inmateIDFieldInmateTab = new JTextField(30);
        inmateNameField = new JTextField(30);

        SpinnerModel minutesModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner minutesSpinner = new JSpinner(minutesModel);
        minutesSpinner.setEditor(new JSpinner.NumberEditor(minutesSpinner, "00"));
        minutesSpinner.setFont(fieldFont);

        SpinnerModel secondsModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner secondsSpinner = new JSpinner(secondsModel);
        secondsSpinner.setEditor(new JSpinner.NumberEditor(secondsSpinner, "00"));
        secondsSpinner.setFont(fieldFont);

        JPanel durationPanel = new JPanel();
        durationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        durationPanel.add(minutesSpinner);
        durationPanel.add(new JLabel("minutes"));
        durationPanel.add(secondsSpinner);
        durationPanel.add(new JLabel("seconds"));

        JLabel inmateIDLabel = new JLabel("Inmate ID:");
        inmateIDLabel.setFont(labelFont);
        JLabel inmateNameLabel = new JLabel("Inmate Name:");
        inmateNameLabel.setFont(labelFont);
        JLabel durationLabel = new JLabel("Visit Duration:");
        durationLabel.setFont(labelFont);

        panel.add(createLabeledField(inmateIDLabel, inmateIDFieldInmateTab));
        panel.add(Box.createVerticalStrut(0));
        panel.add(createLabeledField(inmateNameLabel, inmateNameField));
        panel.add(createLabeledField(durationLabel, durationPanel));

        return panel;
    }

//    Creates a panel with a label and a corresponding input field (like a text field or spinner), 
//    arranging them horizontally with specified dimensions for alignment and appearance.
    private JPanel createLabeledField(JLabel label, JComponent field) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        label.setPreferredSize(new Dimension(100, 25));
        field.setPreferredSize(new Dimension(200, 25));
        fieldPanel.add(label);
        fieldPanel.add(field);
        return fieldPanel;
    }

    //purpose nito it goes by its method name
    private Font loadCustomFont() {
        try {
            File fontFile = new File("C:\\Users\\villa\\Downloads\\FONTS\\Over There.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return font.deriveFont(10f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 12);
        }
    }

    // Creates a panel for police officers to input their details and approval decision.
    private JPanel createPoliceTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        policeIDField = new JTextField(55);
        policeNameField = new JTextField(55);
        approveCheckBox = new JCheckBox("Approve");
        denyCheckBox = new JCheckBox("Deny");

        JLabel officerLabel = new JLabel("To be filled up by the officer in charge");
        officerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\villa\\Downloads\\FONTS\\Over There.ttf"));
            customFont = customFont.deriveFont(11f);
            officerLabel.setFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        officerLabel.setForeground(Color.BLUE);

        panel.add(Box.createVerticalStrut(22));
        panel.add(officerLabel);

        panel.add(createLabeledField("Police ID:", policeIDField));
        panel.add(createLabeledField("Police Name:", policeNameField));

        JLabel approvalStatusLabel = new JLabel("Approval Status:");
        approvalStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(approvalStatusLabel);

        JPanel approvalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        approvalPanel.add(approveCheckBox);
        approvalPanel.add(denyCheckBox);
        panel.add(approvalPanel);

        approveCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                denyCheckBox.setSelected(false);
            }
        });

        denyCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                approveCheckBox.setSelected(false);
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        panel.add(buttonPanel);

        return panel;
    }
    
    

    // Creates a labeled field with a label and a given component, organizing them in a horizontal layout.
    private JPanel createLabeledField(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(component);
        return panel;
    }
    
    // Validates the given police ID by checking it against the records in the PoliceOfficers.txt file.
    // Returns a message indicating whether the police information is valid and if the visit is authorized.
    private String validatePoliceID(String policeID) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\PoliceOfficers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String idFromFile = parts[0].trim();
                    if (idFromFile.equalsIgnoreCase(policeID.trim())) {
                        return "Police information is valid.<br>Visit authorized.";
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Invalid police Information. Visit Unauthorized.";
    }

    private void confirmVisit() { 
       LocalTime currentTime = LocalTime.now();
       VisitationHourChecker visitationHourChecker = new VisitationHourChecker();

       // Check if the current time is within the allowed visitation hours
       if (!visitationHourChecker.isVisitAllowed(currentTime)) {

           String message = "<html><div style='text-align: center;'>Visits are only allowed between 8:00 AM and 3:00 PM.<br>Visit Denied.</div></html>";

           JFrame confirmationFrame = new JFrame("Visit Status");
           confirmationFrame.setSize(400, 300);
           JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
           messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
           confirmationFrame.add(messageLabel, BorderLayout.CENTER);

           JPanel buttonPanel = new JPanel();
           JButton closeButton = new JButton("Close");
           closeButton.addActionListener(e -> confirmationFrame.dispose());
           buttonPanel.add(closeButton);

           confirmationFrame.add(buttonPanel, BorderLayout.SOUTH);
           confirmationFrame.setVisible(true);

           saveVisitRecord("Unauthorized");

           return; 
       }

    // If within the allowed time, proceed with normal visit confirmation
    String status = approveCheckBox.isSelected() ? "Approved" : "Unauthorized";
    String additionalMessage = approveCheckBox.isSelected() ? "Proceed to visiting hall" : "Please ask for assistance";

    String message = "<html><div style='text-align: center;'>Visit " + status + "<br>" + additionalMessage + "</div></html>";

    saveVisitRecord(status);

    JFrame confirmationFrame = new JFrame("Visit Status");
    confirmationFrame.setSize(700, 300);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    confirmationFrame.add(messageLabel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton makeAnotherVisitButton = new JButton("Visit form");
    makeAnotherVisitButton.addActionListener(e -> {
        confirmationFrame.dispose(); 
        new PrisonVisitingSystem().setVisible(true);  // Open the main visiting system form
    });
    buttonPanel.add(makeAnotherVisitButton);

    JButton viewVisitRecordButton = new JButton("View Visit Record");
    viewVisitRecordButton.addActionListener(e -> viewVisitRecord());
    buttonPanel.add(viewVisitRecordButton);

    confirmationFrame.add(buttonPanel, BorderLayout.SOUTH);
    confirmationFrame.setVisible(true);
}
 
 // The handleSubmit() method processes the user's input for inmate and police information, checks visit approval status, and displays an appropriate message based on the input.
    private void handleSubmit() {
        String inmateID = inmateIDFieldInmateTab.getText();
        String inmateName = inmateNameField.getText();
        String policeID = policeIDField.getText(); 

        if (denyCheckBox.isSelected()) {
            displayUnauthorizedMessage();
            return;
        }

        String inmateInfo = searchInmate(inmateID, inmateName);
        String visitApprovalMessage = validatePoliceID(policeID);

        JFrame resultFrame = new JFrame("Confirmation");
        resultFrame.setSize(500, 400);
        resultFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(154, 198, 239)); 

        JLabel messageLabel = new JLabel();
        try {
            // Load custom font
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, 
                              new File("C:\\Users\\villa\\Downloads\\FONTS\\LTAvocado-Regular.ttf"));
            customFont = customFont.deriveFont(20f); // Set font size
            messageLabel.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        }
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (inmateInfo != null) {
            if (!visitApprovalMessage.equals("Invalid police Information. Visit Unauthorized.")) {
                messageLabel.setText("<html><div style='text-align: center;'>Inmate found.<br>" +
                        visitApprovalMessage + "<br><br>" + inmateInfo + "</div></html>");

                JButton confirmButton = new JButton("Confirm");
                confirmButton.setFont(new Font("Arial", Font.PLAIN, 14));
                confirmButton.setBackground(new Color(2, 174, 60));
                confirmButton.setForeground(Color.WHITE);
                confirmButton.setPreferredSize(new Dimension(150, 40));
                confirmButton.addActionListener(e -> confirmVisit());

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(confirmButton);
                resultFrame.add(buttonPanel, BorderLayout.SOUTH);
            } else {
                messageLabel.setText("<html><div style='text-align: center;'>Inmate found, but " +
                        visitApprovalMessage + "</div></html>");

                JButton editButton = new JButton("Edit Response");
                editButton.setFont(new Font("Arial", Font.PLAIN, 14));
                editButton.setBackground(new Color(219, 208, 63));
                editButton.setForeground(Color.WHITE);
                editButton.setPreferredSize(new Dimension(120, 40));
                editButton.addActionListener(e -> {
                    editResponse();
                    resultFrame.dispose();
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(editButton);
                resultFrame.add(buttonPanel, BorderLayout.SOUTH);
            }
        } else {
            messageLabel.setText("<html><div style='text-align: center;'>Inmate not found.</div></html>");

            JButton editButton = new JButton("Edit Response");
            editButton.setFont(new Font("Arial", Font.PLAIN, 14));
            editButton.setForeground(Color.BLACK);
            editButton.setPreferredSize(new Dimension(150, 40));
            editButton.addActionListener(e -> {
                editResponse();
                resultFrame.dispose();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(editButton);
            resultFrame.add(buttonPanel, BorderLayout.SOUTH);
        }

        mainPanel.add(messageLabel, BorderLayout.CENTER);
        resultFrame.add(mainPanel, BorderLayout.CENTER);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    //Displays an unauthorized message and advises the user to seek assistance.
    private void displayUnauthorizedMessage() {
        JFrame unauthorizedFrame = new JFrame("Unauthorized Visit");
        unauthorizedFrame.setSize(400, 200);
        unauthorizedFrame.setLayout(new BorderLayout());

        JLabel unauthorizedLabel = new JLabel(
            "<html><div style='text-align: center; color: red;'>" +
            "Visit Denied. Please seek assistance from the authorities.</div></html>",
            SwingConstants.CENTER
        );
        unauthorizedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        unauthorizedFrame.add(unauthorizedLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> unauthorizedFrame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        unauthorizedFrame.add(buttonPanel, BorderLayout.SOUTH);

        unauthorizedFrame.setLocationRelativeTo(null);
        unauthorizedFrame.setVisible(true);
    }

    // Clears the input fields for inmate ID, name, and police ID, then focuses on the inmate ID field for further input.
    private void editResponse() {
        inmateIDFieldInmateTab.setText("");
        inmateNameField.setText("");

        inmateIDFieldInmateTab.requestFocus();
    }

    
    // Searches for an inmate in the "inmates.txt" file by comparing the inmate ID and name, returns inmate information if found.
    private String searchInmate(String inmateID, String inmateName) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\inmates.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith(inmateID)) {
                    String[] parts = line.split(",");
                    String firstName = parts[1].trim();
                    String middleInitial = parts[2].trim();
                    String lastName = parts[3].trim();

                    String fullName = firstName + " " + (middleInitial.isEmpty() ? "" : middleInitial + " ") + lastName;

                    if (fullName.trim().equalsIgnoreCase(inmateName.trim())) {
                        return getInmateInfo(inmateID);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }
    

    // Retrieves inmate details from the "inmates.txt" file by inmate ID, including age calculation based on DOB.
    private String getInmateInfo(String inmateID) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\inmates.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(inmateID)) {
                    String dob = details[4];
                    int age = calculateAge(dob);

                    // Build the formatted string with proper line separators.
                    StringBuilder inmateDetails = new StringBuilder();
                    inmateDetails.append("Inmate ID: ").append(details[0]).append(System.lineSeparator())
                                  .append("Name: ").append(details[1]).append(" ").append(details[2]).append(" ").append(details[3]).append(System.lineSeparator())
                                  .append("Age: ").append(age).append(System.lineSeparator())
                                  .append("Crime: ").append(details[6]).append(System.lineSeparator())
                                  .append("Cell Number: ").append(details[7]);

                    return inmateDetails.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Inmate details not found.";
    }


    // Calculates the age of an inmate based on their date of birth (DOB).
    private int calculateAge(String dob) {
        if (dob == null || dob.isEmpty()) {
            System.out.println("DOB is missing or invalid");
            return -1; // Returns -1 if the DOB is invalid or missing. error handlinhg
        }
        dob = dob.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthDate, currentDate);
            return period.getYears();
        } catch (Exception e) {
            System.out.println("Error in parsing date: " + dob);
            return -1;
        }
    }
    
    private void saveVisitRecord(String status) { 
        String visitDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedVisitDateTime = formatVisitDateTime(visitDateTime);

        String visitorName = visitorNameField.getText();
        String inmateName = inmateNameField.getText();
        String relationship = relationshipField.getText();

        // Generate Visit ID (dateofvivit and number of visit)
        String visitID = generateVisitID();

        // Check if the visit already exists in the file
        boolean isDuplicate = checkForDuplicateVisit(visitorName, inmateName, formattedVisitDateTime);
        if (isDuplicate) {
            JOptionPane.showMessageDialog(null, "This visit record already exists.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\visit_records.txt", true))) {
            // Save the record with Visit ID
            writer.write(visitID + ", " + visitorName + ", " + relationship + ", " + inmateName + ", " + formattedVisitDateTime + ", " + status);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Visit record saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate the Visit ID
    private String generateVisitID() {
        LocalDate now = LocalDate.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("MMddyyyy")); // MMDDYYYY format

        // Read the file to count visits on the same day
        int visitCount = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\visit_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(datePart)) {
                    visitCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Append the daily visit number (20 visits per day only)
        String visitNumber = String.format("%02d", visitCount); // Format as two digits
        return datePart + visitNumber; // Combine to form the Visit ID (yung sinasabi kanina)
    }


    // Helper method to check if the visit already exists (ginamit na method kanuna)
    private boolean checkForDuplicateVisit(String visitorName, String inmateName, String visitDateTime) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\visit_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(", ");
                if (record.length == 5) {
                    
                    // Check if visitor name, inmate name, and visit date/time match
                    if (record[0].equals(visitorName) && record[2].equals(inmateName) && record[3].equals(visitDateTime)) {
                        return true; // pag duplicate found
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // pag no duplicate found
    }


    // Displays the visit records in a table, allowing the user to view details of previous visits.
    private void viewVisitRecord() {

        JFrame recordFrame = new JFrame("Visit Records");
        recordFrame.setSize(1000, 700);  // Adjust frame size to a reasonable size

        // Add "Visit ID" as the first column
        String[] columnNames = {
            "Visit ID", "Visitor Name", "Relationship to Inmate", 
            "Inmate Name", "Visit Date and Time", "Visit Status"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        table.setFont(new Font("Arial", Font.PLAIN, 18));  
        table.setRowHeight(30);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\visit_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // Split the record to include Visit ID
                String[] record = line.split(", ");
                if (record.length == 6) {  // Update to 6 as Visit ID is now included

                    // Populate the table with Visit ID and other details
                    tableModel.addRow(new Object[]{
                        record[0], record[1], record[2], record[3], record[4], record[5]
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        recordFrame.add(scrollPane, BorderLayout.CENTER);

        table.setBackground(new Color(240, 240, 240)); 
        table.setGridColor(Color.BLACK); 
        table.setSelectionBackground(new Color(0, 120, 215));  
        table.setSelectionForeground(Color.WHITE);  

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> recordFrame.dispose());
        buttonPanel.add(closeButton);

        recordFrame.add(buttonPanel, BorderLayout.SOUTH);

        recordFrame.setVisible(true);
}

// Formats the visit date and time for display, changing the format to MM/dd/yy HH:mm.
    private String formatVisitDateTime(String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, formatter);
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            return parsedDateTime.format(displayFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return dateTime;
        }
    }

    // The main method to launch the PrisonVisitingSystem application.
    public static void main(String[] args) {
        new PrisonVisitingSystem();
    }
}
