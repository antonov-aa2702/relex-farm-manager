package ru.relex.backend.util;


import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

/**
 * Класс для работы с датой и временем в приложении.
 */
@NoArgsConstructor(access = PRIVATE)
public class LocalDateTimeUtil {

    /**
     * Проверяет, что переданная дата является сегодняшней
     * и переданное время больше чем текущее, а также что
     * оно меньше чем время в часах и минутах, указанных в параметрах.
     * <p>
     * Используется для проверки установленного значения срока выполнения рабочей нормы.
     * Границы устанавливаются по согласованию с владельцем.
     *
     * @param deadline время срока выполнения
     * @param hour     крайний час для добавления
     * @param minute   краний минута для добавления
     * @return {@code true}, если дата валидна, иначе {@code false}
     */
    public static boolean isTodayDeadlineBefore(LocalDateTime deadline, int hour, int minute) {
        return deadline.isAfter(LocalDateTime.now()) &&
               deadline.toLocalDate().equals(LocalDate.now()) &&
               deadline.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute)));
    }
}
