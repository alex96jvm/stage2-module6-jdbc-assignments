package jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection;
    private PreparedStatement ps;
    private Statement st;

    private static final String createUserSQL = "INSERT INTO myusers (firstname, lastname, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myusers SET firstname = ?, lastname = ?, age = ? WHERE id = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User user) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(Long userId) {
        User user;

        try (Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(findUserByIdSQL)){

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                user = new User(
                    rs.getLong("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getInt("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user;

        try (Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(findUserByNameSQL)){

            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                user = new User(
                    rs.getLong("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getInt("age"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<User> findAllUser() {
        List <User> users = new ArrayList<>();

        try (Connection connection = CustomDataSource.getInstance().getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(findAllUserSQL)) {

            while (rs.next()) {
                User user = new User(
                    rs.getLong("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User updateUser(User user) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(updateUserSQL)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user ;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(deleteUser)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
