package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.PhoneNumberDao;
import by.dudko.carsales.exception.DaoException;
import by.dudko.carsales.mapper.impl.PhoneNumberMapper;
import by.dudko.carsales.model.entity.PhoneNumber;
import by.dudko.carsales.pool.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneNumberDaoImpl implements PhoneNumberDao {
    private static final String FIND_ALL = "SELECT ph.* FROM ad_phone_numbers ph";
    private static final String FIND_BY_ID = "SELECT ph.* FROM ad_phone_numbers ph WHERE id = ?";
    private static final String INSERT = "INSERT INTO ad_phone_numbers (ad_id, number) VALUES(?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM ad_phone_numbers WHERE id = ?";

    private static final PhoneNumberDao instance = new PhoneNumberDaoImpl();
    private final PhoneNumberMapper mapper = PhoneNumberMapper.getInstance();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static PhoneNumberDao getInstance() {
        return instance;
    }

    @Override
    public List<PhoneNumber> findAll() throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(FIND_ALL);
            return mapper.mapRows(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find all phone numbers", e);
        }
    }

    @Override
    public Optional<PhoneNumber> findById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            return mapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find phone number by id " + id, e);
        }
    }

    @Override
    public void insert(PhoneNumber phoneNumber) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, phoneNumber.getAdId());
            preparedStatement.setString(2, phoneNumber.getNumber());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new DaoException("Failed to get phone number id");
            }
            phoneNumber.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            throw new DaoException("Failed to insert phoneNumber", e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete phone number by id " + id, e);
        }
    }
}
