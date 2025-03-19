package it203finalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PoliceOfficerValidator {

    public static String validatePolice(String policeID) {
        String filePath = "C:\\Users\\villa\\OneDrive\\Documents\\NetBeansProjects\\IT203FinalProject\\src\\it203finalproject\\PoliceOfficers.txt";
        String line;
        String policeName = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equalsIgnoreCase(policeID)) {
                    // Extract name and rank
                    policeName = details[1] + " " + details[2] + ". " + details[3];  // First, Middle Initial, Last Name
                    return "Visit approved by: " + policeName + " " + details[4];  // Police Rank
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading police file: " + e.getMessage());
        }
        return "Invalid police ID.";
    }
}
