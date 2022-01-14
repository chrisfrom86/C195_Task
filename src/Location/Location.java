package Location;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Location {
    public IntegerProperty countryID;
    public String country;
    public IntegerProperty divisionID;
    public String division;

    public Location() {
        this.countryID = new SimpleIntegerProperty();
        this.country = new String();
        this.divisionID = new SimpleIntegerProperty();
        this.division = new String();
    }

    @Override
    public String toString(){
        return (country + division);
    }

    public void setCountryID(int countryID) {
        this.countryID.set(countryID);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID.set(divisionID);
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID.get();
    }

    public IntegerProperty countryIDProperty() {
        return countryID;
    }

    public String getCountry() {
        return country;
    }

    public int getDivisionID() {
        return divisionID.get();
    }

    public IntegerProperty divisionIDProperty() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }
}
