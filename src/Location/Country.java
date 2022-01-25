package Location;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Chris Sequeira
 *
 * The country class is for countries from the database.
 * They take an Integer for countryID and String for country (name).
 */
public class Country {
    public IntegerProperty countryID;
    public String country;

    public Country() {
        this.countryID = new SimpleIntegerProperty();
        this.country = new String();
    }

    /**
     * this method overrides the toString() method for when Country objects are populated into ComboBoxes
     */
    @Override
    public String toString(){ return (country); }

    //setters

    public void setCountryID(int countryID) {
        this.countryID.set(countryID);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //getters

    public int getCountryID() {
        return countryID.get();
    }

    public String getCountry() {
        return country;
    }
}
