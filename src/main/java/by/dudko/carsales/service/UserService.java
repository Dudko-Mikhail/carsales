package by.dudko.carsales.service;

import by.dudko.carsales.model.dto.user.CreateEditUserDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.model.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    boolean registerUser(CreateEditUserDto userDto);
    boolean updateUser(CreateEditUserDto userDto);
    boolean deleteUser(long userId);
    List<CarAd> getUserAds(long userId); // todo может в ads service
}
