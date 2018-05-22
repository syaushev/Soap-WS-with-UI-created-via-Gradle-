package tests;


import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class SuccessPage extends WebPage {


    public SuccessPage(final PageParameters parameters) {


        final Label result = new Label("result", "Была получена погода с: " + parameters.get("datefrom")
                + " по " + parameters.get("dateto"));
        add(result);


    }

}
