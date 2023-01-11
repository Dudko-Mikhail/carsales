package by.dudko.carsales.integration.service;

import by.dudko.carsales.integration.BaseIntegrationTest;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import by.dudko.carsales.model.dto.user.UserReadDto;
import by.dudko.carsales.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserServiceTest extends BaseIntegrationTest {
    private static final Long USER_ID = 1L;
    private static final Long NON_EXISTENT_ID = 100L;
    private final UserService userService;

    @Test
    void findAll() {
        assertThat(userService.findAll()).hasSize(5);
    }

    @Test
    void findByIdWithExistingId() {
        Optional<UserReadDto> maybeUser = userService.findById(USER_ID);
        assertThat(maybeUser).isPresent();

        maybeUser.ifPresent(user -> {
            assertThat(user.getEmail()).isEqualTo("ivan@mail.ru");
            assertThat(user.getPhoneNumber()).isEqualTo("375254781236");
            assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2021, 12, 1, 12, 0));
        });
    }

    @Test
    void findByIdWithNonExistentId() {
        assertThat(userService.findById(NON_EXISTENT_ID)).isEmpty();
    }

    @Test
    void registerUser() {
        UserCreateEditDto newUser = UserCreateEditDto.builder()
                .email("test@mail.ru")
                .phoneNumber("911")
                .build();

        UserReadDto createdUser = userService.registerUser(newUser);

        assertThat(createdUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(createdUser.getPhoneNumber()).isEqualTo(newUser.getPhoneNumber());
    }

    @Test
    void updateUserWithExistingId() {
        UserCreateEditDto createDto = UserCreateEditDto.builder()
                .email("Joe@gmail.com")
                .phoneNumber("375447614281")
                .build();

        var updatedUser = userService.updateUser(USER_ID, createDto);

        assertThat(updatedUser).isPresent();
        updatedUser.ifPresent(user -> {
            assertThat(user.getEmail()).isEqualTo(createDto.getEmail());
            assertThat(user.getPhoneNumber()).isEqualTo(createDto.getPhoneNumber());
        });
    }

    @Test
    void updateWithNonExistentId() {
        UserCreateEditDto user = UserCreateEditDto.builder()
                .email("Joe@gmail.com")
                .phoneNumber("375447614281")
                .build();

        assertThat(userService.updateUser(NON_EXISTENT_ID, user)).isEmpty();
    }

    @Test
    void deleteByIdWithExistingId() {
        assertTrue(userService.deleteUserById(USER_ID));
        assertThat(userService.findById(USER_ID)).isEmpty();
    }

    @Test
    void deleteByIdWithNonExistentId() {
        assertFalse(userService.deleteUserById(NON_EXISTENT_ID));
    }
}
