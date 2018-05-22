package databases;

import references.CityStatus;
import references.CityWeather;

import java.sql.*;

public class SavedWeatherDB {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavedWeatherDB.class);

    public SavedWeatherDB() {
    }


    public CityStatus SaveWeather(CityWeather cityWeather, String ZIP) {

        CityStatus cityStatus = new CityStatus();


        try (Connection conn = MyDBInit.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO weather_info (city, weatherid, zip, state, description, relativehumid, success, response_text, temp,  wind,date,id_dir) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)")) {

            stmt.setString(1, cityWeather.getCity());
            stmt.setInt(2, cityWeather.getWeatherID());
            stmt.setString(3, ZIP);
            stmt.setString(4, cityWeather.getState());
            stmt.setString(5, cityWeather.getDescription());
            stmt.setString(6, cityWeather.getRelativeHumidity());
            stmt.setString(7, cityWeather.getStatus().getSuccess());
            stmt.setString(8, cityWeather.getStatus().getResponseText());
            stmt.setString(9, cityWeather.getTemperature());

            stmt.setString(10, cityWeather.getWindSpeed());
            stmt.setDate(11, new Date(cityWeather.getDateCity().getTime()));
            stmt.setInt(12, cityWeather.getWindDirection());


            stmt.executeUpdate();


        } catch (SQLException e) {

            cityStatus.setSuccess("false");
            cityStatus.setResponseText("Smth went wrong");
            log.warn("SQL Exception in SaveWeather method");
        }
        log.info("Weather Forecast has been saved to DB");
        return cityStatus;
    }


}
