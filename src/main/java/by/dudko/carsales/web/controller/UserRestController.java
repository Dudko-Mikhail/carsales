package by.dudko.carsales.web.controller;

import by.dudko.carsales.mapper.impl.CarAdFullInfoReadMapper;
import by.dudko.carsales.mapper.impl.CarAdReadMapper;
import by.dudko.carsales.model.dto.carad.CarAdFullReadDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final CarAdReadMapper adReadMapper;
    private final CarAdFullInfoReadMapper adFullInfoReadMapper;

    @GetMapping
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable long id) {
        return userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/ads")
    public List<CarAdReadDto> findUserAds(@PathVariable long id) {
        return userService.findUserAds(id, adReadMapper);
    }

    @GetMapping("/{id}/ads/full")
    public List<CarAdFullReadDto> findUserAdsWithFullInfo(@PathVariable long id) {
        return userService.findUserAds(id, adFullInfoReadMapper);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserReadDto registerUser(@RequestBody @Validated UserCreateEditDto userDto) {
        return userService.registerUser(userDto);
    }

    @PutMapping("/{id}")
    public UserReadDto updateUser(@PathVariable long id, @RequestBody @Validated UserCreateEditDto userDto) {
        return userService.updateUser(id, userDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        if (!userService.deleteUserById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
