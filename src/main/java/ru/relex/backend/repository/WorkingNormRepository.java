package ru.relex.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.relex.backend.entity.Product;
import ru.relex.backend.entity.Status;
import ru.relex.backend.entity.User;
import ru.relex.backend.entity.WorkingNorm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс определяет методы взаимодействия с данными, определенными сущностью {@link WorkingNorm}.
 */
@Repository
public interface WorkingNormRepository extends JpaRepository<WorkingNorm, Long>,
        FilterWorkingNormRepository {


    /**
     * Возвращает рабочую норму по продукту, сотруднику и статусу.
     * Используется для проверки существования рабочей нормы.
     *
     * @param product продукт
     * @param user    сотрудник
     * @param status  статус
     * @return {@code Optional}, содержащий рабочую норму или {@code Optional.empty()}, если рабочей нормы не найдено.
     */
    Optional<WorkingNorm> findByProductAndUserAndStatus(Product product,
                                                        User user,
                                                        Status status);

    /**
     * Возвращает рабочие нормы для сотрудника по статусу и временному промежутку.
     * Используется для получения списка рабочих норм сотрудника в заданном промежутке времени.
     *
     * @param user      сотрудник
     * @param status    статус
     * @param startTime начало
     * @param endTime   конец
     * @return список рабочих норм
     */
    List<WorkingNorm> findByUserAndStatusAndDeadlineBetweenOrderByDeadline(
            User user,
            Status status,
            LocalDateTime startTime,
            LocalDateTime endTime);

    /**
     * Поиск рабочих норм по статусу.
     * <p>
     * Используется для получения списка рабочих норм с заданным статусом,
     * который будет использован для анализа текущего собранного количества по отношению к заданной норме
     * и установленного времени сбора.
     *
     * @param status cтатус
     * @return список рабочих норм
     */
    List<WorkingNorm> findByStatus(Status status);
}
