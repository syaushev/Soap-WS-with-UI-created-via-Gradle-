package jasper;

import databases.ReadDaysMaxMinDB;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import references.CityWeather;
import references.Days;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ExportPage extends WebPage {


    private Types typeParameter;
    private Format formatParameter;

    private String nameOfReport;
    private JRBeanCollectionDataSource jrBeanCollectionDataSource;


    public ExportPage(PageParameters pageParameters, List<CityWeather> list) {


        typeParameter = Types.valueOf(String.valueOf(pageParameters.get("num")));
        formatParameter = Format.valueOf(String.valueOf(pageParameters.get("type")));


        WebResponse resp = (WebResponse) getResponse();
        Map<String, Object> map = new HashMap<>();
        switch (typeParameter) {
            case First:
                nameOfReport = "report1.jrxml";
                List<Days> daysList = new ReadDaysMaxMinDB().getStatistic();
                jrBeanCollectionDataSource = new JRBeanCollectionDataSource(daysList);

                break;
            case Second:
                nameOfReport = "report2.jrxml";
                List<CityWeather> cityWeatherList = list;

                map.put("date1", String.valueOf(pageParameters.get("datefrom")));
                map.put("date2", String.valueOf(pageParameters.get("dateto")));
                jrBeanCollectionDataSource = new JRBeanCollectionDataSource(cityWeatherList);
                break;
        }

        map.put("ItemDS", jrBeanCollectionDataSource);

        new ExpClass().ExportJasper(formatParameter, resp, map, jrBeanCollectionDataSource, nameOfReport);

    }


}




