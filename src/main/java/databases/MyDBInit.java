package databases;


import org.postgresql.ds.PGPoolingDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class MyDBInit {
    private static Connection conn;
    private static InitialContext ctx;
    private static PGPoolingDataSource source;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MyDBInit.class);

    public MyDBInit(PGPoolingDataSource source) {
        this.source = source;
    }

    private MyDBInit() {

        try {
            new InitialContext().rebind("DataSource", source);
        } catch (NamingException e) {
            log.warn("Naming exception during creating new Initial Context ");

        }
    }

    private static MyDBInit instance;

    public static MyDBInit getInstance() {
        if (instance == null) {
            instance = new MyDBInit();
        }

        return instance;
    }

    public Connection getConnection() {
        try {

            DataSource dataSource = (DataSource) new InitialContext().lookup("DataSource");
            return dataSource.getConnection();
        } catch (NamingException | SQLException e) {

            log.warn("SQL exception during getting connection");
        }
        return null;

    }


    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();

            } catch (SQLException ex) {
                log.warn("SQL exception in closeConnection method");
            }
        }
    }

}
