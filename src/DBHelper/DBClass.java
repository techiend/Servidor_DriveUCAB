package DBHelper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBClass {

    private static HikariDataSource db;

    static {

        try {
            HikariConfig configUM = new HikariConfig();
            configUM.setJdbcUrl("jdbc:postgresql://24.63.57.90:5432/redes2");
            configUM.setUsername("postgres");
            configUM.setPassword("123456");
            configUM.setDriverClassName("org.postgresql.Driver");
            configUM.addDataSourceProperty("cachePrepStmts", "true");
            configUM.addDataSourceProperty("prepStmtCacheSize", "250");
            configUM.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            configUM.setMaximumPoolSize(3);
            configUM.setAutoCommit(true);

            db = new HikariDataSource(configUM);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static Connection getConn() throws SQLException {
        return db.getConnection();
    }
}
