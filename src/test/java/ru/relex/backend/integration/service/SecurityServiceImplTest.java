package ru.relex.backend.integration.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import ru.relex.backend.annotation.IntegrationTest;
import ru.relex.backend.dto.auth.JwtRequest;
import ru.relex.backend.dto.auth.JwtResponse;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Role;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.service.SecurityService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.extractProperty;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RequiredArgsConstructor
class SecurityServiceImplTest {

    @Value("${owner.email}")
    private String ownerEmail;

    @Value("${owner.password}")
    private String ownerPassword;

    private final SecurityService securityService;

    @Test
    void testLoginIfUserExists() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(ownerEmail)
                .password(ownerPassword)
                .build();

        JwtResponse actualResult = assertDoesNotThrow(() -> securityService.login(jwtRequest));

        assertThat(actualResult)
                .isNotNull();
        assertThat(actualResult.getId())
                .isEqualTo(1);
        assertThat(actualResult.getEmail())
                .isEqualTo(ownerEmail);
        assertThat(actualResult.getToken())
                .isNotNull();
    }

    @Test
    void testLoginIfUserDoesNotExist() {
        String email = "dummy@email.com";
        String password = "12345";
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(email)
                .password(password)
                .build();

        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> securityService
                        .login(jwtRequest))
                .withMessage("Пользователь не найден");
    }

    @Test
    void testLoginFailIfPasswordIncorrect() {
        JwtRequest jwtRequest = JwtRequest.builder()
                .email(ownerEmail)
                .password("dummy")
                .build();

        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> securityService
                        .login(jwtRequest))
                .withMessage("Неверные учетные данные пользователя");
    }


    @Test
    void testRegistrationIfUserDoesNotExist() {
        UserDto userDto = UserDto.builder()
                .firstName("Andrey")
                .middleName("Antonov")
                .lastName("Andreevich")
                .email("test@gmail.com")
                .password("12345")
                .build();

        ResponseUserDto actualResult = assertDoesNotThrow(() -> securityService.registration(userDto));

        assertThat(actualResult.getEnabled())
                .isEqualTo(true);
        assertThat(actualResult.getRole())
                .isEqualTo(Role.ROLE_EMPLOYEE);
    }

    @Test
    void testRegistrationIfUserExist() {
        UserDto userDto = UserDto.builder()
                .firstName("Andrey")
                .middleName("Antonov")
                .lastName("Andreevich")
                .email(ownerEmail)
                .password(ownerPassword)
                .build();

        ResourceIllegalStateException exception =
                assertThrows(ResourceIllegalStateException.class,
                        () -> securityService.registration(userDto));
        Assertions.assertThat(exception.getMessage())
                .isEqualTo("Пользователь уже существует");
    }
}
