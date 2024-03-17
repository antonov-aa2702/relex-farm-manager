package ru.relex.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.relex.backend.aop.annotation.Loggable;
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
import ru.relex.backend.service.SecurityService;
import ru.relex.backend.service.UserService;

import java.util.Optional;

/**
 * Реализация {@link SecurityService}
 */
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Loggable
    public JwtResponse login(JwtRequest jwtRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jwtRequest.getEmail(),
                jwtRequest.getPassword()));
        User user = userService.getByEmail(jwtRequest.getEmail());
        return JwtResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole()))
                .build();
    }

    @Override
    @Loggable
    public ResponseUserDto registration(UserDto userDto) {
        Optional<User> mayBeUser = userRepository.findByEmail(userDto.getEmail());
        if (mayBeUser.isPresent()) {
            throw new ResourceIllegalStateException("Пользователь уже существует");
        }
        User user = userMapper.toEntity(userDto);
        user.setRole(Role.ROLE_EMPLOYEE);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return getRegistrationUserDto(user);
    }

    private ResponseUserDto getRegistrationUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
