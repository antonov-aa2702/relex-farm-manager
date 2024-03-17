package ru.relex.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Модель данных для пользователя в системе.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /**
     * Имя
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Фамилия
     */
    @Column(name = "middle_name")
    private String middleName;

    /**
     * Отчество
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Электронный адрес
     */
    @Column(name = "email")
    private String email;

    /**
     * Пароль
     */
    @Column(name = "password")
    private String password;

    /**
     * Роль пользователя
     */
    @Column(name = "role")
    @Enumerated(STRING)
    private Role role;

    /**
     * Статус аккаунта пользователя
     */
    @Column(name = "enabled")
    private Boolean enabled;
}
