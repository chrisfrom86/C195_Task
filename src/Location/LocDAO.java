package Location;

import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Chris Sequeira
 *
 * The LocDAO class is the data access object for the locations table in the database.
 */
public class LocDAO {

    /**
     * getAllCountries() queries the database for all entries in the countries table.
     * @return returns an observable list of all countries with all info from the database.
     * @throws Exception
     */
    public static ObservableList<Country> getAllCountries() throws Exception {
        ObservableList<Country> locList = FXCollections.observableArrayList();
        String sql = "select * from countries";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("LocDAO - " + ps + " command issued to db");
            while (rs.next()) {
                Country loc = new Country();
                loc.setCountryID(rs.getInt("country_id"));
                loc.setCountry(rs.getString(2));
                locList.add(loc);
                //System.out.println(rs.getInt(1) + " added to " + locList);
            }
            System.out.println("LocDAO - Location objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("LocDAO - Error querying all Locations");
            e.printStackTrace();
        }

        return locList;
    }

    /**
     * getAllDivisions() queries the database for all entries in the first_level_divisions table.
     * @return returns an observable list of all first level divisions (state, province, etc) with all info from the database.
     * @throws Exception
     */
    public static ObservableList<CountryDivision> getAllDivisions() throws Exception {
        String sql = "select * from first_level_divisions";
        ObservableList<CountryDivision> locList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("LocDAO - " + ps + " command issued to db");
            while (rs.next()) {
                CountryDivision loc = new CountryDivision();
                loc.setDivisionID(rs.getInt("division_id"));
                loc.setDivision(rs.getString(2));
                locList.add(loc);
                //System.out.println(rs.getInt(1) + " added to " + locList);
            }
            System.out.println("LocDAO - Location objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("LocDAO - Error querying all Locations");
            e.printStackTrace();
        }
        return locList;
    }

    /**
     * this method gets the country of a given division using divisionID {@param id}.
     * @return
     * @throws SQLException
     */
    public static String getCountryOfDivisionByID(int id) throws SQLException {
        String sql = "select * from countries where countries.country_id = ?";
        //ObservableList<Customer> custList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        //System.out.println("result set = " + rs);
        String result = null;
        
        while (rs.next()) {
            result = rs.getString("country");
        }
        return result;
    }

    /**
     * this method gets the first level division of a given division using divisionName {@param name}.
     *
     * This is used for reading the selection from ComboBoxes in the add and modify appointment forms.
     * @return
     * @throws SQLException
     */
    public static CountryDivision getDivisionByName(String name) throws SQLException {
        String sql = "select * from first_level_divisions where division = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        CountryDivision loc = new CountryDivision();

        while (rs.next()) {
            loc.setDivision(rs.getString("division"));
            loc.setDivisionID(rs.getInt("division_id"));
            loc.setCountryID(rs.getInt("country_id"));
        }

        return loc;
    }

    /**
     * this method gets all info about a division based on its ID {@param id}.
     *
     * this is used for populating a CountryDivision object when pulling customer information from the database.
     *
     * @return
     * @throws SQLException
     */
    public static CountryDivision getDivisionByID(int id) throws SQLException {
        String sql = "select * from first_level_divisions where division_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        CountryDivision loc = new CountryDivision();

        while (rs.next()) {
            loc.setDivision(rs.getString("division"));
            loc.setDivisionID(rs.getInt("division_id"));
            loc.setCountryID(rs.getInt("country_id"));
        }

        return loc;
    }

    /**
     * this method is to get a country by its ID {@param id}.
     *
     * this is used for creating Country objects for appointments when pulling appointments from the appointments table.
     *
     * @return
     * @throws Exception
     */
    public static Country getCountryByID(int id) throws Exception {
        String sql = "select country_id, country from countries where country_id = ?";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Country loc = new Country();

        while (rs.next()) {
            loc.setCountryID(rs.getInt("country_id"));
            loc.setCountry(rs.getString("country"));
        }

        return loc;
    }

    /**
     * This method is used to get all of a country's divisions by the country's ID {@param id}.
     *
     * This is used for populating Location ComboBoxes when a country ComboBox is selected.
     * @return
     * @throws Exception
     */
    public static ObservableList<CountryDivision> getAllDivisionsByCountryID(int id) throws Exception {
        String sql = "select country_id, division_id, division from first_level_divisions where country_id = ?";
        ObservableList<CountryDivision> locList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("LocDAO - " + ps + " command issued to db");
            while (rs.next()) {
                CountryDivision loc = new CountryDivision();
                loc.setDivisionID(rs.getInt("division_id"));
                loc.setDivision(rs.getString("division"));
                locList.add(loc);
                //System.out.println(rs.getInt("division_id") + " added to " + locList);
            }
            System.out.println("LocDAO - Country Division objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("LocDAO - Error querying country division Locations");
            e.printStackTrace();
        }
        return locList;
    }
}
