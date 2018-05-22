package databases;


import references.CityWeather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class WeatherMap {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WeatherMap.class);

    public Map<Integer, CityWeather> getWeatherMap() {

        Map<Integer, CityWeather> cityWeatherMap = new TreeMap<>();


        Connection conn = MyDBInit.getInstance().getConnection();
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("SELECT * FROM weather_info ORDER BY id");
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                CityWeather cityWeather = new CityWeather();
                cityWeather.setCity(rs.getString("city"));
                cityWeather.setWeatherID(rs.getInt("weatherid"));
                cityWeather.setWindSpeed(rs.getString("wind"));
                cityWeather.setTemperature(rs.getString("temp"));
                cityWeather.setDateCity(rs.getDate("date"));
                cityWeather.setDescription(rs.getString("description"));
                cityWeather.setRelativeHumidity(rs.getString("relativehumid"));
                cityWeather.setState(rs.getString("state"));
                cityWeather.setWindDirection(rs.getInt("id_dir"));

                int id = rs.getInt("id");


                cityWeatherMap.put(id, cityWeather);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            log.warn("SQL Exception in getWeatherMap");
        }


        return cityWeatherMap;
    }


    public List<CityWeather> getWeatherListByDate(String dateFrom, String dateTo) {

        List<CityWeather> list = new ArrayList<>();

        try (Connection conn = MyDBInit.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, city, weatherid, zip, state, description, relativehumid, success, response_text, temp, wind, date,id_dir FROM public.weather_info where (date>?)and(date<?) ORDER BY id;")) {


            stmt.setDate(1, java.sql.Date.valueOf(dateFrom));
            stmt.setDate(2, java.sql.Date.valueOf(dateTo));


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                CityWeather cityWeather = new CityWeather();
                cityWeather.setCity(rs.getString("city"));
                cityWeather.setWeatherID(rs.getInt("weatherid"));
                cityWeather.setWindSpeed(rs.getString("wind"));
                cityWeather.setTemperature(rs.getString("temp"));
                cityWeather.setDateCity(rs.getDate("date"));
                cityWeather.setDescription(rs.getString("description"));
                cityWeather.setRelativeHumidity(rs.getString("relativehumid"));
                cityWeather.setState(rs.getString("state"));
                cityWeather.setId(rs.getInt("id"));
                cityWeather.setWindDirection(rs.getInt("id_dir"));
                list.add(cityWeather);


            }


        } catch (SQLException e) {

            log.warn("SQL Exception in getWeatherListByDate");
        }

        return list;


    }
}
