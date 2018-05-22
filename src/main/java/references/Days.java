package references;

import java.io.Serializable;
import java.util.Date;

public class Days implements Serializable {
    private Date date;
    private String maxtemp;
    private String mintemp;
    private String wind_average;

    @Override
    public String toString() {
        return "Days{" +
                "date=" + date +
                ", maxtemp='" + maxtemp + '\'' +
                ", mintemp='" + mintemp + '\'' +
                ", wind_average='" + wind_average + '\'' +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(String maxtemp) {
        this.maxtemp = maxtemp;
    }

    public String getMintemp() {
        return mintemp;
    }

    public void setMintemp(String mintemp) {
        this.mintemp = mintemp;
    }

    public String getWind_average() {
        return wind_average;
    }

    public void setWind_average(String wind_average) {
        this.wind_average = wind_average;
    }
}
