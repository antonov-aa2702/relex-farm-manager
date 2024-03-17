package ru.relex.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.relex.backend.entity.Role;
import ru.relex.backend.util.props.JwtProperties;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Класс для работы c JWT токенами.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Генерирует JWT токен по идентификатору пользователя, имени пользователя и роли
     * с помощью свойств в {@link JwtProperties}
     *
     * @param userId   идентификатор пользователя
     * @param username имя пользователя
     * @param role     роль пользователя
     * @return сгенерированный JWT токен
     */
    public String generateToken(Long userId, String username, Role role) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add("role", role.name())
                .build();
        Instant validity = Instant.now()
                .plus(jwtProperties.getLifetime(), ChronoUnit.HOURS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Осуществляет валидацию JWT токена.
     *
     * @param token токен для валидации
     * @return {@code true}, если токен действителен, иначе {@code false}.
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return claims.getPayload()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception exception) {
            log.info("Failed to validate token: {}", exception.getMessage());
            return false;
        }
    }

    /**
     * Возвращает {@link Authentication} по полученному JWT токену.
     * Метод вызывается только после проверки токена на валидность.
     *
     * @param token токен, прошедший валидацию
     * @return объект {@link Authentication} для дальнейшей аутентификации пользователя
     */
    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    private String extractUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
