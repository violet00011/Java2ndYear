
package it203finalproject;
// VisitRecord class stores visit information such as the visitor, the date, and status.
public class VisitRecord {
    private String visitorName;
    private String relationshipToInmate;
    private String inmateName;
    private String visitDateTime;
    private String status; // Approved or Denied
    
    // Constructor to initialize visit details.
    public VisitRecord(String visitorName, String relationshipToInmate, String inmateName, 
                       String visitDateTime, String status) {
        this.visitorName = visitorName;
        this.relationshipToInmate = relationshipToInmate;
        this.inmateName = inmateName;
        this.visitDateTime = visitDateTime;
        this.status = status;
    }

    // Getters and setters for visit record details.
    public String getVisitorName() { return visitorName; }
    public String getRelationshipToInmate() { return relationshipToInmate; }
    public String getInmateName() { return inmateName; }
    public String getVisitDateTime() { return visitDateTime; }
    public String getStatus() { return status; }

    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public void setRelationshipToInmate(String relationshipToInmate) { this.relationshipToInmate = relationshipToInmate; }
    public void setInmateName(String inmateName) { this.inmateName = inmateName; }
    public void setVisitDateTime(String visitDateTime) { this.visitDateTime = visitDateTime; }
    public void setStatus(String status) { this.status = status; }
}
