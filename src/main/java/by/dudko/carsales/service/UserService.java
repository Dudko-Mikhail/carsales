package by.dudko.carsales.service;

import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserReadDto> findAll();

    Optional<UserReadDto> findById(long userId);

    UserReadDto registerUser(UserCreateEditDto userDto);

    Optional<UserReadDto> updateUser(long userId, UserCreateEditDto userDto);

    boolean deleteUserById(long userId);
}
