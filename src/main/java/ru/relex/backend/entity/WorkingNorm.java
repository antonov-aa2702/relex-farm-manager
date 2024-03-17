package ru.relex.backend.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Модель данных для рабочей нормы.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "working_norms")
public class WorkingNorm {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * Объект продукта, к которому относится рабочая норма
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Объект пользователя, кто создал рабочую норму
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Необходимое количество для достижения рабочей нормы
     */
    @Column(name = "working_count")
    private Integer workingCount;

    /**
     * Текущее количество
     */
    @Column(name = "current_count")
    private Integer currentCount;

    /**
     * Крайний срок выполнения
     */
    @Column(name = "deadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    /**
     * Статус
     */
    @Column(name = "status")
    @Enumerated(STRING)
    private Status status;
}
