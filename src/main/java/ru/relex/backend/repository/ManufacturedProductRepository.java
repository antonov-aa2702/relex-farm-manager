package ru.relex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.relex.backend.entity.ManufacturedProduct;

/**
 * Класс предназначен для работы с данными, определенными сущностью {@link ManufacturedProduct}.
 */
public interface ManufacturedProductRepository extends JpaRepository<ManufacturedProduct, Long>,
        FilterManufacturedProductRepository {
}
