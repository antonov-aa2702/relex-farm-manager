package ru.relex.backend.entity;

/**
 * Перечисление для представления типа письма.
 */
public enum MailType {
    /**
     * Ежедневный отчет о собранных продуктах
     */
    DAILY_PRODUCT_REPORT,
    /**
     * Ежедневный аналитический отчет о выполненных рабочих нормах
     */
    DAILY_EMPLOYEE_ANALYTICS,
    /**
     * Ежедневный аналитический отчет с оценками всех сотрудников
     */
    DAILE_EMPLOYEE_SCORE_ANALYTICS,
    /**
     * Ежедневный персональный отчет для сотрудника с его оценкой за день
     */
    DAILY_EMPLOYEE_SCORE
}
