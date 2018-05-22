package tests;


import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.postgresql.ds.PGPoolingDataSource;
import databases.MyDBInit;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.Properties;

public class HelloWorldApplication extends WebApplication {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HelloWorldApplication.class);

    @Override
    protected void init() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));

            String dbUsername = properties.getProperty("username");
            String dbPassword = properties.getProperty("password");
            String serverName = properties.getProperty("serverName");
            String databaseName = properties.getProperty("databaseName");
            String dataSourceName = properties.getProperty("dataSourceName");
            log.info("Properties have been read");


            PGPoolingDataSource source = new PGPoolingDataSource();
            source.setDataSourceName(dataSourceName);
            source.setServerName(serverName);
            source.setDatabaseName(databaseName);
            source.setUser(dbUsername);
            source.setPassword(dbPassword);

            new MyDBInit(source);
        } catch (IOException e) {
            log.warn("Smth goes wrong in init method");
        }
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return WeatherPage.class;
    }


}


