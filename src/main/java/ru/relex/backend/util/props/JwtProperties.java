package ru.relex.backend.util.props;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Конфигурация для работы с JWT
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "security.jwt")
@Component
public class JwtProperties {
    /**
     * Секрет для работы с JWT
     */
    private String secret;

    /**
     * Время жизни токена в часах
     */
    private Long lifetime;
}
