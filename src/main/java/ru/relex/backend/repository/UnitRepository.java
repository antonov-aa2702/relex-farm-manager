package ru.relex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.relex.backend.entity.Unit;

import java.util.Optional;

/**
 * Интерфейс определяет методы взаимодействия с данными, определенными сущностью {@link Unit}.
 */
@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

    /**
     * Возвращает единицу измерения по ее имени без учета регистра.
     *
     * @param name название единицы измерения
     * @return {@code Optional}, содержащий единицу измерения или {@code Optional.empty()},
     * если единица измерения не найдена.
     */
    Optional<Unit> findByNameIgnoreCase(String name);
}
