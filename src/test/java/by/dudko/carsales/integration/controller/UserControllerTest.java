package by.dudko.carsales.integration.controller;

import by.dudko.carsales.integration.BaseIntegrationTest;
import by.dudko.carsales.model.dto.user.UserCreateEditDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends BaseIntegrationTest {
    private static final long USER_ID = 1L;
    private static final long NON_EXISTENT_USER_ID = 100L;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(5)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(String.format("/users/%d", USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email").value("ivan@mail.ru"))
                .andExpect(jsonPath("phoneNumber").value("375254781236"))
                .andExpect(jsonPath("createdAt").value("2021-12-01T12:00:00"));
    }

    @Test
    void findUserAds() throws Exception {
        mockMvc.perform(get(String.format("/users/%d/ads", USER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findUserAdsWithNonExistentUser() throws Exception {
        mockMvc.perform(get(String.format("/users/%d/ads", NON_EXISTENT_USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdWithNonExistentId() throws Exception {
        mockMvc.perform(get(String.format("/users/%d", NON_EXISTENT_USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerUserWithValidData() throws Exception {
        UserCreateEditDto newUser = UserCreateEditDto.builder()
                .email("Ivanov_Ivan12@gmail.com")
                .phoneNumber("375447462476")
                .build();
        String json = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(newUser.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(newUser.getPhoneNumber()));
    }

    @Test
    void registerUserWithInvalidData() throws Exception {
        UserCreateEditDto newUser = UserCreateEditDto.builder()
                .email("Ivanov_gmail.com")
                .phoneNumber("375447462476")
                .build();
        String json = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fieldErrors[0].field").value("email"));
    }

    @Test
    void updateUserWithValidData() throws Exception {
        UserCreateEditDto newUserInfo = UserCreateEditDto.builder()
                .email("testEmail@gmail.com")
                .phoneNumber("7788")
                .build();
        String json = objectMapper.writeValueAsString(newUserInfo);


        mockMvc.perform(put(String.format("/users/%d", USER_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.email").value(newUserInfo.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(newUserInfo.getPhoneNumber()));
    }

    @Test
    void updateUserWithInvalidData() throws Exception {
        UserCreateEditDto newUserInfo = UserCreateEditDto.builder()
                .email("notEmail_gmail.com")
                .phoneNumber("7788")
                .build();
        String json = objectMapper.writeValueAsString(newUserInfo);


        mockMvc.perform(put(String.format("/users/%d", USER_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.fieldErrors[0].field").value("email"));
    }

    @Test
    void updateNonExistentUser() throws Exception {
        UserCreateEditDto newUserInfo = UserCreateEditDto.builder()
                .email("testEmail@gmail.com")
                .phoneNumber("7788")
                .build();
        String json = objectMapper.writeValueAsString(newUserInfo);

        mockMvc.perform(put(String.format("/users/%d", NON_EXISTENT_USER_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(String.format("/users/%d", USER_ID)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteByIdWithNonExistentUser() throws Exception {
        mockMvc.perform(delete(String.format("/users/%d", NON_EXISTENT_USER_ID)))
                .andExpect(status().isNotFound());
    }
}
