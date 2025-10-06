package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceProvider {
    // H2 em arquivo local (pasta ./data). AUTO_SERVER permite acesso concorrente em dev.
    private static final String URL = "jdbc:h2:file:./data/game;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
