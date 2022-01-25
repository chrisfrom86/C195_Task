package Location;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Chris Sequeira
 *
 * The CountryDivision class is for first level divisions (state, province, etc) from the database.
 * They take an Integer for countryID (foreign key to country table), Integer for divisionID, and String for division (name).
 */
public class CountryDivision {
    public IntegerProperty divisionID;
    public String division;
    public int countryID;

    public CountryDivision() {
        this.divisionID = new SimpleIntegerProperty();
        this.division = new String();
    }

    /**
     * this method overrides the toString() method for when CountryDivision objects are populated into ComboBoxes.
     */
    @Override
    public String toString(){ return (division); }

    //setters

    public void setDivisionID(int divisionID) {
        this.divisionID.set(divisionID);
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    //getters

    public int getDivisionID() {
        return divisionID.get();
    }

    public IntegerProperty divisionIDProperty() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryID() {
        return countryID;
    }
}
