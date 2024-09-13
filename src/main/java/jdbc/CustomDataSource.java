    package jdbc;

    import javax.sql.DataSource;
    import lombok.Getter;
    import lombok.Setter;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.sql.SQLFeatureNotSupportedException;
    import java.util.Properties;
    import java.util.logging.Logger;

    @Getter
    @Setter
    public class CustomDataSource implements DataSource {
        private static volatile CustomDataSource instance;
        private final String driver;
        private final String url;
        private final String name;
        private final String password;

        private CustomDataSource(String driver, String url, String password, String name) {
            this.driver = driver;
            this.url = url;
            this.name = name;
            this.password = password;
        }

        public static CustomDataSource getInstance() {
            if (instance == null) {
                Properties properties = new Properties();
                try {
                    properties.load(CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties"));
                    String driver = properties.getProperty("postgres.driver");
                    String url = properties.getProperty("postgres.url");
                    String name = properties.getProperty("postgres.password");
                    String password = properties.getProperty("postgres.name");
                    instance = new CustomDataSource(driver, url, name, password);
                    Class.forName(properties.getProperty("postgres.driver"));
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return instance;
        }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String user, String password) throws SQLException {
        return new CustomConnector().getConnection(url, user, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
