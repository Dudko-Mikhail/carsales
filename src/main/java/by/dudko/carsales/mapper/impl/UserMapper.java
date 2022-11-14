package by.dudko.carsales.mapper.impl;

import by.dudko.carsales.mapper.RowMapper;
import by.dudko.carsales.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper implements RowMapper<User> {
    private static final UserMapper instance = new UserMapper();

    public static UserMapper getInstance() {
        return instance;
    }

    @Override
    public Optional<User> mapRow(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return Optional.of(User.builder()
                        .id(resultSet.getLong("id"))
                        .email(resultSet.getString("email"))
                        .phoneNumber(resultSet.getString("phone_number"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException e) {
            log.error("Failed to map user", e);
        }
        return Optional.empty();
    }
}
