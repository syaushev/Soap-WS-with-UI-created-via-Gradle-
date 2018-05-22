package databases;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ZipDB {
    public ZipDB() {
    }


    public ResultSet getRS() throws SQLException {
        Connection conn = MyDBInit.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM zipcodes");
        ResultSet rs = stmt.executeQuery();

        return rs;
    }
}

