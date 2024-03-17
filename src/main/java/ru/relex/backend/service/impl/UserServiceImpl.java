package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.relex.backend.aop.annotation.Loggable;
import ru.relex.backend.dto.user.ResponseUserDto;
import ru.relex.backend.dto.user.UserDto;
import ru.relex.backend.entity.Role;
import ru.relex.backend.entity.User;
import ru.relex.backend.exception.ResourceIllegalStateException;
import ru.relex.backend.mapper.UserMapper;
import ru.relex.backend.repository.UserRepository;
import ru.relex.backend.service.UserService;

/**
 * Реализация {@link UserService}
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceIllegalStateException("Пользователь не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceIllegalStateException("Пользователь не найден"));
    }

    @Override
    @Transactional
    @Loggable
    public ResponseUserDto dismissEmployee(Long employeeID) {
        UserDto userDto = getById(employeeID);
        User user = userMapper.toEntity(userDto);
        if (!user.getEnabled()) {
            throw new ResourceIllegalStateException("Пользователь уже удален");
        }
        if (user.getRole().equals(Role.ROLE_OWNER)) {
            throw new ResourceIllegalStateException("Нельзя удалить собственный аккаунт");
        }
        user.setEnabled(false);
        userRepository.save(user);
        return getResponseUserDto(user);
    }

    private ResponseUserDto getResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
