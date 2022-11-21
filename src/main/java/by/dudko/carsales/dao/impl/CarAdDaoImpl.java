package by.dudko.carsales.dao.impl;

import by.dudko.carsales.dao.CarAdDao;
import by.dudko.carsales.exception.DaoException;
import by.dudko.carsales.mapper.impl.CarAdMapper;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.pool.ConnectionPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CarAdDaoImpl implements CarAdDao {
    private static final String FIND_ALL = "SELECT ads.* FROM car_ads ads";
    private static final String FIND_BY_ID = "SELECT ads.* FROM car_ads ads WHERE id = ?";
    private static final String INSERT = "INSERT INTO car_ads (year, brand, model, engine_volume, car_state, mileage, power, user_id, created_at, updated_at)" +
            "VALUES (?, ?, ?, ?, cast(? as carstate), ?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM car_ads WHERE id = ?";

    private static final CarAdDao instance = new CarAdDaoImpl();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final CarAdMapper mapper = CarAdMapper.getInstance();

    public static CarAdDao getInstance() {
        return instance;
    }

    @Override
    public List<CarAd> findAll() throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(FIND_ALL);
            return mapper.mapRows(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find all car ads", e);
        }
    }

    @Override
    public Optional<CarAd> findById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            return mapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Failed to find car ad by id " + id, e);
        }
    }

    @Override
    public void insert(CarAd ad) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, ad.getYear());
            preparedStatement.setString(2, ad.getBrand());
            preparedStatement.setString(3, ad.getModel());
            preparedStatement.setInt(4, ad.getEngineVolume());
            preparedStatement.setString(5, ad.getCarState().name());
            preparedStatement.setInt(6, ad.getMileage());
            preparedStatement.setInt(7, ad.getPower());
            preparedStatement.setLong(8, ad.getUserId());
            preparedStatement.setTimestamp(9, Timestamp.valueOf(ad.getCreatedAt()));
            preparedStatement.setTimestamp(10, Timestamp.valueOf(ad.getUpdatedAt()));
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
                throw new DaoException("Failed to get generated key");
            }
            ad.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            throw new DaoException("Failed to insert car ad", e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws DaoException {
        try (var connection = connectionPool.takeConnection();
             var preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Failed to delete car ad by id" + id, e);
        }
    }
}
