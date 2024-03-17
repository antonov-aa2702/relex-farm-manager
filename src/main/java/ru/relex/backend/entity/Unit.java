package ru.relex.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Модель данных для единицы измерения.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "units")
public class Unit {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    /**
     * Название единицы измерения
     */
    @Column(name = "name")
    private String name;
}
