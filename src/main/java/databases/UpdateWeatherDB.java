package databases;

import org.apache.wicket.markup.repeater.Item;
import references.CityWeather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import java.util.Set;

public class UpdateWeatherDB {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateWeatherDB.class);

    public void UpdateWeather(Set<Item<CityWeather>> itemlist) {


        Set<Item<CityWeather>> list = itemlist;
        Iterator<Item<CityWeather>> iterator = list.iterator();


        while (iterator.hasNext()) {
            CityWeather cityWeather;

            cityWeather = (CityWeather) iterator.next().getDefaultModelObject();

            try (Connection conn = MyDBInit.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE weather.public.weather_info SET city=?, weatherid=?, state=?, description=?, relativehumid=?, temp=?,wind=?  WHERE id=?;")) {
                stmt.setString(1, cityWeather.getCity());
                stmt.setInt(2, cityWeather.getWeatherID());
                stmt.setString(3, cityWeather.getState());
                stmt.setString(4, cityWeather.getDescription());
                stmt.setString(5, cityWeather.getRelativeHumidity());
                stmt.setString(6, cityWeather.getTemperature());
                stmt.setString(7, cityWeather.getWindSpeed());
                stmt.setInt(8, cityWeather.getId());

                stmt.executeUpdate();

            } catch (SQLException e) {

                log.warn("SQL Exception in UpdateWeather");
            }


        }
        log.info("DB's been updated");

    }
}
