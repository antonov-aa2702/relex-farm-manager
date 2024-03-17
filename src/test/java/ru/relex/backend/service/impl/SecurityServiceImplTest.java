package ru.relex.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.relex.backend.annotation.UnitTest;
import ru.relex.backend.dto.auth.JwtRequest;
import ru.relex.backend.dto.auth.JwtResponse;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Role;
import ru.relex.backend.entity.User;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.repository.UserRepository;
import ru.relex.backend.security.JwtTokenProvider;
import ru.relex.backend.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private SecurityServiceImpl securityService;

    @Test
    void testLoginIfUserExists() {
        String email = "email";
        String password = "password";
        String token = "token";
        JwtRequest jwtRequest = getJwtRequest(email, password);
        User user = getDefaultUser();

        when(userService.getByEmail(jwtRequest.getEmail()))
                .thenReturn(user);
        when(jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole()))
                .thenReturn(token);

        JwtResponse actualResult = securityService.login(jwtRequest);

        verify(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(
                        jwtRequest.getEmail(),
                        jwtRequest.getPassword()));
        assertThat(actualResult.getId())
                .isEqualTo(user.getId());
        assertThat(actualResult.getToken())
                .isEqualTo(token);
        assertThat(actualResult.getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    void testLoginIfUserDoesNotExist() {
        String email = "email";
        String password = "password";
        JwtRequest jwtRequest = getJwtRequest(email, password);
        when(userService.getByEmail(jwtRequest.getEmail()))
                .thenThrow(ResourceIllegalStateException.class);

        verifyNoInteractions(jwtTokenProvider);
        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> securityService
                        .login(jwtRequest));
    }


    @Test
    void testRegistrationIfUserDoesNotExist() {
        UserDto userDto = getDefaultUserDto();
        User user = getDefaultUser();

        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword()))
                .thenReturn(userDto.getPassword());
        when(userMapper.toEntity(userDto))
                .thenReturn(user);

        ResponseUserDto actualResult = securityService.registration(userDto);

        assertThat(actualResult.getEnabled())
                .isEqualTo(true);
        assertThat(actualResult.getRole())
                .isEqualTo(Role.ROLE_EMPLOYEE);
        verify(userRepository)
                .findByEmail(userDto.getEmail());
        verify(passwordEncoder)
                .encode(userDto.getPassword());
        verify(userMapper)
                .toEntity(userDto);
    }

    @Test
    void testRegistrationIfUserExist() {
        UserDto userDto = getDefaultUserDto();
        when(userRepository.findByEmail(userDto.getEmail()))
                .thenReturn(Optional.of(getDefaultUser()));
        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> securityService.registration(userDto));
        verify(userRepository)
                .findByEmail(userDto.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userMapper);
    }

    private UserDto getDefaultUserDto() {
        return UserDto.builder()
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .email("test@gmail.com")
                .password("12345")
                .build();
    }

    private JwtRequest getJwtRequest(String email, String password) {
        return JwtRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    private User getDefaultUser() {
        return User.builder()
                .id(1L)
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("password")
                .email("test@gmail.com")
                .role(Role.ROLE_EMPLOYEE)
                .enabled(true)
                .build();
    }
}
