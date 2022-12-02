package by.dudko.carsales.service;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarAd;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserReadDto> findAll();

    Optional<UserReadDto> findById(long userId);

    <T> List<T> findUserAds(long userId, DtoMapper<CarAd, T> mapper);

    UserReadDto registerUser(UserCreateEditDto userDto);

    Optional<UserReadDto> updateUser(long userId, UserCreateEditDto userDto);

    boolean deleteUserById(long userId);
}
