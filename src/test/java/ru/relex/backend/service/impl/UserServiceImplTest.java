package ru.relex.backend.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.relex.backend.annotation.UnitTest;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Role;
import ru.relex.backend.entity.User;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetByIdIfUserExists() {
        String email = "email";
        long userId = 1L;
        User user = getDefaultUser("email", userId, Role.ROLE_EMPLOYEE, true);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        UserDto userDto = getDefaultUserDto(email, userId, true);
        when(userMapper.toDto(user))
                .thenReturn(userDto);

        UserDto actualResult = userService.getById(userId);

        Assertions.assertThat(actualResult)
                .isEqualTo(userDto);
        verify(userRepository).findById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    void testGetByIdIfUserDoesNotExist() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> {
                    userService.getById(userId);
                }).withMessage("Пользователь не найден");
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetByEmailIfUserExists() {
        String email = "email";
        User user = getDefaultUser(email, 1L, Role.ROLE_EMPLOYEE, true);
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        User actualResult = userService.getByEmail(email);

        Assertions.assertThat(actualResult)
                .isEqualTo(user);
        verify(userRepository).findByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testGetByEmailIfUserNotExist() {
        String email = "email";
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> {
                    userService.getByEmail(email);
                }).withMessage("Пользователь не найден");
        verify(userRepository).findByEmail(email);
        verifyNoInteractions(userMapper);
    }


    @Test
    void testDismissEmployeeIfUserDoesNotDismiss() {
        String email = "email";
        long userId = 1L;
        User user = getDefaultUser(email, userId, Role.ROLE_EMPLOYEE, true);
        UserDto userDto = getDefaultUserDto(email, userId, true);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userMapper.toEntity(userDto))
                .thenReturn(user);
        when(userMapper.toDto(user))
                .thenReturn(userDto);
        when(userService.getById(userId))
                .thenReturn(userDto);

        ResponseUserDto actualResult = userService.dismissEmployee(userId);

        assertThat(user.getEnabled())
                .isFalse();
        assertThat(actualResult.getEnabled())
                .isFalse();
        verify(userRepository, times(2))
                .findById(userId);
        verify(userMapper)
                .toEntity(userDto);
        verify(userMapper)
                .toDto(user);
    }

    @Test
    void testDismissEmployeeIfUserDoesNotExist() {
        long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> {
                    userService.dismissEmployee(userId);
                }).withMessage("Пользователь не найден");
        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void testDismissEmployeeIfUserDismiss() {
        String email = "email";
        long userId = 1L;
        User user = getDefaultUser(email, userId, Role.ROLE_EMPLOYEE, false);
        UserDto userDto = getDefaultUserDto(email, userId, false);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(userDto);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(userMapper.toEntity(userDto))
                .thenReturn(user);

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> {
                    userService.dismissEmployee(userId);
                }).withMessage("Пользователь уже удален");
        verify(userRepository, times(2))
                .findById(userId);
        verify(userMapper)
                .toDto(user);
        verify(userMapper)
                .toEntity(userDto);
    }

    @Test
    void testDismissEmployeeIfUserIsOwner() {
        String email = "email";
        long userId = 1L;
        User user = getDefaultUser(email, userId, Role.ROLE_OWNER, true);
        UserDto userDto = getDefaultUserDto(email, userId, true);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(userDto);
        when(userService.getById(userId))
                .thenReturn(userDto);
        when(userMapper.toEntity(userDto))
                .thenReturn(user);

        assertThatExceptionOfType(ResourceIllegalStateException.class)
                .isThrownBy(() -> {
                    userService.dismissEmployee(userId);
                }).withMessage("Нельзя удалить собственный аккаунт");
        verify(userRepository, times(2))
                .findById(userId);
        verify(userMapper)
                .toDto(user);
        verify(userMapper)
                .toEntity(userDto);
    }


    private User getDefaultUser(String email, long userId, Role role, boolean enabled) {
        return User.builder()
                .id(userId)
                .email(email)
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("12345")
                .role(role)
                .enabled(enabled)
                .build();
    }

    private UserDto getDefaultUserDto(String email, long userId, boolean enabled) {
        return UserDto.builder()
                .id(userId)
                .email(email)
                .firstName("FirstName")
                .middleName("MiddleName")
                .lastName("LastName")
                .password("12345")
                .role(Role.ROLE_EMPLOYEE)
                .enabled(enabled)
                .build();
    }
}
