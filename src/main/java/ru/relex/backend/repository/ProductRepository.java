package ru.relex.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Unit;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс определяет методы взаимодействия с данными,
 * связанными с {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Возвращает продукт по его имени и единице измерения.
     *
     * @param name имя продукта
     * @param unit единица измерения
     * @return {@code Optional}, содержащий продукт или {@code Optional.empty()}, если продукт не найден.
     */
    Optional<Product> findByNameIgnoreCaseAndUnit(String name, Unit unit);

    /**
     * Возвращает все продукты, зарегистрированные в системе.
     * <p>
     * Для предотвращения проблемы N+1 определяется {@link EntityGraph}, который
     * определяет характер получения единиц измерения одновременно в момент выборки продуктов с помощью JOIN.
     *
     * @return список всех продуктов
     */
    @EntityGraph(attributePaths = {"unit"})
    List<Product> findAll();
}
