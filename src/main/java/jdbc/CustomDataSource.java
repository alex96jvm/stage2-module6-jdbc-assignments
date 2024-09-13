    package jdbc;

    import javax.sql.DataSource;
    import lombok.Getter;
    import lombok.Setter;
    import java.io.PrintWriter;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.sql.SQLFeatureNotSupportedException;
    import java.util.ResourceBundle;
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
                ResourceBundle rb = ResourceBundle.getBundle("app");
                instance = new CustomDataSource(rb.getString("postgres.driver"),
                        rb.getString("postgres.url"),
                        rb.getString("postgres.password"),
                        rb.getString("postgres.name"));
            }
            return instance;
        }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            return new CustomConnector().getConnection(url, name, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String user, String password) throws SQLException {
        try {
            return new CustomConnector().getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
