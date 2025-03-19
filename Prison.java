
package it203finalproject;

public class Prison {
    private String prisonID;
    private String name;
    private String location;

    public Prison(String prisonID, String name, String location) {
        this.prisonID = prisonID;
        this.name = name;
        this.location = location;
    }

    public String getPrisonID() { return prisonID; }
    public String getName() { return name; }
    public String getLocation() { return location; }
}
