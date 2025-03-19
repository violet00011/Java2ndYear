package it203finalproject;

// User class representing common information for all types of users. Parent Class
public class User {
    private String userID;
    private String firstName;
    private String middleInitial;
    private String lastName;

    // Constructor to initialize common user information (ID, name).
    public User(String userID, String firstName, String middleInitial, String lastName) {
        this.userID = userID;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
    }

    // Getters and setters for user information.
    public String getUserID() { return userID; }
    public String getFirstName() { return firstName; }
    public String getMiddleInitial() { return middleInitial; }
    public String getLastName() { return lastName; }
    
    public void setUserID(String userID) { this.userID = userID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleInitial(String middleInitial) { this.middleInitial = middleInitial; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // Polymorphism method to display details.
    public String displayDetails() {
        return "User ID: " + userID + "\n" +
               "First Name: " + firstName + "\n" +
               "Middle Initial: " + (middleInitial.isEmpty() ? "N/A" : middleInitial) + "\n" +
               "Last Name: " + lastName;
    }
}

