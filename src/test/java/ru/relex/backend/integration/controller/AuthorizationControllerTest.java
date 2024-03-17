package ru.relex.backend.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.relex.backend.annotation.IntegrationTest;
import ru.relex.backend.dto.auth.JwtRequest;
import ru.relex.backend.dto.user.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor
class AuthorizationControllerTest {

    @Value("${owner.email}")
    private String ownerEmail;

    @Value("${owner.password}")
    private String ownerPassword;

    private ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLoginIfUserExists() throws Exception {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(ownerEmail)
                .password(ownerPassword)
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(ownerEmail))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginIfUserDoesNotExist() throws Exception {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email("test@email.com")
                .password("dummy")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Аутентификация не удалась! Проверьте логин и пароль!"));
    }

    @Test
    void testLoginIfEmailInvalid() throws Exception {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email("dummy")
                .password("dummy")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors.email")
                        .value("Неправильный формат электронного адреса"));
    }

    @Test
    @WithMockUser(
            username = "${owner.email}",
            password = "${owner.password}",
            roles = "OWNER")
    void testRegistrationIfUserDoesNotExist() throws Exception {
        UserDto userDto = UserDto.builder()
                .firstName("Andrey")
                .middleName("Antonov")
                .lastName("Andreevich")
                .email("test@email.com")
                .password("12345")
                .build();

        mockMvc.perform(post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ROLE_EMPLOYEE"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @WithMockUser(
            username = "${owner.email}",
            password = "${owner.password}",
            roles = "OWNER")
    void testRegistrationIfUserExists() throws Exception {
        UserDto userDto = UserDto.builder()
                .firstName("Andrey")
                .middleName("Antonov")
                .lastName("Andreevich")
                .email(ownerEmail)
                .password(ownerPassword)
                .build();

        mockMvc.perform(post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Пользователь уже существует"));
    }
}