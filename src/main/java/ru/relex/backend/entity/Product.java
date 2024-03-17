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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Модель данных для продукта.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "products")
public class Product {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * Название продукта
     */
    @Column(name = "name")
    private String name;

    /**
     * Объект единицы измерения
     */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
