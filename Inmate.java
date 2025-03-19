package it203finalproject;

// to get inmate info
public class Inmate extends User {
    private String birthDate;
    private String sex;
    private String crime;
    private String cellNumber;

    //initialize inmate info + yung common infos ininherit from
    public Inmate(String userID, String firstName, String middleInitial, String lastName, 
                  String birthDate, String sex, String crime, String cellNumber) {
        super(userID, firstName, middleInitial, lastName);
        this.birthDate = birthDate;
        this.sex = sex;
        this.crime = crime;
        this.cellNumber = cellNumber;
    }

    //getter and setter method for infos
    public String getBirthDate() { return birthDate; }
    public String getSex() { return sex; }
    public String getCrime() { return crime; }
    public String getCellNumber() { return cellNumber; }
    
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setSex(String sex) { this.sex = sex; }
    public void setCrime(String crime) { this.crime = crime; }
    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }

    //override ng displayDetails method tas inaddd yung attributes nitong si inmate
    @Override
    public String displayDetails() {
        return super.displayDetails() + "\n" +
               "Birth Date: " + birthDate + "\n" +
               "Sex: " + sex + "\n" +
               "Crime: " + crime + "\n" +
               "Cell Number: " + cellNumber;
    }
}

