package by.dudko.carsales.service.impl;

import by.dudko.carsales.mapper.DtoMapper;
import by.dudko.carsales.mapper.impl.UserCreateEditMapper;
import by.dudko.carsales.mapper.impl.UserReadMapper;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.model.entity.CarAd;
import by.dudko.carsales.repository.CarAdRepository;
import by.dudko.carsales.repository.UserRepository;
import by.dudko.carsales.service.AdService;
import by.dudko.carsales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AdService adService;
    private final CarAdRepository adRepository;
    private final UserReadMapper readMapper;
    private final UserCreateEditMapper createEditMapper;

    @Override
    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(readMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserReadDto> findById(long userId) {
        return userRepository.findById(userId)
                .map(readMapper::map);
    }

    @Override
    public <T> List<T> findUserAds(long userId, DtoMapper<CarAd, T> mapper) {
        return adRepository.findAllByOwnerId(userId).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserReadDto registerUser(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .map(createEditMapper::map)
                .map(userRepository::save)
                .map(readMapper::map)
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<UserReadDto> updateUser(long userId, UserCreateEditDto userDto) {
        return userRepository.findById(userId)
                .map(user -> createEditMapper.map(userDto, user))
                .map(readMapper::map);
    }

    @Override
    @Transactional
    public boolean deleteUserById(long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.getUserAds()
                            .forEach(ad -> adService.deleteById(ad.getId()));
                    userRepository.deleteById(userId);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
