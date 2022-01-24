package Location;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Country {
    public IntegerProperty countryID;
    public String country;

    public Country() {
        this.countryID = new SimpleIntegerProperty();
        this.country = new String();
    }

    @Override
    public String toString(){ return (country); }

    public void setCountryID(int countryID) {
        this.countryID.set(countryID);
    }

    public void setCountry(String country) {
        this.country = country;
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
}
