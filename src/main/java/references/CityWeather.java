package references;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;


public class CityWeather implements Serializable {
    private CityStatus status;
    private String state;
    private String city;
    private String weatherStationCity;
    private int weatherID;
    private String description;
    private String temperature;
    private String relativeHumidity;
    private int windDirection;
    private String windSpeed;
    private String pressure;
    private Date dateCity;
    private int id;

    @Override
    public String toString() {
        return "CityWeather{" +
                "status=" + status +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", weatherStationCity='" + weatherStationCity + '\'' +
                ", weatherID=" + weatherID +
                ", description='" + description + '\'' +
                ", temperature='" + temperature + '\'' +
                ", relativeHumidity='" + relativeHumidity + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", pressure='" + pressure + '\'' +
                ", dateCity=" + dateCity +
                ", id=" + id +
                '}';
    }

    public CityStatus getStatus() {
        return status;
    }

    public void setStatus(CityStatus status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherStationCity() {
        return weatherStationCity;
    }

    public void setWeatherStationCity(String weatherStationCity) {
        this.weatherStationCity = weatherStationCity;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(int weatherID) {
        this.weatherID = weatherID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {

       this.windDirection=windDirection;

    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {


        this.windSpeed = windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Date getDateCity() {
        return dateCity;
    }

    public void setDateCity(Date dateCity) {
        this.dateCity = dateCity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
