package interfaces;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import databases.SavedWeatherDB;
import databases.WeatherDescDB;
import databases.ZipDB;
import databases.MyDBInit;
import org.json.JSONArray;
import org.json.JSONObject;
import references.CityForecast;
import references.CityStatus;
import references.CityWeather;
import references.WeatherInformation;

import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebService(endpointInterface = "interfaces.GetInformationByZIP")
public class GetInformationByZIPImpl implements GetInformationByZIP {




    static final String apikey = "3a67249483a0323809914f19cb3cf4f9";


    public CityForecast GetCityForecastByZIP(String ZIP) {



        ZipDB zipDB = new ZipDB();
        CityForecast cityForecast = new CityForecast();
        ResultSet rs;
        CityStatus cityStatus = new CityStatus();
        try {
//            Возвращает ResultSet базы данных zipcode
            rs = zipDB.getRS();


            while (rs.next()) {

                if (rs.getString("zip").equals(ZIP)) {


                    cityStatus.setSuccess("true");
                    cityStatus.setResponseText("City is found");


                    cityForecast.setStatus(cityStatus);
                    cityForecast.setCity(rs.getString("city"));
//                    cityForecast.setResponseText("City is found");
                    cityForecast.setState(rs.getString("state"));
                    cityForecast.setWeatherStationCity(rs.getString("weatherStation"));
                    Date date = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    cityForecast.setDate(formatForDateNow.format(date));
                    return cityForecast;

                } else {
                    cityStatus.setSuccess("false");
                    cityStatus.setResponseText("City is not found");

                    cityForecast.setStatus(cityStatus);
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MyDBInit.getInstance().closeConnection();
        }
        return cityForecast;
    }


    @Override
    public CityWeather GetCityWeatherByZIP(String ZIP) {

        ZipDB zipDB = new ZipDB();
        WeatherDescDB weatherDescDB = new WeatherDescDB();

        CityWeather cityWeather = new CityWeather();
        CityStatus cityStatus = new CityStatus();
        ResultSet rs;

        try {
            rs = zipDB.getRS();


            while (rs.next()) {

                if (rs.getString("zip").equals(ZIP)) {
                    cityStatus.setSuccess("true");
                    cityStatus.setResponseText("City is found");

                    cityWeather.setStatus(cityStatus);
                    cityWeather.setCity(rs.getString("city"));

                    cityWeather.setState(rs.getString("state"));
                    cityWeather.setWeatherStationCity(rs.getString("weatherStation"));


                    StringBuilder result = new StringBuilder();
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityWeather.getCity() + "&APPID=" + apikey + "&units=metric");
                    URLConnection conn = url.openConnection();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();
//                    System.out.println(result);
                    Map<String, Object> respMap = jsonToMap(result.toString());
                    Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
                    Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());


                    cityWeather.setWeatherID(getWeatherDescFromJson(result.toString()));

                    WeatherInformation weatherInformation = GetWeatherInformation(cityWeather.getWeatherID());
                    cityWeather.setDescription(weatherInformation.getDescription());
                    cityWeather.setTemperature(mainMap.get("temp").toString());
                    cityWeather.setPressure(mainMap.get("pressure").toString());
                    cityWeather.setRelativeHumidity(mainMap.get("humidity").toString());


                    cityWeather.setWindSpeed(windMap.get("speed").toString());

                    Date date = new Date();
                    cityWeather.setDateCity(date);


                    return cityWeather;

                } else {

                    cityStatus.setSuccess("false");
                    cityStatus.setResponseText("City is not found");


                    cityWeather.setStatus(cityStatus);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDBInit.getInstance().closeConnection();
        }


        return cityWeather;
    }


    @Override
    public WeatherInformation GetWeatherInformation(int ID) {
        WeatherDescDB weatherDescDB = new WeatherDescDB();
        WeatherInformation weatherInformation = new WeatherInformation();

        weatherInformation.setWeatherID(ID);


        try {
            weatherInformation.setDescription(weatherDescDB.getWeatherDesc(ID));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return weatherInformation;
    }

    @Override
    public int getWeatherDescFromJson(String weatherJson) throws Exception {
        Pattern pattern = Pattern.compile("id.{5}");
        Matcher matcher = pattern.matcher(weatherJson);
        if (matcher.find()) return Integer.parseInt(matcher.group().substring(4));

        throw new Exception("Can't extract weatherDesc from Json");

    }

    @Override
    public CityStatus SaveCityWeatherByZIPandDate(String ZIP, String date) {


        SavedWeatherDB savedWeatherDB = new SavedWeatherDB();
        CityStatus cityStatus = new CityStatus();


        GetInformationByZIPImpl getInformationByZIP = new GetInformationByZIPImpl();
        CityForecast myweather = getInformationByZIP.GetCityForecastByZIP(ZIP);

        if (myweather.getStatus().getSuccess().equals("true")) {

            Map<String, Object> mapOfForecastFromJson = GetForecastFromJson(myweather.getCity());


            CityWeather cityWeather = (CityWeather) mapOfForecastFromJson.get(date);
            cityWeather.setCity(myweather.getCity());
            cityWeather.setStatus(myweather.getStatus());
            cityWeather.setDateCity(cityWeather.getDateCity());


            cityWeather.setState(myweather.getState());
            cityWeather.setWeatherStationCity(myweather.getWeatherStationCity());


            savedWeatherDB.SaveWeather(cityWeather, ZIP);
            MyDBInit.getInstance().closeConnection();


            cityStatus.setSuccess("true");
            cityStatus.setResponseText("City is saved");

        } else {
            cityStatus.setSuccess("false");
            cityStatus.setResponseText("City is not found");
        }

        return cityStatus;


    }


    private static Map<String, Object> GetForecastFromJson(String city) {

        StringBuilder result = new StringBuilder();
        Map<String, Object> weatherInfoMap = new TreeMap<>();


        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID=" + apikey + "&units=metric");
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
//            System.out.println(result);

            String jsonArr = "[" + result.toString() + "]";


            JSONArray jsonarray = new JSONArray(jsonArr);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject list = jsonarray.getJSONObject(i);

                JSONArray listArray = list.getJSONArray("list");

                for (int j = 0; j < listArray.length(); j++) {
                    CityWeather weatherInfo = new CityWeather();

                    JSONObject listObject = listArray.getJSONObject(j);

                    String date = listObject.getString("dt_txt");

                    weatherInfo.setDateCity(new SimpleDateFormat("yyyy-MM-dd").parse(date));
//
//                    System.out.println(date);


                    JSONObject mainObj = listObject.getJSONObject("main");

//                    System.out.println(temp);
                    weatherInfo.setPressure(Integer.toString(mainObj.getInt("pressure")));
                    weatherInfo.setRelativeHumidity(Integer.toString(mainObj.getInt("humidity")));
                    weatherInfo.setTemperature(Integer.toString(mainObj.getInt("temp")));


                    JSONArray weatherArray = listObject.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);


                    weatherInfo.setWeatherID(weatherObj.getInt("id"));
                    weatherInfo.setDescription(weatherObj.getString("description"));


                    JSONObject windObj = listObject.getJSONObject("wind");


                    int windDirection = ((windObj.getInt("deg")));

                    int windDirectionID = 1;

                    if (windDirection >= 0 & windDirection < 45) {
                        windDirectionID = 1;
                    } else if (windDirection >= 45 & windDirection < 90) {
                        windDirectionID = 4;
                    } else if (windDirection >= 90 & windDirection < 135) {
                        windDirectionID = 4;
                    } else if (windDirection >= 135 & windDirection < 180) {
                        windDirectionID = 2;
                    } else if (windDirection >= 180 & windDirection < 225) {
                        windDirectionID = 2;
                    } else if (windDirection >= 225 & windDirection < 270) {
                        windDirectionID = 3;
                    } else if (windDirection >= 270 & windDirection < 315) {
                        windDirectionID = 3;
                    } else if (windDirection >= 315 & windDirection <= 360) {
                        windDirectionID = 1;
                    }
                    weatherInfo.setWindDirection(windDirectionID);

//                    weatherInfo.setWindDirection(((windObj.getInt("deg"))));


                    weatherInfo.setWindSpeed(Integer.toString(windObj.getInt("speed")));
                    weatherInfoMap.put(date, weatherInfo);

                }
//                System.out.println(listArray);


            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }



        return weatherInfoMap;

    }

    private static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        return map;
    }


}
