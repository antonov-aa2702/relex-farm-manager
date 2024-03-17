package ru.relex.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Модель данных для произведенного продукта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "manufactured_products")
public class ManufacturedProduct {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * Объект продукта, который будет произведён
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Объект пользователя, кто производит продукт
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Количество произведенного продукта
     */
    @Column(name = "count")
    private Integer count;

    /**
     * Время производства
     */
    @Column(name = "time")
    private LocalDateTime time;
}
