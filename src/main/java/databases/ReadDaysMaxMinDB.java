package databases;


import references.Days;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ReadDaysMaxMinDB {
    enum Direction {Северный, Южный, Западный, Восточный}

    ;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReadDaysMaxMinDB.class);

    public List<Days> getStatistic() {


        List<Days> daysList = new ArrayList<>();

        try (Connection conn = MyDBInit.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT date,max(temp),min(temp),round(avg(id_dir)) FROM weather.public.weather_info GROUP BY date ORDER BY date;")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Days day = new Days();

                day.setDate(rs.getDate("date"));
                day.setMaxtemp(rs.getString("max"));
                day.setMintemp(rs.getString("min"));
                String directionID = rs.getString("round");

                switch (directionID) {
                    case "1":
                        day.setWind_average(String.valueOf(Direction.Северный));
                        break;
                    case "2":
                        day.setWind_average(String.valueOf(Direction.Южный));
                        break;
                    case "3":
                        day.setWind_average(String.valueOf(Direction.Западный));
                        break;
                    case "4":
                        day.setWind_average(String.valueOf(Direction.Восточный));
                        break;

                }
                daysList.add(day);
            }

        } catch (SQLException e) {

            log.warn("SQL exception (getStatistic method)");
        }

        return daysList;

    }


}
