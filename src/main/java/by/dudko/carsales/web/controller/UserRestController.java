package by.dudko.carsales.web.controller;

import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.service.AdService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final AdService adService;

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
    public List<?> findUserAds(@PathVariable long id,
                               @RequestParam(name = "full", defaultValue = "false") boolean isFull) {
        var userAds = isFull ? adService.findByOwnerIdWithFullData(id)
                : adService.findByOwnerId(id);
        return userAds.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
