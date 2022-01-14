package Location;

import Main.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocDAO {
    public static ObservableList<Location> getAllCountries() throws Exception {
        ObservableList<Location> locList = FXCollections.observableArrayList();
        String sql = "select * from countries";
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("Select * from countries command issued to db");
            while (rs.next()) {
                Location loc = new Location();
                loc.setCountryID(rs.getInt("country_id"));
                loc.setCountry(rs.getString(2));
                locList.add(loc);
                //System.out.println(rs.getInt(1) + " added to " + locList);
            }
            System.out.println("Location objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("Error querying all Locations");
            e.printStackTrace();
        }

        return locList;
    }

    public static ObservableList<Location> getAllDivisions() throws Exception {
        String sql = "select * from first_level_divisions";
        ObservableList<Location> locList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("Select * from first_level_divisions command issued to db");
            while (rs.next()) {
                Location loc = new Location();
                loc.setDivisionID(rs.getInt("division_id"));
                loc.setDivision(rs.getString(2));
                locList.add(loc);
                //System.out.println(rs.getInt(1) + " added to " + locList);
            }
            System.out.println("Location objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("Error querying all Locations");
            e.printStackTrace();
        }
        return locList;
    }

    public static int getCountryID(String string) throws Exception {
        String sql = "select country_id, country from countries where country = ?";
        ObservableList<Location> locList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setString(1, string);
        ResultSet rs = ps.executeQuery();

        return rs.getInt("country_id");
    }

    public static ObservableList<Location> getCountryDivisions(int id) throws Exception {
        String sql = "select country_id, division_id, division from first_level_divisions where country_id = ?";
        ObservableList<Location> locList = FXCollections.observableArrayList();
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        try {
            System.out.println("'select first_level_divisions.country_id, division_id, division from countries, first_level_divisions where countries.country_id = first_level_divisions.country_id' command issued to db");
            while (rs.next()) {
                Location loc = new Location();
                loc.setDivisionID(rs.getInt("division_id"));
                loc.setDivision(rs.getString("division"));
                locList.add(loc);
                //System.out.println(rs.getInt("division_id") + " added to " + locList);
            }
            System.out.println("Country Division objects added to ObservableList<Location>");
        } catch (SQLException e) {
            System.out.println("Error querying country division Locations");
            e.printStackTrace();
        }
        return locList;
    }
}
