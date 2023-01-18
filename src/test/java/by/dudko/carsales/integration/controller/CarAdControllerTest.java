package by.dudko.carsales.integration.controller;

import by.dudko.carsales.integration.BaseIntegrationTest;
import by.dudko.carsales.model.dto.carad.CarAdCreateDto;
import by.dudko.carsales.model.dto.carad.CarAdEditDto;
import by.dudko.carsales.model.dto.carad.CarAdReadDto;
import by.dudko.carsales.model.entity.CarState;
import by.dudko.carsales.utils.ImageStorageManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class CarAdControllerTest extends BaseIntegrationTest {
    private static final long AD_ID = 1L;
    private static final long NON_EXISTENT_AD_ID = 100L;
    private static final long IMAGE_ID = 1L;
    private static final long NON_EXISTENT_IMAGE_ID = 200L;
    private static ImageStorageManager storageManager;
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public void setStorageManager(ImageStorageManager storageManager) {
        CarAdControllerTest.storageManager = storageManager;
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/ads")
                        .param("size", "2")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.metadata.page").value(1))
                .andExpect(jsonPath("$.metadata.size").value(2))
                .andExpect(jsonPath("$.metadata.totalElements").value(3));

    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/ads/{id}", AD_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(AD_ID));
    }

    @Test
    void findByIdWithNonExistentId() throws Exception {
        mockMvc.perform(get("/ads/{id}", NON_EXISTENT_AD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdWithInvalidIdType() throws Exception {
        mockMvc.perform(get("/ads/{id}", "12test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    void findImageById() throws Exception {
        storageManager.fillImageStorage();
        mockMvc.perform(get("/ads/images/{id}", IMAGE_ID))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findImageByNonExistentId() throws Exception {
        mockMvc.perform(get("/ads/images/{id}", NON_EXISTENT_IMAGE_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void findOwner() throws Exception {
        mockMvc.perform(get("/ads/{id}/owner", AD_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$.phoneNumber").value("375254781236"))
                .andExpect(jsonPath("$.createdAt").value("2021-12-01T12:00:00"));
    }

    @Test
    void findOwnerOfNonExistentAd() throws Exception {
        mockMvc.perform(get("/ads/{id}/owner", NON_EXISTENT_AD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAdWithValidData() throws Exception {
        CarAdCreateDto createDto = CarAdCreateDto.builder()
                .userId(2L)
                .year(Year.of(2020))
                .brand("Good")
                .model("XW9")
                .engineVolume(68)
                .carState(CarState.NEW.name())
                .mileage(0)
                .power(640)
                .phoneNumbers(Set.of("777", "888", "999"))
                .build();
        String json = objectMapper.writeValueAsString(createDto);
        System.out.println(json);
        mockMvc.perform(post("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createAdWithInvalidData() throws Exception {
        CarAdCreateDto createDto = CarAdCreateDto.builder()
                .userId(9L)
                .year(Year.now().plusYears(1))
                .brand("")
                .engineVolume(-1)
                .carState(CarState.NEW.name())
                .mileage(-1)
                .power(-1)
                .phoneNumbers(Set.of("777", "888", "999", "678"))
                .build();
        String json = objectMapper.writeValueAsString(createDto);
        mockMvc.perform(post("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fieldErrors", Matchers.hasSize(8)))
                .andExpect(jsonPath("$.errors.fieldErrors.*.field",
                        Matchers.containsInAnyOrder("userId", "year", "brand", "model", "engineVolume", "mileage", "power", "phoneNumbers")));
    }

    @Test
    void updateAdWithValidData() throws Exception {
        CarAdEditDto editDto = CarAdEditDto.builder()
                .power(750)
                .year(Year.of(2000))
                .model("test")
                .brand("test")
                .mileage(1000)
                .build();
        String json = objectMapper.writeValueAsString(editDto);

        CarAdReadDto readDto = CarAdReadDto.builder()
                .id(AD_ID)
                .year(2000)
                .model("test")
                .brand("test")
                .mileage(1000)
                .ownerEmail("ivan@mail.ru")
                .createdAt(LocalDateTime.of(2021, 12, 3, 13, 24))
                .imagesCount(2)
                .build();
        String expectedResponse = objectMapper.writeValueAsString(readDto);

        mockMvc.perform(put("/ads/{id}", AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void updateAdWithInvalidData() throws Exception {
        CarAdEditDto editDto = CarAdEditDto.builder()
                .power(-750)
                .year(Year.now().plusYears(1))
                .model("test")
                .brand("test")
                .mileage(-1000)
                .build();
        String json = objectMapper.writeValueAsString(editDto);

        mockMvc.perform(put("/ads/{id}", AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.fieldErrors", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.errors.fieldErrors.*.field",
                        Matchers.containsInAnyOrder("power", "mileage", "year")));
    }

    @Test
    void updateNonExistentAd() throws Exception {
        CarAdEditDto editDto = CarAdEditDto.builder()
                .power(750)
                .year(Year.of(2000))
                .model("test")
                .brand("test")
                .mileage(1000)
                .build();
        String json = objectMapper.writeValueAsString(editDto);

        mockMvc.perform(put("/ads/{id}", NON_EXISTENT_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void findOwnerWithExistingAd() throws Exception {
        mockMvc.perform(get("/ads/{id}/owner", AD_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$.phoneNumber").value("375254781236"));
    }

    @Test
    void findOwnerWithNonExistentAd() throws Exception {
        mockMvc.perform(get("/ads/{id}/owner", NON_EXISTENT_AD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadImages() throws Exception {
        byte[] bytes = new byte[]{100, 101, 102, 103, 104, 105};
        MockMultipartFile testFile = new MockMultipartFile("images", "test.png",
                MediaType.IMAGE_PNG_VALUE, bytes);
        mockMvc.perform(multipart("/ads/{id}/images", AD_ID)
                        .file(testFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void uploadImagesToNonExistentAd() throws Exception {
        byte[] bytes = new byte[]{100, 101, 102, 103, 104, 105};
        MockMultipartFile testFile = new MockMultipartFile("images", "test.png",
                MediaType.IMAGE_PNG_VALUE, bytes);
        mockMvc.perform(multipart("/ads/{id}/images", NON_EXISTENT_AD_ID)
                        .file(testFile))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAd() throws Exception {
        storageManager.fillImageStorage();
        mockMvc.perform(delete("/ads/{id}", AD_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAdWithNonExistentAd() throws Exception {
        mockMvc.perform(delete("/ads/{id}", NON_EXISTENT_AD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void deleteAdImage() throws Exception {
        mockMvc.perform(delete("/ads/images/{id}", IMAGE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAdImageWithNonExistentAd() throws Exception {
        mockMvc.perform(delete("/ads/images/{id}", NON_EXISTENT_IMAGE_ID))
                .andExpect(status().isNotFound());
    }

    @AfterAll
    static void clearImageStorage() {
        storageManager.clearImageStorage();
    }
}
