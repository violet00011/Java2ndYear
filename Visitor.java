package it203finalproject;

// to get visitor info
public class Visitor extends User {
    private String relationToInmate;

    //inherits from usser class
    public Visitor(String userID, String firstName, String middleInitial, String lastName, String relationToInmate) {
        super(userID, firstName, middleInitial, lastName);
        this.relationToInmate = relationToInmate;
    }

    //getter and setter method for infos
    public String getRelationToInmate() { return relationToInmate; }
    public void setRelationToInmate(String relationToInmate) { this.relationToInmate = relationToInmate; }

    //override ng displayDetails method tas inaddd yung attributes ni Visitor
    @Override
    public String displayDetails() {
        return super.displayDetails() + "\n" +
               "Relation to Inmate: " + relationToInmate;
    }
}
