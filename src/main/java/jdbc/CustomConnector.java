package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CustomConnector {
    private final Properties properties;

    public CustomConnector() {
        properties = new Properties();
        try (InputStream input = CustomConnector.class.getResourceAsStream("/app.properties")) {
            properties.load(input);
            Class.forName(properties.getProperty("driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(String url) {
        String user = properties.getProperty("name");
        String password = properties.getProperty("password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
