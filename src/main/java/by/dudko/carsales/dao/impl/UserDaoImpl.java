package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.UserDao;
import by.dudko.carsales.exception.DaoException;
import by.dudko.carsales.mapper.impl.UserMapper;
import by.dudko.carsales.model.entity.User;
import by.dudko.carsales.pool.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDaoImpl implements UserDao {
    private static final String FIND_ALL = "SELECT u.* FROM users u";
    private static final String FIND_BY_ID = "SELECT u.* FROM users u WHERE id = ?";
    private static final String INSERT = "INSERT INTO users (email, phone_number, created_at, updated_at)" +
            " VALUES(?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id = ?";


    private static final UserDao instance = new UserDaoImpl();
    private final UserMapper mapper = UserMapper.getInstance();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static UserDao getInstance() {
        return instance;
    }

    @Override
    public List<User> findAll() throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(FIND_ALL);
            return mapper.mapRows(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find all users", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            return mapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find user by id " + id, e);
        }
    }

    @Override
    public void insert(User user) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(user.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(user.getUpdatedAt()));
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new DaoException("Failed to get user id");
            }
            user.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            throw new DaoException("Failed to insert user", e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete user by id " + id, e);
        }
    }
}
