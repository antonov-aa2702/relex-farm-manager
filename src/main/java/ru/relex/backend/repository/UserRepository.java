package ru.relex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.relex.backend.entity.User;

import java.util.Optional;

/**
 * Интерфейс определяет методы взаимодействия с данными, определенными сущностью {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Возвращает пользователя по его электронной почте.
     *
     * @param email электронная почта
     * @return {@code Optional}, содержащий пользователя или {@code Optional.empty()},
     * если пользователя не найдено.
     */
    Optional<User> findByEmail(String email);
}
