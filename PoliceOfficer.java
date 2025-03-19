package it203finalproject;

//to get police info
public class PoliceOfficer extends User {
    private String policeRank;

    //inherits from usser class
    public PoliceOfficer(String userID, String firstName, String middleInitial, String lastName, String policeRank) {
        super(userID, firstName, middleInitial, lastName);
        this.policeRank = policeRank;
    }

    //getter and setter method for infos
    public String getPoliceRank() { return policeRank; }
    public void setPoliceRank(String policeRank) { this.policeRank = policeRank; }

    //override ng displayDetails method tas inaddd yung attributes ni police officer
    @Override
    public String displayDetails() {
        return super.displayDetails() + "\n" +
               "Rank: " + policeRank;
    }
}
